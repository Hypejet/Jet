package net.hypejet.jet.server.network.protocol.connection;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.events.packet.PacketSendEvent;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.netty.decoder.PacketDecompressor;
import net.hypejet.jet.server.network.netty.encoder.PacketCompressor;
import net.hypejet.jet.server.session.JetHandshakeSession;
import net.hypejet.jet.server.session.Session;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_COMPRESSOR;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_DECODER;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_DECOMPRESSOR;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_ENCODER;

/**
 * Represents an implementation of {@link PlayerConnection}, which is handled by netty's
 * {@link SocketChannel socket channel}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class SocketPlayerConnection implements PlayerConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketPlayerConnection.class);

    private final SocketChannel channel;
    private final JetMinecraftServer server;

    private final ReentrantReadWriteLock playerLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock protocolStateLock = new ReentrantReadWriteLock();

    private Session<?> session = new JetHandshakeSession(this);

    private ProtocolState state = ProtocolState.HANDSHAKE;
    private int compressionThreshold = -1;

    private JetPlayer player;

    /**
     * Constructs a {@link SocketPlayerConnection socket player connection}.
     *
     * @param channel a {@link SocketChannel socket channel}, which handles the connection
     * @param server a {@linkplain MinecraftServer minecraft server} owning the connection
     * @since 1.0
     */
    public SocketPlayerConnection(@NonNull SocketChannel channel, @NonNull JetMinecraftServer server) {
        this.channel = channel;
        this.server = server;
    }

    @Override
    public @NonNull ProtocolState getProtocolState() {
        this.protocolStateLock.readLock().lock();
        try {
            return this.state;
        } finally {
            this.protocolStateLock.readLock().unlock();
        }
    }

    @Override
    public @Nullable ServerPacket sendPacket(@NonNull ServerPacket packet) {
        this.protocolStateLock.readLock().lock();
        try {
            PacketSendEvent event = new PacketSendEvent(packet);
            this.server.eventNode().call(event);

            if (event.isCancelled()) return null;

            packet = event.getPacket();
            ProtocolState currentState = this.state;

            if (packet.state() != currentState) {
                LOGGER.error("Packet {} cannot be handled during {} protocol state", packet, currentState,
                        new IllegalArgumentException(packet.toString()));
                return null;
            }

            this.channel.writeAndFlush(packet, this.channel.voidPromise());
            return packet;
        } finally {
            this.protocolStateLock.readLock().unlock();
        }
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        ServerPacket packet = switch (this.state) {
            case LOGIN -> new ServerDisconnectLoginPacket(reason);
            case CONFIGURATION -> new ServerDisconnectConfigurationPacket(reason);
            case PLAY -> new ServerDisconnectPlayPacket(reason);
            default -> null;
        };

        if (packet != null) {
            this.sendPacket(packet);
        }

        this.close();
    }

    @Override
    public int compressionThreshold() {
        return this.compressionThreshold;
    }

    @Override
    public @NonNull JetMinecraftServer server() {
        return this.server;
    }

    @Override
    public @Nullable JetPlayer player() {
        try {
            this.playerLock.readLock().lock();
            return this.player;
        } finally {
            this.playerLock.readLock().unlock();
        }
    }

    /**
     * Closes the {@link PlayerConnection player connection}, nothing will.
     *
     * @since 1.0
     */
    public void close() {
        try {
            if (!this.channel.isActive()) return; // The connection is already closed
            this.channel.close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a protocol state of this connection.
     *
     * @param state the protocol state
     * @since 1.0
     */
    public void setProtocolState(@NonNull ProtocolState state) {
        this.protocolStateLock.writeLock().lock();
        try {
            this.state = state;
        } finally {
            this.protocolStateLock.writeLock().unlock();
        }
    }

    /**
     * Sets a compression threshold of this connection.
     *
     * @param compressionThreshold the compression threshold
     * @throws IllegalStateException if the current protocol state of the player is
     *                               not {@link ProtocolState#LOGIN}
     * @since 1.0
     */
    public void setCompressionThreshold(int compressionThreshold) {
        if (this.getProtocolState() != ProtocolState.LOGIN) {
            throw new IllegalStateException("You cannot set a compression threshold in protocol state other than" +
                    "login");
        }

        ServerPacket finalPacket = this.sendPacket(new ServerEnableCompressionLoginPacket(compressionThreshold));

        if (finalPacket instanceof ServerEnableCompressionLoginPacket packet) {
            compressionThreshold = packet.threshold();
            this.compressionThreshold = compressionThreshold;

            ChannelPipeline pipeline = this.channel.pipeline();

            ChannelHandler packetDecompressor = pipeline.get(PACKET_DECOMPRESSOR);
            ChannelHandler packetCompressor = pipeline.get(PACKET_COMPRESSOR);

            if (compressionThreshold < 0) {
                if (packetDecompressor != null)
                    pipeline.remove(packetDecompressor);
                if (packetCompressor != null)
                    pipeline.remove(packetCompressor);
                return;
            }

            if (packetDecompressor != null && packetCompressor != null) return;

            pipeline.addBefore(PACKET_DECODER, PACKET_DECOMPRESSOR, new PacketDecompressor());
            pipeline.addBefore(PACKET_ENCODER, PACKET_COMPRESSOR, new PacketCompressor(this));
        }
    }

    /**
     * Gets a {@link SocketChannel socket channel} of the {@link PlayerConnection player connection}.
     *
     * @return the socket channel
     * @since 1.0
     */
    public @NonNull SocketChannel getChannel() {
        return this.channel;
    }

    /**
     * Gets a current {@linkplain Session session} of this connection.
     *
     * @return the session, {@code null} if the connection is not in any session
     * @since 1.0
     */
    public @NonNull Session<?> getSession() {
        return this.session;
    }

    /**
     * Sets a current {@linkplain Session session} of this connection.
     *
     * @param session the session
     * @since 1.0
     */
    public void setSession(@NonNull Session<?> session) {
        this.session = Objects.requireNonNull(session, "The session must not be null");
    }

    /**
     * Initializes the {@linkplain JetPlayer player} on this connection.
     *
     * @param player the player
     * @since 1.0
     * @throws IllegalArgumentException if the player was already initialized
     */
    public void initializePlayer(@NonNull JetPlayer player) {
        this.playerLock.writeLock().lock();
        try {
            if (this.player != null)
                throw new IllegalArgumentException("The player was already initialized");
            this.player = player;
        } finally {
            this.playerLock.writeLock().unlock();
        }
    }
}