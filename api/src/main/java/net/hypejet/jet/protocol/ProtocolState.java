package net.hypejet.jet.protocol;

/**
 * Represents a state of a Minecraft protocol.
 *
 * @since 1.0
 * @author Codestech
 */
public enum ProtocolState {
    /**
     * A {@linkplain ProtocolState protocol state} used for Minecraft connection initialization,
     *
     * @since 1.0
     */
    HANDSHAKE,
    /**
     * A {@linkplain ProtocolState protocol state} used for getting server list data from a Minecraft client.
     *
     * @since 1.0
     */
    STATUS,
    /**
     * A {@linkplain ProtocolState protocol state} used for retrieving player's data and authenticating it.
     *
     * @since 1.0
     */
    LOGIN,
    /**
     * A {@linkplain ProtocolState protocol state} used for configuring player by a server.
     *
     * @since 1.0
     */
    CONFIGURATION,
    /**
     * A {@linkplain ProtocolState protocol state} used when a player is in the game.
     *
     * @since 1.0
     */
    PLAY
}