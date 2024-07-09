package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.server.util.CompressionUtil;
import net.hypejet.jet.server.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a {@linkplain ByteToMessageDecoder byte-to-message decoder}, which decompresses
 * {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketDecompressor extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDecompressor.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) return; // The connection was closed
        int dataLength = NetworkUtil.readVarInt(in);

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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("An error occurred while decompressing a packet", cause);
        ctx.channel().close().sync();
    }
}