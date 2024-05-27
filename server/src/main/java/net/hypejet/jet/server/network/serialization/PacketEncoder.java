package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;

public final class PacketEncoder extends MessageToByteEncoder<ClientBoundPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientBoundPacket msg, ByteBuf out) {
        NetworkBuffer buffer = new NetworkBufferImpl(out);
        buffer.writeVarInt(msg.getPacketId());
        msg.write(buffer);
    }
}