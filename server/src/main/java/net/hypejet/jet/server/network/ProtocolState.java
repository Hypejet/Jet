package net.hypejet.jet.server.network;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.network.PlayerConnectionState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a state of a Minecraft protocol.
 *
 * @since 1.0
 * @author Codestech
 */
public enum ProtocolState {
    /**
     * {@linkplain ProtocolState A protocol state} used when a player is in the game.
     *
     * @since 1.0
     */
    PLAY,
    /**
     * {@linkplain ProtocolState A protocol state} used for configuring player by a server.
     *
     * @since 1.0
     */
    CONFIGURATION,
    /**
     * {@linkplain ProtocolState A protocol state} used for retrieving player's data and authenticating it.
     *
     * @since 1.0
     */
    LOGIN,
    /**
     * {@linkplain ProtocolState A protocol state} used for getting server list data from a Minecraft client.
     *
     * @since 1.0
     */
    STATUS,
    /**
     * {@linkplain ProtocolState A protocol state} used for Minecraft connection initialization.
     *
     * @since 1.0
     */
    HANDSHAKE;

    private static final Mapper<ProtocolState, PlayerConnectionState> STATE_MAPPER =
            Mapper.builder(ProtocolState.class, PlayerConnectionState.class)
                    .register(ProtocolState.PLAY, PlayerConnectionState.PLAY)
                    .register(ProtocolState.CONFIGURATION, PlayerConnectionState.CONFIGURATION)
                    .register(ProtocolState.LOGIN, PlayerConnectionState.LOGIN)
                    .register(ProtocolState.STATUS, PlayerConnectionState.STATUS)
                    .register(ProtocolState.HANDSHAKE, PlayerConnectionState.HANDSHAKE)
                    .build();

    /**
     * Gets {@linkplain PlayerConnectionState a player connection state} representation of this
     * {@linkplain ProtocolState protocol state}.
     *
     * @return the player connection state representation
     * @since 1.0
     * @throws IllegalStateException if the player connection state representation could not be found
     */
    public @NonNull PlayerConnectionState toConnectionState() {
        PlayerConnectionState state = STATE_MAPPER.write(this);
        if (state == null) {
            throw new IllegalStateException(String.format(
                    "Could not find a player connection state for protocol state with name of %s",
                    this.name()
            ));
        }
        return state;
    }

    /**
     * Gets {@linkplain ProtocolState a protocol state} representation of {@linkplain PlayerConnectionState a player
     * connection state} specified.
     *
     * @param connectionState the player connection state
     * @return the protocol state representation
     * @since 1.0
     * @throws IllegalStateException if the protocol state representation could not be found
     */
    public static @NonNull ProtocolState fromConnectionState(@NonNull PlayerConnectionState connectionState) {
        ProtocolState protocolState = STATE_MAPPER.read(connectionState);
        if (protocolState == null) {
            throw new IllegalStateException(String.format(
                    "Could not find a protocol state for a player connection state with name of %s",
                    connectionState.name()
            ));
        }
        return protocolState;
    }
}