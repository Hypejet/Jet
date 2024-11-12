package net.hypejet.jet.server.network.handler;

/**
 * Represents a function, which handles a disconnection
 * of {@linkplain net.hypejet.jet.protocol.connection.PlayerConnection a player connection}.
 *
 * @since 1.0
 * @author Codestech
 * @since 1.0
 * @see net.hypejet.jet.protocol.connection.PlayerConnection
 */
@FunctionalInterface
public interface NetworkDisconnectionHandler {
    /**
     * Handles the disconnection.
     *
     * @since 1.0
     */
    void handleDisconnection();
}