package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents {@linkplain ByteToMessageDecoder a byte-to-message decoder}, which decodes lengths of
 * {@linkplain ServerPacket server packets} and creates a frame
 * with a length of them.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketLengthDecoder extends ByteToMessageDecoder {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketLengthDecoder packet length decoder}.
     *
     * @param connection a connection that the packet length decoding should be handled for
     * @since 1.0
     */
    public PacketLengthDecoder(@NonNull SocketPlayerConnection connection) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) return; // The connection was closed

        in.markReaderIndex();
        int packetLength = VarIntNetworkCodec.INSTANCE.read(in);

        if (packetLength > in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }

        out.add(in.retainedSlice(in.readerIndex(), packetLength));
        in.skipBytes(packetLength);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }
}