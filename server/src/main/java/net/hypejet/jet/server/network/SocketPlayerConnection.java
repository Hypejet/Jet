package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.events.packet.PacketSendEvent;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.network.packet.server.common.ServerDisconnectPacket;
import net.hypejet.jet.network.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.acquisition.AbstractAcquirable;
import net.hypejet.jet.server.acquisition.mapped.MappedAcquisition;
import net.hypejet.jet.server.acquisition.value.AcquirableValue;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.exception.NetworkException;
import net.hypejet.jet.server.network.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.netty.decoder.PacketDecoder;
import net.hypejet.jet.server.network.netty.decoder.PacketDecompressor;
import net.hypejet.jet.server.network.netty.decoder.PacketLengthDecoder;
import net.hypejet.jet.server.network.netty.encoder.PacketCompressor;
import net.hypejet.jet.server.network.netty.encoder.PacketEncoder;
import net.hypejet.jet.server.network.netty.encoder.PacketLengthEncoder;
import net.hypejet.jet.server.network.netty.reader.PacketReader;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.HandshakeTask;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of {@link PlayerConnection}, which is handled
 * by {@link SocketChannel a socket channel}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class SocketPlayerConnection implements PlayerConnection, Thread.UncaughtExceptionHandler,
        NetworkDisconnectionHandler {

    private static final String PACKET_ENCODER = "minecraft-packet-encoder";
    private static final String PACKET_COMPRESSOR = "minecraft-packet-compressor";
    private static final String PACKET_LENGTH_ENCODER = "minecraft-packet-length-encoder";

    private static final String PACKET_DECODER = "minecraft-packet-decoder";
    private static final String PACKET_DECOMPRESSOR = "minecraft-packet-decompressor";
    private static final String PACKET_LENGTH_DECODER = "minecraft-packet-length-decoder";

    private static final String PACKET_READER = "minecraft-packet-reader";

    private static final Set<String> HANDLER_SET = Set.of(
            PACKET_ENCODER, PACKET_COMPRESSOR, PACKET_LENGTH_ENCODER,
            PACKET_DECODER, PACKET_DECOMPRESSOR, PACKET_LENGTH_DECODER,
            PACKET_READER
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketPlayerConnection.class);

    private final SocketChannel channel;
    private final JetMinecraftServer server;

    private final AcquirableValue<Session> sessionAcquirableValue;

    private final ReentrantReadWriteLock playerLock = new ReentrantReadWriteLock();
    private JetPlayer player;

    private final AcquirableValue<Integer> compressionThreshold = new AcquirableValue<>(-1);

    /**
     * Constructs the {@link SocketPlayerConnection socket player connection}.
     *
     * @param channel a socket channel, which handles the connection
     * @param server a minecraft server owning the connection
     * @since 1.0
     */
    public SocketPlayerConnection(@NonNull SocketChannel channel, @NonNull JetMinecraftServer server) {
        if (!(channel.eventLoop() instanceof SingleThreadEventLoop)) {
            channel.close();
            throw new IllegalArgumentException("The event loop must be a single-threaded event loop");
        }

        this.channel = NullabilityUtil.requireNonNull(channel, "channel");
        this.server = NullabilityUtil.requireNonNull(server, "server");
        this.sessionAcquirableValue = new SessionAcquirableValue(this);

        this.updateHandlers();
    }

    @Override
    public @NonNull Acquisition<ProtocolState> protocolState() {
        return new MappedAcquisition<>(this.sessionAcquirableValue.acquire(), Session::protocolState);
    }

    @Override
    public @NonNull CompletableFuture<@Nullable ServerPacket> sendPacket(@NonNull ServerPacket packet) {
        if (this.isClosed())
            return CompletableFuture.completedFuture(null);

        PacketSendEvent event = new PacketSendEvent(packet);
        this.server.eventNode().call(event);

        if (event.isCancelled())
            return CompletableFuture.completedFuture(null);
        ServerPacket finalPacket = event.getPacket(); // Java requires this to access the packet from a lambda function

        CompletableFuture<ServerPacket> packetFuture = new CompletableFuture<>();
        this.channel.writeAndFlush(packet).addListener(future -> {
            switch (future.state()) {
                case SUCCESS -> packetFuture.complete(finalPacket);
                case FAILED -> packetFuture.completeExceptionally(future.exceptionNow());
                case CANCELLED -> packetFuture.cancel(false);
            }
        });

        return packetFuture;
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        try (Acquisition<Session> ignoredAcquisition = this.sessionAcquirableValue.acquire()) {
            // TODO: Do checks? Check whether the acquisition is actually needed?
            this.sendPacket(new ServerDisconnectPacket(reason));
            this.close();
        }
    }

    @Override
    public @NonNull JetMinecraftServer server() {
        return this.server;
    }

    @Override
    public @NonNull JetPlayer playerOrThrow() {
        JetPlayer player = this.player();
        if (player == null)
            throw new IllegalStateException("The player has not been initialized yet");
        return player;
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

    @Override
    public boolean isClosed() {
        return !this.channel.isActive();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof NetworkException)
            return; // The exception has been already handled
        this.close(); // Close the connection to avoid more issues

        NetworkException networkException = new NetworkException(this, e);
        LOGGER.error("A network error occurred in thread {}", t.getName(), networkException);
    }

    @Override
    public void handleDisconnection() {
        this.ensureInEventLoop();

        try (Acquisition<Session> sessionAcquisition = this.createOrReuseSessionAcquisition()) {
            sessionAcquisition.get().handleDisconnection();
        }

        if (this.player != null)
            this.server.unregisterPlayer(this.player);
    }

    /**
     * Closes the connection, nothing will happen if the connection has been already closed.
     *
     * @since 1.0
     */
    public void close() {
        try {
            if (!this.channel.isActive()) return; // The connection has been already closed
            this.channel.close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a compression threshold of this connection.
     *
     * @param compressionThreshold the compression threshold
     * @throws IllegalStateException if the current protocol state of the player is not {@link ProtocolState#LOGIN}
     * @since 1.0
     */
    public void setCompressionThreshold(int compressionThreshold) {
        try (Acquisition<Session> sessionAcquisition = this.createOrReuseSessionAcquisition()) {
            // TODO: This can be replaced with an interface
            if (sessionAcquisition.get().protocolState() != ProtocolState.LOGIN) {
                throw new IllegalStateException("You cannot set a compression threshold in" +
                        " protocol state other than login");
            }

            ServerPacket packet = this.sendPacket(new ServerEnableCompressionLoginPacket(compressionThreshold)).get();
            if (!(packet instanceof ServerEnableCompressionLoginPacket(int threshold)))
                return;

            try (MutableAcquisition<Integer> thresholdAcquisition = this.compressionThreshold.acquireMutable()) {
                /* Handlers need to be updated after the compression threshold
                   is set, because that field is going to be used. */
                thresholdAcquisition.set(threshold);
                this.updateHandlers();
            }
        } catch (ExecutionException | InterruptedException exception) {
            // TODO: Find out if it is necessary
            this.uncaughtException(Thread.currentThread(), exception);
        }
    }

    /**
     * Creates {@linkplain Acquisition an acquisition} of the session or uses a reused one if the caller thread already
     * created an acquisition. See {@link AbstractAcquirable#acquire()} for more information.
     *
     * @return the acquisition
     * @see AbstractAcquirable#acquire()
     */
    public @NonNull Acquisition<Session> createOrReuseSessionAcquisition() {
        return this.sessionAcquirableValue.acquire();
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
            this.server.registerPlayer(player);
        } finally {
            this.playerLock.writeLock().unlock();
        }
    }

    /**
     * Ensures that the caller thread is {@linkplain io.netty.channel.EventLoop an event loop} thread.
     *
     * @since 1.0
     * @throws IllegalStateException if the caller thread is not an event loop thread
     */
    public void ensureInEventLoop() {
        if (!this.channel.eventLoop().inEventLoop())
            throw new IllegalStateException("Current thread is not in an event loop");
    }

    /**
     * Submits a task to {@linkplain io.netty.channel.EventLoop an event loop}.
     *
     * @param task the task
     * @return a future to manage the task
     * @since 1.0
     */
    public @NonNull Future<?> submitToEventLoop(@NonNull Runnable task) {
        return this.channel.eventLoop().submit(task);
    }

    private void updateHandlers() {
        this.ensureInEventLoop();
        try (Acquisition<Integer> compressionThresholdAcquisition = this.compressionThreshold.acquire()) {
            ChannelPipeline pipeline = this.channel.pipeline();
            for (String handlerName : HANDLER_SET) {
                ChannelHandler handler = pipeline.get(handlerName);
                if (handler == null) continue;
                pipeline.remove(handler);
            }

            pipeline.addFirst(PACKET_ENCODER, new PacketEncoder(this));
            pipeline.addFirst(PACKET_DECODER, new PacketDecoder(this));
            pipeline.addBefore(PACKET_DECODER, PACKET_LENGTH_DECODER, new PacketLengthDecoder(this));
            pipeline.addBefore(PACKET_ENCODER, PACKET_LENGTH_ENCODER, new PacketLengthEncoder(this));
            pipeline.addAfter(PACKET_DECODER, PACKET_READER, new PacketReader(this));

            int threshold = compressionThresholdAcquisition.get();
            if (threshold >= 0) {
                pipeline.addBefore(PACKET_DECODER, PACKET_DECOMPRESSOR, new PacketDecompressor(this));
                pipeline.addBefore(PACKET_ENCODER, PACKET_COMPRESSOR, new PacketCompressor(this, threshold));
            }
        }
    }

    /**
     * Represents {@linkplain AcquirableValue an acquirable value}, which holds {@linkplain Session a session}.
     *
     * @since 1.0
     * @author Codestech
     * @see Session
     * @see AcquirableValue
     */
    private static final class SessionAcquirableValue extends AcquirableValue<Session> {

        private final SocketPlayerConnection connection;

        /**
         * Constructs the {@linkplain SessionAcquirableValue session acquirable value}.
         *
         * @param connection a connection that should own the acquirable value
         * @since 1.0
         */
        private SessionAcquirableValue(@NonNull SocketPlayerConnection connection) {
            super(acquirable -> {
                connection.ensureInEventLoop();
                Session session = new Session(ProtocolState.HANDSHAKE, connection,
                        new HandshakeTask(acquirable, connection));
                session.startSession();
                return session;
            });
            this.connection = connection;
        }

        @Override
        protected void onPreSet(@NonNull Session value, @NonNull Session currentValue) {
            this.connection.ensureInEventLoop();
        }

        @Override
        protected void onPostSet(@NonNull Session value, @NonNull Session previousValue) {
            value.startSession();
        }
    }
}