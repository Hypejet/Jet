package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.netty.reader.UnhandledPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents a {@linkplain ByteToMessageDecoder byte-to-message decoder}, which decodes
 * {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packets} and their identifiers.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private final SocketPlayerConnection connection;
    private final ClientPacketRegistry packetRegistry;

    /**
     * Constructs the {@linkplain PacketDecoder packet decoder}.
     *
     * @param connection a connection that the decoding should be handled for
     * @param packetRegistry a client packet registry that should be used for finding client packet handlers
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection connection, @NonNull ClientPacketRegistry packetRegistry) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        this.packetRegistry = NullabilityUtil.requireNonNull(packetRegistry, "packet registry");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try (Acquisition<ProtocolState> protocolStateAcquisition = this.connection.protocolState()) {
            ProtocolState protocolState = protocolStateAcquisition.get();
            int packetId = VarIntNetworkCodec.INSTANCE.read(in);

            ClientPacketHandler<?> handler = this.packetRegistry.handlerFor(packetId, protocolState);
            if (handler == null) throw throwPacketHandlerNotFound(packetId, protocolState);
            UnhandledPacket<?> unhandledPacket = readPacket(in, handler); // Read the packet with generics

            int readableBytes = in.readableBytes();
            if (readableBytes > 0) {
                throw new IllegalStateException(String.format(
                        "Packet with identifier of %s has been not fully read. (%s > 0)",
                        packetId, readableBytes
                ));
            }

            out.add(unhandledPacket);
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

    private static <P extends ClientPacket> @NonNull UnhandledPacket<P> readPacket(
            @NonNull ByteBuf buf,  @NonNull ClientPacketHandler<P> handler
    ) {
        return new UnhandledPacket<>(handler.read(buf), handler);
    }
}