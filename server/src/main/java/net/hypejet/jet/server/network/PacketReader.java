package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.login.LoginSessionInitializeEvent;
import net.hypejet.jet.event.events.login.PlayerLoginEvent;
import net.hypejet.jet.event.events.packet.PacketReceiveEvent;
import net.hypejet.jet.event.events.ping.ServerListPingEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.client.ClientStatusPacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerPingResponseStatusPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.session.JetLoginSession;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

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
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        if (!(msg instanceof ClientPacket packet))
            throw new IllegalStateException("A message received is not a client packet");

        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        EventNode<Object> eventNode = this.server.eventNode();

        eventNode.call(event);
        if (event.isCancelled()) return;

        if (packet instanceof ClientHandshakePacket handshakePacket) {
            ClientHandshakePacket.HandshakeIntent intent = handshakePacket.intent();

            ProtocolState nextState = switch (intent) {
                case STATUS -> ProtocolState.STATUS;
                case LOGIN -> ProtocolState.LOGIN;
                case TRANSFER -> throw new UnsupportedOperationException("Transfers are not supported yet");
            };

            this.playerConnection.setProtocolState(nextState);

            if (intent == ClientHandshakePacket.HandshakeIntent.LOGIN) {
                try {
                    if (handshakePacket.protocolVersion() != this.server.protocolVersion()) {
                        this.playerConnection.disconnect(this.server.configuration().unsupportedVersionMessage());
                        return;
                    }

                    LoginSessionInitializeEvent sessionEvent = new LoginSessionInitializeEvent(this.playerConnection);
                    eventNode.call(sessionEvent);

                    LoginSessionHandler loginSessionHandler = sessionEvent.getSessionHandler();
                    if (loginSessionHandler == null) {
                        throw new IllegalStateException("The login session handler was not set");
                    }

                    JetLoginSession session = new JetLoginSession(this.playerConnection, loginSessionHandler);
                    this.playerConnection.setSession(session);

                    Thread.ofPlatform()
                            .uncaughtExceptionHandler((thread, exception) ->
                                    handleLoginError(exception, this.playerConnection))
                            .start(() -> {
                                if (!session.awaitHandlerCompletion()) {
                                    loginSessionHandler.onTimeOut(session, LoginSessionHandler.TimeOutType.HANDLER);
                                    throw new RuntimeException(new TimeoutException("The login handler has timed out"));
                                }

                                String username = session.username();
                                if (username == null) {
                                    throw new IllegalArgumentException("The username was not set");
                                }

                                UUID uniqueId = session.uniqueId();
                                if (uniqueId == null) {
                                    throw new IllegalArgumentException("The unique identifier was not set");
                                }

                                Player player = new JetPlayer(uniqueId, username, this.playerConnection);
                                this.playerConnection.initializePlayer(player);

                                PlayerLoginEvent loginEvent = new PlayerLoginEvent(player);
                                eventNode.call(loginEvent);

                                if (loginEvent.getResult() instanceof PlayerLoginEvent.Result.Fail failResult) {
                                    this.playerConnection.disconnect(failResult.disconnectReason());
                                    return;
                                }

                                this.playerConnection.sendPacket(new ServerLoginSuccessLoginPacket(uniqueId, username,
                                        session.gameProfiles(), true));

                                if (!session.awaitAcknowledge()) {
                                    loginSessionHandler.onTimeOut(session, LoginSessionHandler.TimeOutType.ACKNOWLEDGE);
                                    throw new RuntimeException(new TimeoutException("The login was not acknowledged " +
                                            "on time"));
                                }
                            });

                    return;
                } catch (Throwable throwable) {
                    handleLoginError(throwable, this.playerConnection);
                }
            }
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
            if (!(this.playerConnection.getSession() instanceof JetLoginSession session)) {
                throw new IllegalStateException("A login packet was received while the current session is not a" +
                        " valid login session");
            }

            LoginSessionHandler sessionHandler = session.sessionHandler();
            sessionHandler.onGenericPacket(loginPacket, session);

            switch (loginPacket) {
                case ClientCookieResponseLoginPacket cookieResponse ->
                        sessionHandler.onCookieResponse(cookieResponse, session);
                case ClientEncryptionResponseLoginPacket encryptionResponse ->
                        sessionHandler.onEncryptionResponse(encryptionResponse, session);
                case ClientPluginMessageResponseLoginPacket pluginMessage ->
                        sessionHandler.onPluginMessage(pluginMessage, session);
                case ClientLoginAcknowledgeLoginPacket acknowledgePacket -> {
                    session.releaseAcknowledgeLatch();
                    sessionHandler.onLoginAcknowledge(acknowledgePacket, session);
                    this.playerConnection.setProtocolState(ProtocolState.CONFIGURATION);
                    this.playerConnection.setSession(null); // The login session has ended
                }
                case ClientLoginRequestLoginPacket loginRequest -> {
                    this.playerConnection.setCompressionThreshold(this.server.configuration().compressionThreshold());
                    sessionHandler.onLoginRequest(loginRequest, session);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.close(); // Close the connection to avoid more issues
        LOGGER.error("An error occurred while reading a packet", cause);
    }

    private static void handleLoginError(@NonNull Throwable throwable, @NonNull SocketPlayerConnection connection) {
        LOGGER.error("An error occurred while logging a player", throwable);
        connection.disconnect(Component.text()
                .append(Component.text("A server error occurred while logging you in:", NamedTextColor.RED))
                .appendNewline()
                .append(Component.text(throwable.toString()))
                .build());
    }
}