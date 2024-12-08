package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.util.CompressionUtil;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents {@linkplain ByteToMessageDecoder a byte-to-message decoder}, which decompresses
 * {@linkplain ServerPacket server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketDecompressor extends ByteToMessageDecoder {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketDecompressor packet decompressor}.
     *
     * @param connection a connection that the decompression should be handled for
     * @since 1.0
     */
    public PacketDecompressor(@NonNull SocketPlayerConnection connection) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) return; // The connection was closed
        int dataLength = VarIntNetworkCodec.INSTANCE.read(in);

        if (dataLength == 0) {
            out.add(in.retainedSlice());
            in.skipBytes(in.readableBytes());
            return;
        }

        byte[] compressed = NetworkUtil.readRemainingBytes(in);
        ByteBuf uncompressed = Unpooled.wrappedBuffer(CompressionUtil.decompress(compressed));
        out.add(uncompressed);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }
}