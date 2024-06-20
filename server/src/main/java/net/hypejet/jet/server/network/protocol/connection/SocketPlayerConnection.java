package net.hypejet.jet.server.network.protocol.connection;

import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.packet.PacketSendEvent;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.session.Session;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Session<?> session;

    private ProtocolState state = ProtocolState.HANDSHAKE;
    private int compressionThreshold = -1;

    private Player player;

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
        return this.state;
    }

    @Override
    public @Nullable ServerPacket sendPacket(@NonNull ServerPacket packet) {
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
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        ServerPacket packet = switch (this.state) {
            case LOGIN -> new ServerDisconnectLoginPacket(reason);
            case CONFIGURATION -> new ServerDisconnectConfigurationPacket(reason);
            case PLAY -> null; // TODO
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
    public @Nullable Player player() {
        return this.player;
    }

    /**
     * Closes the {@link PlayerConnection player connection}.
     *
     * @since 1.0
     */
    public void close() {
        try {
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
        this.state = state;
    }

    /**
     * Sets a compression threshold of this connection.
     *
     * @param compressionThreshold the compression threshold
     * @since 1.0
     */
    public void setCompressionThreshold(int compressionThreshold) {
        ServerPacket finalPacket = this.sendPacket(new ServerEnableCompressionLoginPacket(compressionThreshold));
        if (finalPacket instanceof ServerEnableCompressionLoginPacket packet)
            this.compressionThreshold = packet.threshold();
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
    public @Nullable Session<?> getSession() {
        return this.session;
    }

    /**
     * Sets a current {@linkplain Session session} of this connection.
     *
     * @param session the session
     * @since 1.0
     */
    public void setSession(@Nullable Session<?> session) {
        this.session = session;
    }

    /**
     * Initializes the {@linkplain Player player} on this connection.
     *
     * @param player the player
     * @since 1.0
     * @throws IllegalArgumentException if the player was already initialized
     */
    public void initializePlayer(@NonNull Player player) {
        if (this.player != null)
            throw new IllegalArgumentException("The player was already initialized");
        this.player = player;
    }
}