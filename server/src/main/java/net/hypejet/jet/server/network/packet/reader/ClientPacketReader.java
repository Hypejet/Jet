package net.hypejet.jet.server.network.packet.reader;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.RawPacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;

/**
 * Represents something that decodes {@linkplain RawPacket raw packets} into {@linkplain ClientPacket client packets}
 * and handles the decoded packets.
 *
 * @since 1.0
 * @see RawPacket
 * @see ClientPacket
 */
public final class ClientPacketReader implements NetworkDisconnectionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPacketReader.class);

    private final SocketPlayerConnection connection;
    private final Queue<RawPacket> packetQueue = new ConcurrentLinkedQueue<>();

    private final BooleanAcquirable paused = new BooleanAcquirable();
    private final Condition resumeCondition = this.paused.newCondition();

    // More about existence of this field in a comment in SocketPlayerConnection.SessionAcquisition#onSet
    private final NotNullObjectAcquirable<Session> sessionAcquirable;

    /**
     * Constructs the {@linkplain ClientPacketReader client packet reader}.
     *
     * @param connection a connection that the packets should be decoded for
     * @param initialSession a session that the packet reader should initially use
     * @since 1.0
     */
    public ClientPacketReader(@NonNull SocketPlayerConnection connection, @NonNull Session initialSession) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        NullabilityUtil.requireNonNull(initialSession, "initial session");
        this.sessionAcquirable = new NotNullObjectAcquirable<>(initialSession);

        Thread.ofVirtual()
                .name("Client packet reader thread")
                .uncaughtExceptionHandler(connection)
                .start(this::readPackets)
                .interrupt();
    }

    /**
     * Queues {@linkplain RawPacket a raw packet} to be decoded.
     *
     * @param packet the raw packet
     * @since 1.0
     */
    public void queue(@NonNull RawPacket packet) {
        this.packetQueue.add(NullabilityUtil.requireNonNull(packet, "packet"));
    }

    /**
     * Blocks a thread responsible for decoding and reading packets until {@link #resumePacketReading()} is called.
     *
     * @since 1.0
     */
    public void pausePacketReading() {
        try (WriteBooleanAcquisition acquisition = this.paused.acquireWrite()) {
            acquisition.set(true);
        }
    }
    /**
     * Unblocks a thread responsible for decoding and reading packets if it was blocked via
     * {@link #pausePacketReading()}.
     *
     * @since 1.0
     */
    public void resumePacketReading() {
        try (WriteBooleanAcquisition acquisition = this.paused.acquireWrite()) {
            acquisition.set(false);
            this.resumeCondition.signalAll();
        }
    }

    /**
     * Updates {@linkplain Session a session} used for decoding and reading packets by this
     * {@linkplain ClientPacketReader client packet reader}.
     *
     * @param newSession the session
     * @since 1.0
     */
    public void updateSession(@NotNull Session newSession) {
        try (WriteNotNullObjectAcquisition<Session> acquisition = this.sessionAcquirable.acquireWrite()) {
            acquisition.set(newSession);
        }
    }

    @Override
    public void handleDisconnection() {
        // We need to resume the packet reading to avoid a situation where the thread is never unblocked
        this.resumePacketReading();
    }

    private void readPackets() {
        while (this.connection.isActive()) {
            try (WriteBooleanAcquisition acquisition = this.paused.acquireWrite()) {
                if (acquisition.get()) {
                    this.resumeCondition.awaitUninterruptibly();
                    continue; // We need to recheck whether the connection is active
                }
                this.readNextPacket();
            }
        }
    }

    private void readNextPacket() {
        RawPacket rawPacket = this.packetQueue.poll();
        if (rawPacket == null) return;

        try (NotNullObjectAcquisition<Session> acquisition = this.sessionAcquirable.acquireRead()) {
            Session session = acquisition.get();
            handlePacket(decodeBody(rawPacket, session.protocolState()), session);
        }
    }

    private static @NonNull ClientPacket decodeBody(@NonNull RawPacket rawPacket, @NonNull ProtocolState state) {
        int identifier = rawPacket.identifier();
        NetworkReader<? extends ClientPacket> reader = ClientPacketRegistry.readerFor(identifier, state);

        if (reader == null) {
            throw new IllegalStateException(String.format(
                    "Could not find a reader of a packet with id of %s in protocol state %s",
                    identifier, state
            ));
        }

        ByteBuf buf = Unpooled.copiedBuffer(rawPacket.body().array());
        try {
            ClientPacket packet = reader.read(buf);

            int readableBytes = buf.readableBytes();
            if (readableBytes > 0) {
                throw new IllegalStateException(String.format(
                        "Packet with identifier of %s has been not fully read. (%s > 0)",
                        identifier, readableBytes
                ));
            }

            return packet;
        } finally {
            buf.release();
        }
    }

    private static void handlePacket(@NonNull ClientPacket packet, @NonNull Session session) {
        Class<? extends ClientPacket> packetClass = packet.getClass();
        ClientPacketHandler<?> handler = ClientPacketRegistry.handlerFor(packetClass);

        if (handler == null) {
            LOGGER.warn("No packet handler was specified for a client packet with class of {}",
                    packetClass.getSimpleName());
            return;
        }

        handlePacket(packet, session, handler); // Handle the packet with generics
    }

    private static <P extends ClientPacket> void handlePacket(@NonNull ClientPacket packet, @NonNull Session session,
                                                              @NonNull ClientPacketHandler<P> handler) {
        handler.handle(handler.packetClass().cast(packet), session);
    }
}