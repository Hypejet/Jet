package net.hypejet.jet.server.network.packet.handler;

import net.hypejet.jet.network.PlayerConnection;

/**
 * Represents a function, which handles a disconnection of {@linkplain PlayerConnection a player connection}.
 *
 * @since 1.0
 * @since 1.0
 * @see PlayerConnection
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