package net.hypejet.jet.server.session;

import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.profile.GameProfile;
import net.hypejet.jet.session.LoginSession;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a server implementation of {@linkplain LoginSession login session}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginSession
 */
public final class JetLoginSession implements LoginSession, Session<LoginSessionHandler> {

    private static final long AWAIT_DURATION = 20;
    private static final TimeUnit AWAIT_TIME_UNIT = TimeUnit.SECONDS;

    private final PlayerConnection connection;

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
    public JetLoginSession(@NonNull PlayerConnection connection, @NonNull LoginSessionHandler sessionHandler) {
        this.connection = connection;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public @NonNull PlayerConnection connection() {
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
     * Blocks until a {@linkplain LoginSessionHandler login session handler} calls {@link #finish()} on this session.
     *
     * @return true if the count reached zero and {@code false} on time out
     * @since 1.0
     * @see CountDownLatch#await(long, TimeUnit)
     */
    public boolean awaitHandlerCompletion() {
        try {
            return this.handlerLatch.await(AWAIT_DURATION, AWAIT_TIME_UNIT);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Blocks until a client sends a {@linkplain ClientLoginAcknowledgeLoginPacket login acknowledge packet}.
     *
     * @return true if the count reached zero and {@code false} if on time out
     * @since 1.0
     * @see CountDownLatch#await(long, TimeUnit)
     */
    public boolean awaitAcknowledge() {
        try {
            return this.acknowledgeLatch.await(AWAIT_DURATION, AWAIT_TIME_UNIT);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Releases all threads blocked by {@link #awaitAcknowledge()}.
     *
     * @since 1.0
     * @see #awaitAcknowledge()
     */
    public void releaseAcknowledgeLatch() {
        this.acknowledgeLatch.countDown();
    }
}