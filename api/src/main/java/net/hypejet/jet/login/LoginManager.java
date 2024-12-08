package net.hypejet.jet.login;

import net.hypejet.jet.login.profile.GameProfileProperty;
import net.hypejet.jet.network.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a something that manages a state, during which {@linkplain PlayerConnection a player connection} is
 * authenticated.
 *
 * <p>A {@link #finish(String, UUID, Collection)} method should be called when the state should be finished.</p>
 *
 * @since 1.0
 * @author Codestech
 */
public interface LoginManager {
    /**
     * Gets a connection with the player.
     *
     * @return the connection
     * @since 1.0
     */
    @NonNull PlayerConnection connection();

    /**
     * Finishes the login state and initializes {@linkplain net.hypejet.jet.entity.player.Player a player}
     * in {@linkplain PlayerConnection a player connection} with no additional properties.
     *
     * @param username a username that the player should have
     * @param uniqueId a unique identifier that the player should have
     * @since 1.0
     * @throws IllegalStateException if the session has been already finished
     */
    void finish(@NonNull String username, @NonNull UUID uniqueId);

    /**
     * Finishes the login state and {@linkplain net.hypejet.jet.entity.player.Player a player}
     * in {@linkplain PlayerConnection a player connection}.
     *
     * @param username a username that the player should have
     * @param uniqueId a unique identifier that the player should have
     * @param properties additional properties of the player should have
     * @since 1.0
     * @throws IllegalStateException if the session has been already finished
     */
    void finish(@NonNull String username, @NonNull UUID uniqueId, @NonNull Collection<GameProfileProperty> properties);
}