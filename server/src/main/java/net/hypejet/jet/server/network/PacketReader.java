package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import net.hypejet.jet.server.player.JetPlayerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PacketReader extends ChannelInboundHandlerAdapter {

    private final JetPlayerConnection playerConnection;

    public PacketReader(@NonNull JetPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        if (!(msg instanceof ServerBoundPacket packet))
            throw new IllegalStateException("A message received is not a server-bound packet");

        if (packet instanceof HandshakePacket handshakePacket) {
            this.playerConnection.setProtocolState(handshakePacket.nextState());
            this.playerConnection.kick(
                    Component.text("Disconnection", NamedTextColor.DARK_RED)
                            .appendNewline()
                            .append(Component.text(
                                    "As Jet is still WIP, you got disconnected",
                                    NamedTextColor.LIGHT_PURPLE)
                            )
            );
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        this.playerConnection.close(); // Close the connection to avoid more issues
    }
}