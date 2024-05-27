package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class InboundManager extends ChannelInboundHandlerAdapter {

    private final JetPlayerConnection playerConnection;

    public InboundManager(@NonNull JetPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof ServerBoundPacket packet))
            throw new IllegalStateException("A message received is not a server-bound packet");

        if (packet instanceof HandshakePacket handshakePacket) {
            this.playerConnection.setProtocolState(handshakePacket.nextState());
        }

        System.out.println("Packet received: " + packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}