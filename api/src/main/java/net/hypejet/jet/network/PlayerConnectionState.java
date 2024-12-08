package net.hypejet.jet.network;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a state of {@linkplain PlayerConnection a player connection}.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example. Minecraft has already added additional player connection state once.</p>
 *
 * @since 1.0
 */
public final class PlayerConnectionState {

    /**
     * {@linkplain PlayerConnectionState A player connection state} used when {@linkplain PlayerConnection a player
     * connection} has been initialized, authenticated as well as configured and is ready to fully join to the server.
     *
     * @since 1.0
     */
    public static final PlayerConnectionState PLAY = new PlayerConnectionState("play");

    /**
     * {@linkplain PlayerConnectionState A player connection state} used when {@linkplain PlayerConnection a player
     * connection} has been initialized as well as authenticated and is going to be configured to follow server
     * settings.
     *
     * @since 1.0
     */
    public static final PlayerConnectionState CONFIGURATION = new PlayerConnectionState("configuration");

    /**
     * {@linkplain PlayerConnectionState A player connection state} used when {@linkplain PlayerConnection a player
     * connection} has been initialized and is going to be authenticated.
     *
     * @since 1.0
     */
    public static final PlayerConnectionState LOGIN = new PlayerConnectionState("login");

    /**
     * {@linkplain PlayerConnectionState A player connection state} used when {@linkplain PlayerConnection a player
     * connection} has been initialized and is going to retrieve information of the server for a server list on
     * a Minecraft client.
     *
     * @since 1.0
     */
    public static final PlayerConnectionState STATUS = new PlayerConnectionState("status");

    /**
     * {@linkplain PlayerConnectionState A player connection state} used when {@linkplain PlayerConnection a player
     * connection} is during creation and early initialization.
     *
     * @since 1.0
     */
    public static final PlayerConnectionState HANDSHAKE = new PlayerConnectionState("handshake");

    private final String name;

    private PlayerConnectionState(@NonNull String name) {
        this.name = NullabilityUtil.requireNonNull(name, "name");
    }

    /**
     * Gets a readable lower-case name of this connection state.
     *
     * @return the name
     * @since 1.0
     */
    public @NonNull String name() {
        return this.name;
    }

    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       since all instances are defined in constants of this class. */

    @Override
    public String toString() {
        return "State{" +
                "name='" + this.name + '\'' +
                '}';
    }
}