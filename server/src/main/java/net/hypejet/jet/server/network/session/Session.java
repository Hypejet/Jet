package net.hypejet.jet.server.network.session;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Represents something that manages {@linkplain ProtocolState a protocol state}.
 *
 * @author Codestech
 * @since 1.0
 * @see ProtocolState
 */
public final class Session implements NetworkDisconnectionHandler {

    private final ProtocolState protocolState;
    private final SocketPlayerConnection connection;
    private final Supplier<SessionTask> sessionTaskSupplier;

    private @MonotonicNonNull SessionTask sessionTask;
    private final ReentrantLock startSessionLock = new ReentrantLock();

    /**
     * Constructs the {@linkplain Session session}.
     *
     * @param protocolState a protocol state that the session should manage
     * @param connection a connection that should own the session
     * @param sessionTaskSupplier a supplier of a session task that should manage the session
     * @since 1.0
     */
    public Session(@NonNull ProtocolState protocolState, @NonNull SocketPlayerConnection connection,
                   @NonNull Supplier<SessionTask> sessionTaskSupplier) {
        this.protocolState = NullabilityUtil.requireNonNull(protocolState, "protocol state");
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        this.sessionTaskSupplier =  NullabilityUtil.requireNonNull(sessionTaskSupplier, "session task supplier");
    }

    @Override
    public void handleDisconnection() {
        this.sessionTask.handleDisconnection();
    }

    /**
     * Starts the session.
     *
     * @since 1.0
     * @throws IllegalStateException if the session has been already started
     */
    public void startSession() {
        try {
            this.startSessionLock.lock();
            if (this.sessionTask != null)
                throw new IllegalStateException("The session has been already started");
            this.sessionTask = this.sessionTaskSupplier.get();
        } finally {
            this.startSessionLock.unlock();
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
     * @return the session task
     * @since 1.0
     * @throws IllegalStateException if the session task has been not initialized
     */
    public @NonNull SessionTask sessionTask() {
        if (this.sessionTask == null)
            throw new IllegalStateException("The session task has not been initialized");
        return this.sessionTask;
    }
}