package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.clientbound.login.success.ServerLoginSuccessPacket;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import net.hypejet.jet.protocol.packet.serverbound.login.ClientLoginAcknowledgePacket;
import net.hypejet.jet.protocol.packet.serverbound.login.LoginRequestPacket;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@link ChannelInboundHandlerAdapter channel inbound handler adapter}, which processes
 * Minecraft {@link ServerBoundPacket server-bound packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerBoundPacket
 */
public final class PacketReader extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketReader.class);

    private final SocketPlayerConnection playerConnection;

    public PacketReader(@NonNull SocketPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        if (!(msg instanceof ServerBoundPacket packet))
            throw new IllegalStateException("A message received is not a server-bound packet");

        switch (packet) {
            case HandshakePacket handshakePacket -> this.playerConnection.setProtocolState(handshakePacket.nextState());
            case LoginRequestPacket requestPacket -> this.playerConnection.sendPacket(ServerLoginSuccessPacket.builder()
                    .uniqueId(requestPacket.uniqueId())
                    .username(requestPacket.username())
                    .build());
            case ClientLoginAcknowledgePacket ignored -> this.playerConnection.setProtocolState(ProtocolState.CONFIGURATION);
            default -> {}
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.close(); // Close the connection to avoid more issues
        LOGGER.error("An error occurred while reading a packet", cause);
    }
}