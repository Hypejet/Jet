package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.server.acquisition.value.AcquirableValue;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.client.ClientPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents {@linkplain ByteToMessageDecoder a byte-to-message decoder}, which decodes
 * {@linkplain net.hypejet.jet.network.packet.server.ServerPacket server packets} and their identifiers.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.network.packet.server.ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private final SocketPlayerConnection connection;
    // More about existence of this field in a comment in SocketPlayerConnection.SessionAcquisition#onSet
    private final AcquirableValue<ProtocolState> protocolState;

    /**
     * Constructs the {@linkplain PacketDecoder packet decoder}.
     *
     * @param connection a connection that the decoding should be handled for
     * @param initialProtocolState an initial protocol state that the decoder should use
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection connection, @NonNull ProtocolState initialProtocolState) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        NullabilityUtil.requireNonNull(initialProtocolState, "initial protocol state");
        this.protocolState = new AcquirableValue<>(initialProtocolState);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try (Acquisition<ProtocolState> protocolStateAcquisition = this.protocolState.acquire()) {
            ProtocolState protocolState = protocolStateAcquisition.get();
            int packetId = VarIntNetworkCodec.INSTANCE.read(in);

            NetworkReader<? extends ClientPacket> reader = ClientPacketRegistry.readerFor(packetId, protocolState);
            if (reader == null) throw throwPacketHandlerNotFound(packetId, protocolState);
            ClientPacket packet = reader.read(in);

            int readableBytes = in.readableBytes();
            if (readableBytes > 0) {
                throw new IllegalStateException(String.format(
                        "Packet with identifier of %s has been not fully read. (%s > 0)",
                        packetId, readableBytes
                ));
            }

            out.add(packet);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }

    private static @NonNull RuntimeException throwPacketHandlerNotFound(int packetId,
                                                                        @NonNull ProtocolState protocolState) {
        return new IllegalStateException(String.format(
                "Could not find a reader of a packet with id of \"%s\" in protocol state \"%s\"",
                packetId, protocolState
        ));
    }

    /**
     * Updates {@linkplain ProtocolState a protocol state} that is used by this packet decoder.
     *
     * @param protocolState the protocol state
     * @since 1.0
     */
    public void updateProtocolState(@NonNull ProtocolState protocolState) {
        try (MutableAcquisition<ProtocolState> protocolStateAcquisition = this.protocolState.acquireMutable()) {
            protocolStateAcquisition.set(protocolState);
        }
    }
}