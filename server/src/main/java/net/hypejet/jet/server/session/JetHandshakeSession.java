package net.hypejet.jet.server.session;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.events.login.LoginSessionInitializeEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import net.hypejet.jet.session.handler.SessionHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Represents a {@linkplain Session session} and a {@linkplain SessionHandler session handler}, which handles the
 * {@linkplain ProtocolState#HANDSHAKE handshake protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ProtocolState#HANDSHAKE
 * @see Session
 * @see SessionHandler
 */
public final class JetHandshakeSession implements Session<JetHandshakeSession>, SessionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetHandshakeSession.class);

    private static final long TIME_OUT = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private final SocketPlayerConnection connection;
    private final CountDownLatch handshakeLatch = new CountDownLatch(1);

    /**
     * Constructs the {@linkplain JetHandshakeSession handshake session}.
     *
     * @param connection a connection with a player that this session is handled for
     * @since 1.0
     */
    public JetHandshakeSession(@NonNull SocketPlayerConnection connection) {
        this.connection = Objects.requireNonNull(connection, "The connection must not be null");
        Thread.ofVirtual()
                .name("Handshake session")
                .uncaughtExceptionHandler((thread, throwable) -> this.handleThrowable(throwable))
                .start(() -> {
                    try {
                        if (!this.handshakeLatch.await(TIME_OUT, TIME_OUT_UNIT)) {
                            this.connection.disconnect(Session.TIME_OUT_DISCONNECTION_MESSAGE);
                        }
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @Override
    public @NonNull JetHandshakeSession sessionHandler() {
        return this;
    }

    @Override
    public void onConnectionClose(@Nullable Throwable cause) {
        if (this.handshakeLatch.getCount() > 0) {
            this.handshakeLatch.countDown();
        }
    }

    /**
     * Handles a {@linkplain ClientHandshakePacket handshake packet}.
     *
     * @param packet the handshake packet
     * @since 1.0
     */
    public void handleHandshakePacket(@NonNull ClientHandshakePacket packet) {
        try {
            if (this.handshakeLatch.getCount() == 0) {
                throw new IllegalArgumentException("The handshake packet was already received");
            }

            this.handshakeLatch.countDown();

            switch (packet.intent()) {
                case STATUS -> {
                    this.connection.setProtocolState(ProtocolState.STATUS);
                    this.connection.setSession(new JetStatusSession(this.connection));
                }
                case LOGIN -> {
                    this.connection.setProtocolState(ProtocolState.LOGIN);

                    MinecraftServer server = this.connection.server();
                    EventNode<Object> eventNode = server.eventNode();

                    if (packet.protocolVersion() != server.protocolVersion()) {
                        this.connection.disconnect(server.configuration().unsupportedVersionMessage());
                        return;
                    }

                    LoginSessionInitializeEvent sessionEvent = new LoginSessionInitializeEvent(this.connection);
                    eventNode.call(sessionEvent);

                    LoginSessionHandler loginSessionHandler = sessionEvent.getSessionHandler();
                    if (loginSessionHandler == null) {
                        throw new IllegalStateException("The login session handler was not set");
                    }

                    this.connection.setSession(new JetLoginSession(this.connection, loginSessionHandler));
                }
                case TRANSFER -> throw new UnsupportedOperationException("Transfers are not supported yet");
            }
        } catch (Throwable throwable) {
            this.handleThrowable(throwable);
        }
    }

    /**
     * Handles an error that occurred during this session.
     *
     * @param throwable the error
     * @since 1.0
     */
    private void handleThrowable(@NonNull Throwable throwable) {
        LOGGER.error("An error occurred during the handshake session", throwable);
        this.connection.close();
    }
}