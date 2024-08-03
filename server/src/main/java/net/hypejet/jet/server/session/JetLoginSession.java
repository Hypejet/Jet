package net.hypejet.jet.server.session;

import net.hypejet.jet.event.events.player.login.PlayerLoginEvent;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.session.LoginSession;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a server implementation of {@linkplain LoginSession login session}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginSession
 */
public final class JetLoginSession implements LoginSession, Session<LoginSessionHandler> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetLoginSession.class);

    private final SocketPlayerConnection connection;

    private final CountDownLatch handlerLatch = new CountDownLatch(1);
    private final CountDownLatch acknowledgeLatch = new CountDownLatch(1);

    private final LoginSessionHandler sessionHandler;

    private final ReentrantLock finishLock = new ReentrantLock();

    private @MonotonicNonNull String username;
    private @MonotonicNonNull UUID uniqueId;

    private Collection<ServerLoginSuccessLoginPacket.Property> properties = List.of();

    /**
     * Constructs the {@linkplain JetLoginSession login session}.
     *
     * @param connection a connection with a player
     * @param sessionHandler a {@linkplain LoginSessionHandler login session handler} that handles this session
     * @since 1.0
     */
    public JetLoginSession(@NonNull SocketPlayerConnection connection, @NonNull LoginSessionHandler sessionHandler) {
        this.connection = connection;
        this.sessionHandler = sessionHandler;

        Thread.ofVirtual()
                .name("Login session")
                .uncaughtExceptionHandler((thread, throwable) -> this.handleThrowable(throwable))
                .start(() -> {
                    try {
                        if (!this.handlerLatch.await(Session.TIME_OUT_DURATION, Session.TIME_OUT_UNIT)) {
                            this.sessionHandler.onTimeOut(this, LoginSessionHandler.TimeOutType.HANDLER);
                            throw new RuntimeException(new TimeoutException("The login handler has timed out"));
                        }

                        String username = this.username;
                        UUID uniqueId = this.uniqueId;

                        Collection<ServerLoginSuccessLoginPacket.Property> properties = this.properties;

                        JetPlayer player = new JetPlayer(uniqueId, username, this.connection);
                        this.connection.initializePlayer(player);

                        JetMinecraftServer server = this.connection.server();

                        if (this.connection.isClosed()) return;

                        PlayerLoginEvent loginEvent = new PlayerLoginEvent(player);
                        server.eventNode().call(loginEvent);

                        if (loginEvent.getResult() instanceof PlayerLoginEvent.Result.Fail failResult) {
                            this.connection.disconnect(failResult.disconnectReason());
                            return;
                        }

                        this.connection.sendPacket(new ServerLoginSuccessLoginPacket(
                                uniqueId, username, properties, true
                        ));

                        if (!this.acknowledgeLatch.await(Session.TIME_OUT_DURATION, Session.TIME_OUT_UNIT)) {
                            this.sessionHandler.onTimeOut(this, LoginSessionHandler.TimeOutType.ACKNOWLEDGE);
                            throw new TimeoutException("The login was not acknowledged on time");
                        }
                    } catch (InterruptedException | TimeoutException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void finish(@NonNull String username, @NonNull UUID uniqueId,
                       @NonNull Collection<ServerLoginSuccessLoginPacket.Property> properties) {
        Objects.requireNonNull(username, "The username must not be null");
        Objects.requireNonNull(uniqueId, "The unique identifier must not be null");
        Objects.requireNonNull(properties, "The properties must not be null");

        this.finishLock.lock();

        try {
            if (this.handlerLatch.getCount() == 0)
                throw new IllegalArgumentException("The login session has been already finished");

            this.username = username;
            this.uniqueId = uniqueId;
            this.properties = List.copyOf(properties);

            this.handlerLatch.countDown();
        } finally {
            this.finishLock.unlock();
        }
    }

    @Override
    public @NonNull LoginSessionHandler sessionHandler() {
        return this.sessionHandler;
    }

    @Override
    public void onConnectionClose(@Nullable Throwable throwable) {
        if (this.handlerLatch.getCount() > 0) {
            this.handlerLatch.countDown();
        }

        if (this.acknowledgeLatch.getCount() > 0) {
            this.acknowledgeLatch.countDown();
        }

        this.sessionHandler.onConnectionClose(throwable);
    }

    /**
     * Releases all threads blocked by {@link #acknowledgeLatch}.
     *
     * @since 1.0
     * @see #acknowledgeLatch
     */
    public void releaseAcknowledgeLatch() {
        this.acknowledgeLatch.countDown();
    }

    /**
     * Handles an error that occurred during this session.
     *
     * @param throwable the error
     * @since 1.0
     */
    private void handleThrowable(@NonNull Throwable throwable) {
        Objects.requireNonNull(throwable, "The throwable cannot be null");
        LOGGER.error("An error occurred while logging in a player", throwable);
        this.connection.disconnect(Component.text("An error occurred while logging you in", NamedTextColor.RED));
    }

    /**
     * Casts a {@linkplain Session session} to the {@linkplain JetLoginSession login session} or throws an exception.
     *
     * @param session the session to cast
     * @return the cast session
     * @throws IllegalStateException if session is not a login session
     * @since 1.0
     */
    public static @NonNull JetLoginSession asLoginSession(@Nullable Session<?> session) {
        if (session instanceof JetLoginSession loginSession) return loginSession;
        throw new IllegalStateException("The session is not a login session");
    }
}