package net.hypejet.jet.server.session;

import net.hypejet.jet.event.events.login.PlayerLoginEvent;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.protocol.profile.GameProfile;
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
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a server implementation of {@linkplain LoginSession login session}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginSession
 */
public final class JetLoginSession implements LoginSession, Session<LoginSessionHandler> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetLoginSession.class);

    private static final long AWAIT_DURATION = 20;
    private static final TimeUnit AWAIT_TIME_UNIT = TimeUnit.SECONDS;

    private final SocketPlayerConnection connection;

    private final ReentrantReadWriteLock usernameLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock uniqueIdLock = new ReentrantReadWriteLock();

    private final CountDownLatch handlerLatch = new CountDownLatch(1);
    private final CountDownLatch acknowledgeLatch = new CountDownLatch(1);

    private final LoginSessionHandler sessionHandler;

    private @MonotonicNonNull String username;
    private @MonotonicNonNull UUID uniqueId;
    private Collection<GameProfile> gameProfiles = List.of();

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
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public @Nullable String username() {
        this.usernameLock.readLock().lock();
        try {
            return this.username;
        } finally {
            this.usernameLock.readLock().unlock();
        }
    }

    @Override
    public void username(@NonNull String username) {
        this.usernameLock.writeLock().lock();
        try {
            this.username = username;
        } finally {
            this.usernameLock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable UUID uniqueId() {
        this.uniqueIdLock.readLock().lock();
        try {
            return this.uniqueId;
        } finally {
            this.uniqueIdLock.readLock().unlock();
        }
    }

    @Override
    public void uniqueId(@NonNull UUID uniqueId) {
        this.uniqueIdLock.writeLock().lock();
        try {
            this.uniqueId = uniqueId;
        } finally {
            this.uniqueIdLock.writeLock().unlock();
        }
    }

    @Override
    public @NonNull Collection<GameProfile> gameProfiles() {
        return this.gameProfiles;
    }

    @Override
    public void gameProfiles(@NonNull Collection<GameProfile> gameProfiles) {
        this.gameProfiles = List.copyOf(gameProfiles);
    }

    @Override
    public void finish() {
        if (this.handlerLatch.getCount() == 0) {
            throw new IllegalArgumentException("The login session has been already finished");
        }
        this.handlerLatch.countDown();
    }

    @Override
    public @NonNull LoginSessionHandler sessionHandler() {
        return this.sessionHandler;
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
     * Begins the work of this login session.
     *
     * @since 1.0
     */
    public void begin() {
        Thread.ofVirtual()
                .uncaughtExceptionHandler((thread, exception) -> handleLoginError(exception, this.connection))
                .start(() -> {
                    try {
                        if (!this.handlerLatch.await(AWAIT_DURATION, AWAIT_TIME_UNIT)) {
                            this.sessionHandler.onTimeOut(this, LoginSessionHandler.TimeOutType.HANDLER);
                            throw new RuntimeException(new TimeoutException("The login handler has timed out"));
                        }

                        String username = this.username();
                        if (username == null) {
                            throw new IllegalArgumentException("The username was not set");
                        }

                        UUID uniqueId = this.uniqueId();
                        if (uniqueId == null) {
                            throw new IllegalArgumentException("The unique identifier was not set");
                        }

                        JetPlayer player = new JetPlayer(uniqueId, username, this.connection);
                        this.connection.initializePlayer(player);

                        PlayerLoginEvent loginEvent = new PlayerLoginEvent(player);
                        this.connection.server().eventNode().call(loginEvent);

                        if (loginEvent.getResult() instanceof PlayerLoginEvent.Result.Fail failResult) {
                            this.connection.disconnect(failResult.disconnectReason());
                            return;
                        }

                        this.connection.sendPacket(new ServerLoginSuccessLoginPacket(
                                uniqueId, username, this.gameProfiles(), true)
                        );

                        if (!this.acknowledgeLatch.await(AWAIT_DURATION, AWAIT_TIME_UNIT)) {
                            this.sessionHandler.onTimeOut(this, LoginSessionHandler.TimeOutType.ACKNOWLEDGE);
                            throw new TimeoutException("The login was not acknowledged on time");
                        }
                    } catch (InterruptedException | TimeoutException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    /**
     * Handles a {@linkplain Throwable throwable}, which is related to the login session.
     *
     * @param throwable the throwable
     * @param connection a player connection, which had the error
     * @since 1.0
     */
    public static void handleLoginError(@NonNull Throwable throwable, @NonNull PlayerConnection connection) {
        LOGGER.error("An error occurred while logging in a player", throwable);
        connection.disconnect(Component.text("An error occurred while logging you in", NamedTextColor.RED));
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