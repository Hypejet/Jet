package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;
import net.hypejet.jet.server.util.NetworkUtil;

/**
 * Represents a {@link MessageToByteEncoder message-to-byte encoder}, which encodes Minecraft packets.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientBoundPacket
 */
public final class PacketEncoder extends MessageToByteEncoder<ClientBoundPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientBoundPacket msg, ByteBuf out) {
        ByteBuf buf = Unpooled.buffer();

        NetworkBuffer buffer = new NetworkBufferImpl(buf);
        buffer.writeVarInt(msg.getPacketId());
        msg.write(buffer);

        NetworkUtil.writeVarLong(out, buf.readableBytes());
        out.writeBytes(buf);
    }
}