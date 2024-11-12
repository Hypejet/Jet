package net.hypejet.jet.server.network.session;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages {@linkplain ProtocolState a protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ProtocolState
 */
public final class Session implements NetworkDisconnectionHandler {

    private final ProtocolState protocolState;
    private final SocketPlayerConnection connection;

    private final SessionTask sessionTask;
    private boolean started;

    /**
     * Constructs the {@linkplain Session session}.
     *
     * @param protocolState      a protocol state that the session should manage
     * @param connection         a player connection that should own the session
     * @param initialSessionTask an initial session task
     * @throws IllegalStateException if the caller thread is not an event loop thread
     * @since 1.0
     */
    public Session(@NonNull ProtocolState protocolState, @NonNull SocketPlayerConnection connection,
                   @NonNull SessionTask initialSessionTask) {
        connection.ensureInEventLoop();
        this.protocolState = NullabilityUtil.requireNonNull(protocolState, "protocol state");
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        this.sessionTask = NullabilityUtil.requireNonNull(initialSessionTask, "initial session task");
    }

    @Override
    public void handleDisconnection() {
        this.sessionTask.handleDisconnection();
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
     * Gets {@linkplain SocketPlayerConnection a player connection} that owns this session.
     *
     * @return the player connection
     * @since 1.0
     */
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    /**
     * Starts the session and schedules the initial {@linkplain SessionTask session task}.
     *
     * @throws IllegalArgumentException if the session has been already started
     * @since 1.0
     */
    public void startSession() {
        this.connection.ensureInEventLoop();

        if (this.started)
            throw new IllegalArgumentException("The session has been already started");
        this.started = true;

        if (this.sessionTask instanceof SessionTask.EventLoopTask eventLoopTask)
            eventLoopTask.runEventLoopTask();
        if (this.sessionTask instanceof SessionTask.VirtualThreadTask virtualThreadTask)
            Thread.ofVirtual()
                    .uncaughtExceptionHandler(this.connection)
                    .start(virtualThreadTask::runVirtualThreadTask);
    }

    /**
     * Gets {@linkplain SessionTask a task of the session}.
     *
     * @return the session task
     * @since 1.0
     */
    public @NonNull SessionTask sessionTask() {
        return this.sessionTask;
    }
}