package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@linkplain MessageToByteEncoder message-to-byte encoder}, which encodes {@linkplain ServerPacket
 * server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketEncoder extends MessageToByteEncoder<ServerPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) throws Exception {
        try {
            ServerPacketRegistry.write(out, msg);
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while encoding a packet", throwable);
            ctx.channel().close().sync();
        }
    }
}