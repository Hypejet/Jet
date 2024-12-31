package net.hypejet.jet.server.network;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.network.PlayerConnectionState;
import org.checkerframework.checker.nullness.qual.NonNull;

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
                    "Could not find a player connection state representation for protocol state with name of %s",
                    this.name()
            ));
        }
        return state;
    }
}