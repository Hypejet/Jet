package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;

/**
 * Represents a task of {@linkplain net.hypejet.jet.server.network.session.Session a session}.
 *
 * <p>Session tasks during construction are not safe for all packet operations. That should be done in {@link #start()}
 * instead.</p>
 *
 * @since 1.0
 * @author Codestech
 */
public interface SessionTask extends NetworkDisconnectionHandler {
    /**
     * Starts the session task.
     *
     * <p>Called when a session with this session task has been set for
     * {@linkplain net.hypejet.jet.server.network.SocketPlayerConnection a player connection}.</p>
     *
     * @since 1.0
     */
    void start();
}