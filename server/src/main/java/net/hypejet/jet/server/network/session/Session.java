package net.hypejet.jet.server.network.session;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages {@linkplain ProtocolState a protocol state} with {@linkplain SessionTask a session
 * task}.
 *
 * @param protocolState a protocol state that the session should manage
 * @param connection a connection that should own the session
 * @param sessionTask a session task that should manage the session
 * @author Codestech
 * @see ProtocolState
 * @since 1.0
 */
public record Session(@NonNull ProtocolState protocolState, @NonNull SocketPlayerConnection connection,
                      @NonNull SessionTask sessionTask) implements NetworkDisconnectionHandler {
    /**
     * Constructs the {@linkplain Session session}.
     *
     * @param protocolState a protocol state that the session should manage
     * @param connection a connection that should own the session
     * @param sessionTask a session task that should manage the session
     * @since 1.0
     */
    public Session {
        NullabilityUtil.requireNonNull(protocolState, "protocol state");
        NullabilityUtil.requireNonNull(connection, "connection");
        NullabilityUtil.requireNonNull(sessionTask, "session task");
    }

    @Override
    public void handleDisconnection() {
        this.sessionTask.handleDisconnection();
    }
}