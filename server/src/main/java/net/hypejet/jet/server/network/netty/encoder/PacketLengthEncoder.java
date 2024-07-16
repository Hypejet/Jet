package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@linkplain MessageToByteEncoder message-to-byte encoder}, which writes a length of a serialized
 * and compressed {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketLengthEncoder extends MessageToByteEncoder<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketLengthEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        try {
            VarIntNetworkCodec.instance().write(out, msg.readableBytes());
            out.writeBytes(msg);
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while encoding a length of a packet", throwable);
            ctx.channel().close().sync();
        }
    }
}