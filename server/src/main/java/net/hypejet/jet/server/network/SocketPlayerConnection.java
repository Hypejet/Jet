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
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.events.packet.PacketSendEvent;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.network.packet.server.common.ServerDisconnectPacket;
import net.hypejet.jet.network.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
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
import net.hypejet.jet.server.network.packet.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.HandshakeTask;
import net.hypejet.jet.server.util.unit.Unit;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
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

    private final AcquirableValue<Session> session;

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

        // We update handlers after the instantiation and such an operation require to be executed in an event loop
        this.ensureInEventLoop();

        Session initialSession = new Session(ProtocolState.HANDSHAKE, this);
        this.session = new AcquirableSession(initialSession, channel);

        /* We need to update the handlers after the initial session is set, since they are going to be used.
           The session needs to be started later however, since it might send packets, which require handlers to
           be already set. */
        this.updateHandlers(-1);
        initialSession.startSession(new HandshakeTask(this));
    }

    @Override
    public @NonNull Acquisition<ProtocolState> protocolState() {
        return new MappedAcquisition<>(this.session.acquire(), Session::protocolState);
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
        try (Acquisition<Session> sessionAcquisition = this.session.acquire()) {
            sessionAcquisition.get().handleDisconnection();
        }

        if (this.player != null)
            this.server.unregisterPlayer(this.player);
    }

    /**
     * Gets {@linkplain AcquirableValue an acquirable value} of {@linkplain Session a session}.
     *
     * @return the acquirable value
     * @since 1.0
     */
    public @NonNull AcquirableValue<Session> session() {
        return this.session;
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
        // No need for a lock for the handlers, we just require the code to be executed in the event loop
        this.ensureInEventLoop();

        try (Acquisition<ProtocolState> protocolStateAcquisition = this.protocolState()) {
            ChannelPipeline pipeline = this.channel.pipeline();
            for (String handlerName : HANDLER_SET) {
                ChannelHandler handler = pipeline.get(handlerName);
                if (handler == null) continue;
                pipeline.remove(handler);
            }

            ProtocolState protocolState = protocolStateAcquisition.get();
            pipeline.addFirst(PACKET_ENCODER, new PacketEncoder(this, protocolState));
            pipeline.addFirst(PACKET_DECODER, new PacketDecoder(this, protocolState));

            pipeline.addBefore(PACKET_DECODER, PACKET_LENGTH_DECODER, new PacketLengthDecoder(this));
            pipeline.addBefore(PACKET_ENCODER, PACKET_LENGTH_ENCODER, new PacketLengthEncoder(this));
            pipeline.addAfter(PACKET_DECODER, PACKET_READER, new PacketReader(this));

            if (compressionThreshold < 0) return;
            pipeline.addBefore(PACKET_DECODER, PACKET_DECOMPRESSOR, new PacketDecompressor(this));
            pipeline.addBefore(PACKET_ENCODER, PACKET_COMPRESSOR, new PacketCompressor(this, compressionThreshold));
        }
    }

    /**
     * Represents {@linkplain AcquirableValue an acquirable value}, which holds {@linkplain Session a session}.
     *
     * @since 1.0
     * @see Session
     * @see AcquirableValue
     */
    private static final class AcquirableSession extends AcquirableValue<Session> {

        private final SocketChannel channel;

        /**
         * Constructs the {@linkplain AcquirableSession acquirable session}.
         *
         * @param initialValue an initial session
         * @since 1.0
         */
        public AcquirableSession(@NonNull Session initialValue, @NonNull SocketChannel channel) {
            super(initialValue);
            this.channel = NullabilityUtil.requireNonNull(channel, "channel");
        }

        @Override
        protected void onSet(@NonNull Session value) {
            ChannelPipeline pipeline = this.channel.pipeline();
            ProtocolState protocolState = value.protocolState();

            /* Packet encoders and decoders hold a protocol state, because they need it for packet serialization
               and sometimes cannot use the protocol state from a session. An example of such a case is where
               a session has been acquired with mutability by another thread and the acquisition is closed only when
               a packet is received, so a packet handler waits for it to unlock, but it unlocks only when the packet
               is received, therefore we get a deadlock. This kind of situation is present in a handshake session
               for example. */

            PacketEncoder encoder = pipeline.get(PacketEncoder.class);
            if (encoder != null)
                encoder.updateProtocolState(protocolState);

            PacketDecoder decoder = pipeline.get(PacketDecoder.class);
            if (decoder != null)
                decoder.updateProtocolState(protocolState);
        }
    }
}