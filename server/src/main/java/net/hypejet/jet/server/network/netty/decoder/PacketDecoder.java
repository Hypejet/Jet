package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
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

    /**
     * Constructs the {@linkplain PacketDecoder packet decoder}.
     *
     * @param connection a connection that the decoding should be handled for
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        this.connection.consumeSession(session -> {
            if (!ctx.channel().isActive()) return; // The connection has been closed

            ProtocolState protocolState = session.protocolState();
            int packetId = VarIntNetworkCodec.instance().read(in);

            ClientPacketCodec<?> codec = ClientPacketRegistry.codec(packetId, protocolState);
            if (codec == null) throw packetReaderNotFound(packetId, protocolState);

            out.add(codec.read(in));

            int readableBytes = in.readableBytes();
            if (readableBytes > 0) {
                throw new IllegalStateException(String.format(
                        "Packet with identifier of %s has been not fully read. (%s > 0)",
                        packetId, readableBytes
                ));
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }

    private static @NonNull RuntimeException packetReaderNotFound(int packetId, @NonNull ProtocolState protocolState) {
        return new IllegalStateException("Could not find a reader of a packet with id of "
                + packetId + " in protocol state " + protocolState);
    }
}