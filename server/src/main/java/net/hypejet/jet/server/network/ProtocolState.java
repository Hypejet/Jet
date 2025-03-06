package net.hypejet.jet.server.network;

import net.hypejet.jet.network.PlayerConnection;

/**
 * Represents a state of a Minecraft protocol.
 *
 * @since 1.0
 */
public enum ProtocolState {
    /**
     * {@linkplain ProtocolState A protocol state} used when {@linkplain PlayerConnection a player connection} has
     * been initialized, authenticated as well as configured and is ready to fully join to the server.
     *
     * @since 1.0
     */
    PLAY,
    /**
     * {@linkplain ProtocolState A protocol state} used when {@linkplain PlayerConnection a player connection} has been
     * initialized as well as authenticated and is going to be configured to follow server settings.
     *
     * @since 1.0
     */
    CONFIGURATION,
    /**
     * {@linkplain ProtocolState A protocol state} used when {@linkplain PlayerConnection a player connection} has been
     * initialized and is going to be authenticated.
     *
     * @since 1.0
     */
    LOGIN,
    /**
     * {@linkplain ProtocolState A protocol state} used when {@linkplain PlayerConnection a player connection} has been
     * initialized and is going to retrieve information of the server for a server list on a Minecraft client.
     *
     * @since 1.0
     */
    STATUS,
    /**
     * {@linkplain ProtocolState A protocol state} used when {@linkplain PlayerConnection a player connection}
     * is during creation and early initialization.
     *
     * @since 1.0
     */
    HANDSHAKE
}