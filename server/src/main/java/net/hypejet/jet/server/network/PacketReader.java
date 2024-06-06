package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.player.login.LoginHandler;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.event.events.packet.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.player.login.DefaultLoginHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

    private final JetMinecraftServer server;

    /**
     * Constructs a {@linkplain PacketReader packet reader}.
     *
     * @param playerConnection a player connection to read packets for
     * @param server a server that provides the {@code connection}
     * @since 1.0
     */
    public PacketReader(@NonNull SocketPlayerConnection playerConnection, @NonNull JetMinecraftServer server) {
        this.playerConnection = playerConnection;
        this.handler = new DefaultLoginHandler(); // TODO: Built-in Mojang handler support and an event
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        if (!(msg instanceof ClientPacket packet))
            throw new IllegalStateException("A message received is not a client packet");

        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        this.server.eventNode().call(event);

        if (event.isCancelled()) return;

        if (packet instanceof ClientHandshakePacket handshakePacket) {
            ProtocolState nextState = handshakePacket.nextState();
            this.playerConnection.setProtocolState(nextState);

            if (nextState == ProtocolState.LOGIN && handshakePacket.protocolVersion() != this.server.protocolVersion()) {
                this.playerConnection.kick(Component.text("Unsupported protocol version", NamedTextColor.DARK_RED));
                return;
            }

            return;
        }

        if (packet instanceof ClientLoginPacket loginPacket) {
            switch (loginPacket) {
                case ClientLoginAcknowledgeLoginPacket ignored ->
                        this.playerConnection.setProtocolState(ProtocolState.CONFIGURATION);
                case ClientLoginRequestLoginPacket ignored -> this.playerConnection.setCompressionThreshold(256);
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