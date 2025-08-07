package net.hypejet.jet.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import java.util.Objects;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.exception.NetworkException;
import net.hypejet.jet.server.network.netty.decoder.PacketDecompressor;
import net.hypejet.jet.server.network.netty.decoder.PacketLengthDecoder;
import net.hypejet.jet.server.network.netty.decoder.RawPacketDecoder;
import net.hypejet.jet.server.network.netty.encoder.PacketCompressor;
import net.hypejet.jet.server.network.netty.encoder.PacketLengthEncoder;
import net.hypejet.jet.server.network.netty.encoder.RawPacketEncoder;
import net.hypejet.jet.server.network.netty.handler.RawPacketHandler;
import net.hypejet.jet.server.network.packet.RawPacket;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry.RegistryPacketSpecification;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerDisconnectPacket;
import net.hypejet.jet.server.network.packet.reader.ClientPacketReader;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.pack.ResourcePackHandler;
import net.hypejet.jet.server.network.session.task.HandshakeSessionTask;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.util.acquisition.NotNullObjectMappedAcquisition;
import net.hypejet.jet.server.util.unit.Unit;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of {@linkplain PlayerConnection a player connection}, which is handled
 * by {@link SocketChannel a socket channel}.
 *
 * @since 1.0
 */
public final class SocketPlayerConnection implements PlayerConnection, Thread.UncaughtExceptionHandler,
        NetworkDisconnectionHandler {

    private static final String RAW_PACKET_ENCODER = "minecraft-raw-packet-encoder";
    private static final String PACKET_COMPRESSOR = "minecraft-packet-compressor";
    private static final String PACKET_LENGTH_ENCODER = "minecraft-packet-length-encoder";

    private static final String RAW_PACKET_DECODER = "minecraft-raw-packet-decoder";
    private static final String PACKET_DECOMPRESSOR = "minecraft-packet-decompressor";
    private static final String PACKET_LENGTH_DECODER = "minecraft-packet-length-decoder";

    private static final String RAW_PACKET_HANDLER = "minecraft-raw-packet-reader";

    private static final Set<String> HANDLER_SET = Set.of(
            RAW_PACKET_ENCODER, PACKET_COMPRESSOR, PACKET_LENGTH_ENCODER,
            RAW_PACKET_DECODER, PACKET_DECOMPRESSOR, PACKET_LENGTH_DECODER,
            RAW_PACKET_HANDLER
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketPlayerConnection.class);

    private final SocketChannel channel;
    private final JetMinecraftServer server;

    private final ReentrantReadWriteLock playerLock = new ReentrantReadWriteLock();
    private JetPlayer player;

    private final NotNullObjectAcquirable<Session> session;
    private final ClientPacketReader clientPacketReader;

    private final ResourcePackHandler resourcePackHandler = new ResourcePackHandler(this);

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

        this.channel = Objects.requireNonNull(channel, "channel");
        this.server = Objects.requireNonNull(server, "server");

        // We update handlers after the instantiation and such an operation require to be executed in an event loop
        this.ensureInEventLoop();

        Session initialSession = new Session(ProtocolState.HANDSHAKE, this, new HandshakeSessionTask(this));
        this.session = new NotNullObjectAcquirable<>(initialSession);

        /* We need to update the handlers and initialize the client packet reader after the initial session is set,
           since they are going to be used. The session needs to be started later however, since it might send packets,
           which require handlers to be already set. */
        this.updateHandlers(-1);
        this.clientPacketReader = new ClientPacketReader(this, initialSession);

        // During the initialization, the session task must be started manually
        initialSession.sessionTask().start();
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

    @Override
    public @NonNull JetPlayer playerOrThrow() {
        JetPlayer player = this.player();
        if (player == null)
            throw new IllegalStateException("The player has not been initialized yet");
        return player;
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        try (NotNullObjectAcquisition<ProtocolState> protocolStateAcquisition = this.protocolState()) {
            CompletableFuture<?> disconnectPacketResultFuture;

            if (ServerPacketRegistry.isSupported(protocolStateAcquisition.get(), ServerDisconnectPacket.class)) {
                CompletableFuture<PacketSendResult> packetFuture = new CompletableFuture<>();
                disconnectPacketResultFuture = packetFuture;
                this.sendPacket(new ServerDisconnectPacket(reason), packetFuture); // FIXME: Do that later
            } else {
                disconnectPacketResultFuture = CompletableFuture.completedFuture(Unit.INSTANCE);
            }

            disconnectPacketResultFuture.handle((result, throwable) -> {
                this.close();
                return Unit.INSTANCE;
            });
        }
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
        try (NotNullObjectAcquisition<Session> sessionAcquisition = this.acquireSessionRead()) {
            sessionAcquisition.get().handleDisconnection();
        }

        if (this.player != null)
            this.player.handleDisconnection();
        this.clientPacketReader.handleDisconnection();
    }

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of
     * {@linkplain ProtocolState a protocol state} of this connection.
     *
     * @return the acquisition
     * @since 1.0
     */
    public @NonNull NotNullObjectAcquisition<ProtocolState> protocolState() {
        return new NotNullObjectMappedAcquisition<>(
                this.acquireSessionRead(),
                acquisition -> acquisition.get().protocolState()
        );
    }

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition}, which allows doing read-only
     * operations of a current {@linkplain Session session} of this connection.
     *
     * @return the object acquisition
     * @since 1.0
     */
    public @NonNull NotNullObjectAcquisition<Session> acquireSessionRead() {
        return this.session.acquireRead();
    }

    /**
     * Creates {@linkplain WriteNotNullObjectAcquisition a write not-null object acquisition}, which allows doing write
     * operations of a current {@linkplain Session session} of this connection.
     *
     * @return the write object acquisition
     * @since 1.0
     */
    public @NonNull WriteNotNullObjectAcquisition<Session> acquireSessionWrite() {
        return new WriteSessionAcquisition(this.session.acquireWrite());
    }

    /**
     * Sends {@linkplain ServerPacket a server packet} to a client backed by this connection without waiting for
     * a result.
     *
     * @param packet the server packet
     * @since 1.0
     */
    public void sendPacket(@NonNull ServerPacket packet) {
        this.sendPacket(packet, null);
    }

    /**
     * Sends {@linkplain ServerPacket a server packet} to a client backed by this connection.
     *
     * @param packet the server packet
     * @param resultFuture a completable future that should be completed when a result of the operation is available
     * @since 1.0
     */
    public void sendPacket(@NonNull ServerPacket packet,
                           @Nullable CompletableFuture<? super PacketSendResult> resultFuture) {
        try (NotNullObjectAcquisition<ProtocolState> protocolStateAcquisition = this.protocolState()) {
            RawPacket rawPacket = encode(packet, protocolStateAcquisition.get());

            ChannelPromise promise = resultFuture == null ? this.channel.voidPromise() : this.channel.newPromise();
            ChannelFuture channelFuture = this.channel.writeAndFlush(rawPacket, promise);

            if (!promise.isVoid() && resultFuture != null) {
                channelFuture.addListener(future -> {
                    Future.State futureState = future.state();
                    switch (futureState) {
                        case SUCCESS -> resultFuture.complete(PacketSendResult.SUCCESS);
                        case FAILED -> resultFuture.complete(PacketSendResult.NETWORK_ERROR);
                        case CANCELLED -> resultFuture.complete(PacketSendResult.CANCELLATION);
                        default -> resultFuture.completeExceptionally(new IllegalArgumentException(
                                String.format("The future is in an unexpected state of %s", futureState.name())
                        ));
                    }
                });
            }
        }
    }

    /**
     * Gets {@linkplain ClientPacketReader a client packet reader} of this connection.
     *
     * @return the client packet reader
     * @since 1.0
     */
    public @NonNull ClientPacketReader clientPacketReader() {
        return this.clientPacketReader;
    }

    /**
     * Gets {@linkplain ResourcePackHandler a resource pack handler} of this connection.
     *
     * @return the resource pack handler
     * @since 1.0
     */
    public @NonNull ResourcePackHandler resourcePackHandler() {
        return this.resourcePackHandler;
    }

    /**
     * Closes the connection.
     *
     * @since 1.0
     * @see SocketChannel#close()
     */
    public void close() {
        this.channel.close();
    }

    /**
     * Gets whether the connection is active.
     *
     * <p>Note that calling this method outside event loop may lead to race conditions.</p>
     *
     * @return {@code true} if the connection is active, {@code false} otherwise
     * @since 1.0
     */
    public boolean isActive() {
        return this.channel.isActive();
    }

    /**
     * Initializes the {@linkplain JetPlayer player} of this connection.
     *
     * @param player the player
     * @throws IllegalArgumentException if the player was already initialized
     * @since 1.0
     */
    public void initializePlayer(@NonNull JetPlayer player) {
        this.playerLock.writeLock().lock();
        try {
            if (this.player != null)
                throw new IllegalArgumentException("The player has been already initialized");
            this.player = player;
            this.server.registerPlayer(player);
        } finally {
            this.playerLock.writeLock().unlock();
        }
    }

    /**
     * Ensures that the caller thread is {@linkplain EventLoop an event loop} thread.
     *
     * @throws IllegalStateException if the caller thread is not an event loop thread
     * @since 1.0
     */
    public void ensureInEventLoop() {
        if (!this.channel.eventLoop().inEventLoop())
            throw new IllegalStateException("Current thread is not an event loop thread");
    }

    /**
     * Updates {@linkplain ChannelHandler channel handlers} of {@linkplain SocketChannel a socket channel} of this
     * connection.
     *
     * @param compressionThreshold a compression threshold that should be used for packet encoding and decoding
     * @since 1.0
     */
    public void updateHandlers(int compressionThreshold) {
        // Channel handlers can be updates safely only in an event loop
        this.ensureInEventLoop();

        ChannelPipeline pipeline = this.channel.pipeline();
        for (String handlerName : HANDLER_SET) {
            ChannelHandler handler = pipeline.get(handlerName);
            if (handler == null) continue;
            pipeline.remove(handler);
        }

        pipeline.addFirst(RAW_PACKET_ENCODER, new RawPacketEncoder(this));
        pipeline.addFirst(RAW_PACKET_DECODER, new RawPacketDecoder(this));

        pipeline.addBefore(RAW_PACKET_DECODER, PACKET_LENGTH_DECODER, new PacketLengthDecoder(this));
        pipeline.addBefore(RAW_PACKET_ENCODER, PACKET_LENGTH_ENCODER, new PacketLengthEncoder(this));

        pipeline.addAfter(RAW_PACKET_DECODER, RAW_PACKET_HANDLER, new RawPacketHandler(this));

        if (compressionThreshold < 0) return; // The compression is disabled
        pipeline.addBefore(RAW_PACKET_DECODER, PACKET_DECOMPRESSOR, new PacketDecompressor(this));
        pipeline.addBefore(RAW_PACKET_ENCODER, PACKET_COMPRESSOR, new PacketCompressor(this, compressionThreshold));
    }

    private static @NonNull RawPacket encode(@NonNull ServerPacket packet, @NonNull ProtocolState state) {
        Class<? extends ServerPacket> packetClass = packet.getClass();
        RegistryPacketSpecification<?> specification = ServerPacketRegistry.specificationFor(state, packetClass);

        if (specification == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a packet codec for packet %s in protocol state %s",
                    packetClass.getSimpleName(), state
            ));
        }

        ByteBuf buf = Unpooled.buffer();
        try {
            write(specification, buf, packet); // Write the packet body with java generics
            return new RawPacket(specification.packetIdentifier(), NetworkUtil.readRemainingBytes(buf));
        } finally {
            buf.release();
        }
    }

    private static <P extends ServerPacket> void write(@NonNull RegistryPacketSpecification<P> specification,
                                                       @NonNull ByteBuf buf, @NonNull ServerPacket packet) {
        specification.packetWriter().write(buf, specification.packetClass().cast(packet));
    }

    /**
     * Represents a result of sending {@linkplain ServerPacket a server packet}.
     *
     * @since 1.0
     * @see ServerPacket
     */
    public enum PacketSendResult {
        /**
          {@linkplain PacketSendResult A packet send result}, which represents a success.
         *
         * @since 1.0
         */
        SUCCESS,
        /**
         * {@linkplain PacketSendResult A packet send result}, which represents a cancellation of packet sending, which
         * could have been caused by an internal reason.
         *
         * @since 1.0
         */
        CANCELLATION,
        /**
         * {@linkplain PacketSendResult A packet send result}, which represents a network error, which has been
         * already handled.
         *
         * @since 1.0
         */
        NETWORK_ERROR
    }

    /**
     * Represents {@linkplain WriteNotNullObjectAcquisition a write not-null object acquisition} of
     * {@linkplain Session a session}, which does essential tasks when a session is being set.
     *
     * @param originalAcquisition an original session acquisition
     * @since 1.0
     */
    private record WriteSessionAcquisition(@NonNull WriteNotNullObjectAcquisition<Session> originalAcquisition)
            implements WriteNotNullObjectAcquisition<Session> {
        /**
         * Constructs the {@linkplain WriteSessionAcquisition write session acquisition}.
         *
         * @param originalAcquisition an original session acquisition
         * @since 1.0
         */
        private WriteSessionAcquisition {
            Objects.requireNonNull(originalAcquisition, "original acquisition");
        }

        @Override
        public @NotNull Session get() {
            return this.originalAcquisition.get();
        }

        @Override
        public void set(@NotNull Session value) {
            if (this.originalAcquisition.get() == value) {
                throw new IllegalArgumentException("A session cannot be set to the same value that it is currently" +
                        " bound to");
            }

            // There is no need for nullability, owner checks and lock ensuring, since the method will do that for us
            this.originalAcquisition.set(value);

            /* Sessions are hard to implement in terms of thread-safety and avoiding race-conditions. A write
               acquisition is needed to be created when a packet informing that the session has been finished is sent.
               We need to handle an acknowledgment to that then, se we cannot rely on the same session acquirable,
               that is why sometimes another session fields are made. Due to that, we need to update these fields when
               the session is set. */
            value.connection().clientPacketReader().updateSession(value);
            value.sessionTask().start();
        }

        @Override
        public boolean isUnlocked() {
            return this.originalAcquisition.isUnlocked();
        }

        @Override
        public void close() {
            this.originalAcquisition.close();
        }

        @Override
        public void ensurePermittedAndLocked() {
            this.originalAcquisition.ensurePermittedAndLocked();
        }

        @Override
        public @NotNull AcquisitionType acquisitionType() {
            return this.originalAcquisition.acquisitionType();
        }
    }
}