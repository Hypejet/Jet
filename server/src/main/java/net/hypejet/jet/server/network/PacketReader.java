package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.player.login.LoginHandler;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestPacket;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.player.login.DefaultLoginHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@linkplain ChannelInboundHandlerAdapter channel inbound handler adapter}, which processes
 * Minecraft {@linkplain  ClientPacket client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public final class PacketReader extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketReader.class);

    private final SocketPlayerConnection playerConnection;
    private final LoginHandler handler;

    public PacketReader(@NonNull SocketPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
        this.handler = new DefaultLoginHandler(); // TODO: Built-in Mojang handler support and an event
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        if (!(msg instanceof ClientPacket packet))
            throw new IllegalStateException("A message received is not a client packet");

        if (packet instanceof ClientHandshakePacket handshakePacket) {
            this.playerConnection.setProtocolState(handshakePacket.nextState());
            return;
        }

        if (packet instanceof ClientLoginPacket loginPacket) {
            switch (loginPacket) {
                case ClientLoginAcknowledgePacket ignored ->
                        this.playerConnection.setProtocolState(ProtocolState.CONFIGURATION);
                case ClientLoginRequestPacket ignored -> this.playerConnection.setCompressionThreshold(256);
                default -> {}
            }

            this.handler.onPacket(loginPacket, this.playerConnection);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.close(); // Close the connection to avoid more issues
        LOGGER.error("An error occurred while reading a packet", cause);
    }
}