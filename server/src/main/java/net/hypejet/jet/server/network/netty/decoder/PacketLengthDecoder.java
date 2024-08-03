package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a {@linkplain ByteToMessageDecoder byte-to-message decoder}, which decodes lengths of
 * {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packets} and creates a frame
 * with a length of them.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketLengthDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketLengthDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) return; // The connection was closed

        in.markReaderIndex();
        int packetLength = VarIntNetworkCodec.instance().read(in);

        if (packetLength > in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }

        out.add(in.retainedSlice(in.readerIndex(), packetLength));
        in.skipBytes(packetLength);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("An error occurred while decoding a length of a packet", cause);
        ctx.channel().close().sync();
    }
}