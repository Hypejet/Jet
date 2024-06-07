package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.event.events.ping.ServerListPingEvent;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.player.login.LoginHandler;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.event.events.packet.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.ClientStatusPacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerPingResponseStatusPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.player.login.DefaultLoginHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
                this.playerConnection.kick(this.server.configuration().unsupportedVersionMessage());
                return;
            }

            return;
        }

        if (packet instanceof ClientStatusPacket statusPacket) {
            switch (statusPacket) {
                case ClientPingRequestStatusPacket (long payload) ->
                    this.playerConnection.sendPacket(new ServerPingResponseStatusPacket(payload));
                case ClientServerListRequestStatusPacket () -> {
                    JetServerConfiguration configuration = this.server.configuration();

                    ServerListPing ping = new ServerListPing(
                            new ServerListPing.Version(this.server.minecraftVersion(), this.server.protocolVersion()),
                            // TODO: An actual list of players online
                            new ServerListPing.Players(configuration.maxPlayers(), 0, List.of()),
                            configuration.serverListDescription(),
                            this.server.serverIcon(),
                            false, // TODO: An actual property
                            false, // TODO: An actual property
                            null
                    );

                    ServerListPingEvent pingEvent = new ServerListPingEvent(this.playerConnection, ping);
                    this.server.eventNode().call(pingEvent);
                    ping = pingEvent.getPing();

                    this.playerConnection.sendPacket(new ServerListResponseStatusPacket(ping));
                }
            }
        }

        if (packet instanceof ClientLoginPacket loginPacket) {
            switch (loginPacket) {
                case ClientLoginAcknowledgeLoginPacket ignored ->
                        this.playerConnection.setProtocolState(ProtocolState.CONFIGURATION);
                case ClientLoginRequestLoginPacket ignored -> this.playerConnection.setCompressionThreshold(
                        this.server.configuration().compressionThreshold()
                );
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