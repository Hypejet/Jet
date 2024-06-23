package net.hypejet.jet.server.session;

import net.hypejet.jet.event.events.ping.ServerListPingEvent;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerPingResponseStatusPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.session.handler.SessionHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Represents a {@linkplain Session session} and a {@linkplain SessionHandler session handler}, which handles
 * the {@linkplain net.hypejet.jet.protocol.ProtocolState#STATUS status protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.ProtocolState#STATUS
 * @see Session
 * @see SessionHandler
 */
public final class JetStatusSession implements Session<JetStatusSession>, SessionHandler {

    private static final long TIME_OUT = 30;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private static final Logger LOGGER = LoggerFactory.getLogger(JetStatusSession.class);

    private final SocketPlayerConnection connection;

    private final CountDownLatch listRequestLatch = new CountDownLatch(1);
    private final CountDownLatch pingRequestLatch = new CountDownLatch(1);

    /**
     * Constructs the {@linkplain JetStatusSession status session}.
     *
     * @param connection a connection with a player that the session should be handled for
     * @since 1.0
     */
    public JetStatusSession(@NonNull SocketPlayerConnection connection) {
        this.connection = connection;

        Thread.ofVirtual()
                .uncaughtExceptionHandler((thread, throwable) -> this.handleThrowable(throwable))
                .start(() -> {
                    try {
                        if (!this.listRequestLatch.await(TIME_OUT, TIME_OUT_UNIT)
                                || !this.pingRequestLatch.await(TIME_OUT, TIME_OUT_UNIT)) {
                            this.connection.disconnect(Session.TIME_OUT_DISCONNECTION_MESSAGE);
                        }
                        // The status state has been finished, does not matter if successfully or not
                        this.connection.close();
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @Override
    public @NonNull JetStatusSession sessionHandler() {
        return this;
    }

    @Override
    public void onConnectionClose(@Nullable Throwable cause) {
        if (this.listRequestLatch.getCount() > 0) this.listRequestLatch.countDown();
        if (this.pingRequestLatch.getCount() > 0) this.pingRequestLatch.countDown();
    }

    /**
     * Handles a ping request from a client.
     *
     * @param packet the ping request
     * @since 1.0
     */
    public void handlePingRequest(@NonNull ClientPingRequestStatusPacket packet) {
        if (this.listRequestLatch.getCount() > 0) {
            this.listRequestLatch.countDown(); // The client may skip getting server list, it is a natural behaviour
        }

        this.pingRequestLatch.countDown();
        this.connection.sendPacket(new ServerPingResponseStatusPacket(packet.payload()));
    }

    /**
     * Handles a server list ping request from a client.
     *
     * @since 1.0
     */
    public void handleStatusRequest() {
        if (this.listRequestLatch.getCount() == 0)
            throw new IllegalArgumentException("The server list packet has been already sent");

        this.listRequestLatch.countDown();

        JetMinecraftServer server = this.connection.server();
        JetServerConfiguration configuration = server.configuration();

        ServerListPing ping = new ServerListPing(
                new ServerListPing.Version(server.minecraftVersion(), server.protocolVersion()),
                // TODO: An actual list of players online
                new ServerListPing.Players(configuration.maxPlayers(), 0, List.of()),
                configuration.serverListDescription(),
                server.serverIcon(),
                false, // TODO: An actual property
                false, // TODO: An actual property
                null
        );

        ServerListPingEvent pingEvent = new ServerListPingEvent(this.connection, ping);
        server.eventNode().call(pingEvent);
        ping = pingEvent.getPing();

        this.connection.sendPacket(new ServerListResponseStatusPacket(ping));
    }

    /**
     * Handles an error, which occurred during this session.
     *
     * @param throwable the error
     * @since 1.0
     */
    private void handleThrowable(@NonNull Throwable throwable) {
        LOGGER.error("An error occurred during the status session", throwable);
        this.connection.close();
    }

    /**
     * Casts a {@linkplain Session session} to the {@linkplain JetStatusSession status session} or throws an exception.
     *
     * @param session the session to cast
     * @return the cast session
     * @throws IllegalStateException if session is not a status session
     * @since 1.0
     */
    public static @NonNull JetStatusSession asLoginSession(@Nullable Session<?> session) {
        if (session instanceof JetStatusSession statusSession) return statusSession;
        throw new IllegalStateException("The session is not a status session");
    }
}