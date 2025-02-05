package net.hypejet.jet.login;

import net.hypejet.jet.login.profile.GameProfileProperty;
import net.hypejet.jet.network.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents something that manages a state, during which {@linkplain PlayerConnection a player connection} is
 * authenticated.
 *
 * <p>A {@link #finish(String, UUID, Collection)} method should be called when the state should be finished.</p>
 *
 * @since 1.0
 */
public interface LoginManager {
    /**
     * Gets the player connection.
     *
     * @return the player connection
     * @since 1.0
     */
    @NonNull PlayerConnection connection();

    /**
     * Finishes the login state and initializes {@linkplain net.hypejet.jet.entity.player.Player a player}
     * of {@linkplain PlayerConnection a player connection} associated with the state with no additional properties.
     *
     * @param username a username that the player should have
     * @param uniqueId a unique identifier that the player should have
     * @throws IllegalStateException if the session has been already finished
     * @since 1.0
     */
    void finish(@NonNull String username, @NonNull UUID uniqueId);

    /**
     * Finishes the login state and {@linkplain net.hypejet.jet.entity.player.Player a player}
     * of {@linkplain PlayerConnection a player connection} associated with the state.
     *
     * @param username a username that the player should have
     * @param uniqueId a unique identifier that the player should have
     * @param properties additional properties of the player should have
     * @throws IllegalStateException if the session has been already finished
     * @since 1.0
     */
    void finish(@NonNull String username, @NonNull UUID uniqueId, @NonNull Collection<GameProfileProperty> properties);
}