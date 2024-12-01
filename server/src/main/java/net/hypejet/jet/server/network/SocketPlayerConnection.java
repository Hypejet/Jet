package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.SucceededFuture;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.events.packet.PacketSendEvent;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.network.packet.server.common.ServerDisconnectPacket;
import net.hypejet.jet.network.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.acquisition.AbstractAcquirable;
import net.hypejet.jet.server.acquisition.mapped.MappedAcquisition;
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
import net.hypejet.jet.server.network.packet.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.HandshakeTask;
import net.hypejet.jet.server.util.unit.Unit;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
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

    private final ReentrantReadWriteLock playerLock = new ReentrantReadWriteLock();
    private JetPlayer player;

    private final SessionAcquirable session;

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

        this.ensureInEventLoop();
        this.updateHandlers(-1);

        this.session = new SessionAcquirable(this);
        this.session.initialize(new Session(
                ProtocolState.HANDSHAKE, this,
                () -> new HandshakeTask(this)
        ));
    }

    @Override
    public @NonNull Acquisition<ProtocolState> protocolState() {
        return new MappedAcquisition<>(this.createSessionAcquisition(), Session::protocolState);
    }

    @Override
    public @NonNull CompletableFuture<PacketSendResult> sendPacket(@NonNull ServerPacket packet) {
        if (this.isClosed())
            return CompletableFuture.completedFuture(PacketSendResult.cancellation());

        PacketSendEvent event = new PacketSendEvent(packet);
        this.server.eventNode().call(event);

        if (event.isCancelled())
            return CompletableFuture.completedFuture(PacketSendResult.cancellation());
        ServerPacket finalPacket = event.getPacket(); // Java requires this to access the packet from a lambda function

        CompletableFuture<PacketSendResult> packetFuture = new CompletableFuture<>();
        this.channel.writeAndFlush(finalPacket).addListener(future -> {
            Future.State futureState = future.state();
            switch (futureState) {
                case SUCCESS -> packetFuture.complete(new PacketSendResult.Success(finalPacket));
                case FAILED -> packetFuture.complete(PacketSendResult.networkError());
                case CANCELLED -> packetFuture.complete(PacketSendResult.cancellation());
                default -> packetFuture.completeExceptionally(new IllegalArgumentException(
                        String.format("The future is in an unexpected state of %s", futureState.name())
                ));
            }
        });

        return packetFuture;
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        // TODO: Run in an event loop
        CompletableFuture<?> packetSendFuture;

        try (Acquisition<ProtocolState> protocolStateAcquisition = this.protocolState()) {
            if (ServerPacketRegistry.isSupported(protocolStateAcquisition.get(), ServerDisconnectPacket.class))
                packetSendFuture = this.sendPacket(new ServerDisconnectPacket(reason));
            else packetSendFuture = CompletableFuture.completedFuture(Unit.INSTANCE);

            packetSendFuture.handle((result, throwable) -> {
                this.close();
                return Unit.INSTANCE;
            });
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
        return !this.channel.isActive(); // TODO
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
        try (Acquisition<Session> sessionAcquisition = this.createSessionAcquisition()) {
            sessionAcquisition.get().handleDisconnection();
        }

        if (this.player != null)
            this.server.unregisterPlayer(this.player);
    }

    public @NonNull Acquisition<Session> createSessionAcquisition() {
        return this.session.acquire();
    }

    public @NonNull MutableAcquisition<Session> createMutableSessionAcquisition() {
        return this.session.createMutableAcquisition();
    }

    /**
     * Closes the connection, nothing will happen if the connection has been already closed.
     *
     * @since 1.0
     */
    public void close() {
        this.submitToEventLoop(() -> {
            if (!this.isClosed()) return; // The connection has been already closed
            this.channel.close();
        });
    }

    /**
     * Sets a compression threshold of this connection.
     *
     * @param compressionThreshold the compression threshold
     * @throws IllegalStateException if the current protocol state of the player is not {@link ProtocolState#LOGIN}
     * @since 1.0
     */
    public void setCompressionThreshold(int compressionThreshold) {
        this.ensureInEventLoop(); // The handlers can be updates only in event loop threads
        try {
            ServerEnableCompressionLoginPacket packet = new ServerEnableCompressionLoginPacket(compressionThreshold);
            if (!(this.sendPacket(packet).get() instanceof PacketSendResult.Success(ServerPacket finalPacket))) return;

            if (!(finalPacket instanceof ServerEnableCompressionLoginPacket compressionPacket)) return;
            packet = compressionPacket;
            compressionThreshold = packet.compressionThreshold();

            this.updateHandlers(compressionThreshold);
        } catch (Throwable throwable) {
            Thread currentThread = Thread.currentThread();
            this.uncaughtException(currentThread, throwable);

            if (throwable instanceof InterruptedException)
                currentThread.interrupt(); // Restore the interrupted status
        }
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
     * Ensures that the caller thread is {@linkplain EventLoop an event loop} thread.
     *
     * @since 1.0
     * @throws IllegalStateException if the caller thread is not an event loop thread
     */
    public void ensureInEventLoop() {
        if (!this.channel.eventLoop().inEventLoop())
            throw new IllegalStateException("Current thread is not in an event loop");
    }

    /**
     * Submits {@linkplain Runnable a runnable} task to {@linkplain EventLoop an event loop}. If the caller thread is
     * an event loop thread then the task is executed immediately.
     *
     * @param task the task
     * @return a future to manage the task
     * @since 1.0
     */
    public @NonNull Future<?> submitToEventLoop(@NonNull Runnable task) {
        EventLoop eventLoop = this.channel.eventLoop();
        if (eventLoop.inEventLoop()) {
            try {
                return new SucceededFuture<>(eventLoop, Unit.INSTANCE);
            } catch (Exception exception) {
                return new FailedFuture<>(eventLoop, exception);
            }
        }
        return eventLoop.submit(task);
    }

    private void updateHandlers(int compressionThreshold) {
        this.ensureInEventLoop();

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

        if (compressionThreshold >= 0) {
            pipeline.addBefore(PACKET_DECODER, PACKET_DECOMPRESSOR, new PacketDecompressor(this));
            pipeline.addBefore(PACKET_ENCODER, PACKET_COMPRESSOR, new PacketCompressor(this, compressionThreshold));
        }
    }

    private static final class SessionAcquirable extends AbstractAcquirable<Session> {

        private final SocketPlayerConnection playerConnection;

        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private @MonotonicNonNull Session session;

        private SessionAcquirable(@NonNull SocketPlayerConnection playerConnection) {
            this.playerConnection = NullabilityUtil.requireNonNull(playerConnection, "player connection");
        }

        @Override
        protected @NonNull Acquisition<Session> createAcquisition() {
            return new SessionAcquisition(this, this.lock.readLock());
        }

        private @NonNull MutableAcquisition<Session> createMutableAcquisition() {
            return new MutableSessionAcquisition(this);
        }

        // Unfortunately, this is probably the only method to achieve correctness with acquisitions and sessions
        private void initialize(@NonNull Session session) {
            this.playerConnection.ensureInEventLoop();
            if (this.isInitialized())
                throw new IllegalStateException("The session has been already initialized");
            this.set(session);
        }

        private boolean isInitialized() {
            return this.session != null;
        }

        private void ensureInitialized() {
            if (!this.isInitialized())
                throw new IllegalStateException("The session has been not initialized");
        }

        private void set(@NonNull Session session) {
            this.session = NullabilityUtil.requireNonNull(session, "value");
            session.startSession();
        }

        private static final class MutableSessionAcquisition extends SessionAcquisition
                implements MutableAcquisition<Session> {

            private MutableSessionAcquisition(@NonNull SessionAcquirable acquirable) {
                super(acquirable, acquirable.lock.writeLock());
                acquirable.playerConnection.ensureInEventLoop();
            }

            @Override
            public void set(@NonNull Session value) {
                this.runChecksAndEnsureInitialized();
                this.acquirable.set(value);
            }
        }

        private static class SessionAcquisition extends AbstractAcquirable.AbstractAcquisition<Session> {

            protected final SessionAcquirable acquirable;

            private SessionAcquisition(@NonNull SessionAcquirable acquirable, @NonNull Lock lock) {
                super(acquirable, lock);
                this.acquirable = acquirable;
            }

            @Override
            public @NonNull Session get() {
                this.runChecksAndEnsureInitialized();
                return this.acquirable.session;
            }

            protected void runChecksAndEnsureInitialized() {
                this.runChecks();
                this.acquirable.ensureInitialized();
            }
        }
    }
}