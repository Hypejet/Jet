package net.hypejet.jet.server.network.session;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents something that manages {@linkplain ProtocolState a protocol state} with {@linkplain SessionTask a session
 * task}.
 *
 * <p>Session after construction does not do anything. It is required to {@linkplain #startSession(SessionTask) start
 * the session manually}. Sessions work like that to ensure that everything works correctly. Some session tasks acquire
 * sessions at the construction and to ensure that session has been already fully initialized and set we use
 * this approach.</p>
 *
 * @author Codestech
 * @since 1.0
 * @see ProtocolState
 */
public final class Session implements NetworkDisconnectionHandler {

    private final ProtocolState protocolState;
    private final SocketPlayerConnection connection;

    private @MonotonicNonNull SessionTask sessionTask;
    private final ReentrantReadWriteLock startSessionLock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain Session session}.
     *
     * @param protocolState a protocol state that the session should manage
     * @param connection a connection that should own the session
     * @since 1.0
     */
    public Session(@NonNull ProtocolState protocolState, @NonNull SocketPlayerConnection connection) {
        this.protocolState = NullabilityUtil.requireNonNull(protocolState, "protocol state");
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    @Override
    public void handleDisconnection() {
        this.sessionTask.handleDisconnection();
    }

    /**
     * Starts the session.
     *
     * @param sessionTask a session task that should manage the session
     * @since 1.0
     * @throws IllegalStateException if the session has been already started
     */
    public void startSession(@NonNull SessionTask sessionTask) {
        try {
            this.startSessionLock.writeLock().lock();
            if (this.sessionTask != null)
                throw new IllegalStateException("The session has been already started");
            this.sessionTask = NullabilityUtil.requireNonNull(sessionTask, "session task");
        } finally {
            this.startSessionLock.writeLock().unlock();
        }
    }

    /**
     * Gets {@linkplain ProtocolState a protocol state} that the session manages.
     *
     * @return the protocol state
     * @since 1.0
     */
    public @NonNull ProtocolState protocolState() {
        return this.protocolState;
    }

    /**
     * Gets {@linkplain SocketPlayerConnection a socket player connection} that owns the session.
     *
     * @return the socket player connection
     * @since 1.0
     */
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    /**
     * Gets {@linkplain SessionTask a session task} that manages the session.
     *
     * @return the session task, {@code null} if the session has not benn started yet
     * @since 1.0
     */
    public @Nullable SessionTask sessionTask() {
        try {
            this.startSessionLock.readLock().lock();
            return this.sessionTask;
        } finally {
            this.startSessionLock.readLock().unlock();
        }
    }
}