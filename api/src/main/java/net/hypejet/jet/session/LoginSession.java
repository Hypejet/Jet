package net.hypejet.jet.session;

import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.profile.GameProfile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a session, during which a player is authenticated.
 * <p>A {@linkplain #finish()} method should be called when the session is finished.</p>
 *
 * @since 1.0
 * @author Codestech
 */
public interface LoginSession {
    /**
     * Gets a connection with the player.
     *
     * @return the connection
     * @since 1.0
     */
    @NonNull PlayerConnection connection();

    /**
     * Gets a username of the player.
     *
     * @return the username, {@code null} if not set yet
     * @since 1.0
     */
    @Nullable String username();

    /**
     * Sets a username of the player.
     *
     * @param username the username
     * @since 1.0
     */
    void username(@NonNull String username);

    /**
     * Gets a {@linkplain UUID unique identifier} of the player.
     *
     * @return the unique identifier, {@code null} if not set yet
     * @since 1.0
     */
    @Nullable UUID uniqueId();

    /**
     * Sets a {@linkplain UUID unique identifier} of the player.
     *
     * @param uniqueId the unique identifier
     * @since 1.0
     */
    void uniqueId(@NonNull UUID uniqueId);

    /**
     * Gets a {@linkplain Collection collection} of {@linkplain GameProfile game profiles} of the player.
     *
     * @return the collection, empty if not set yet
     * @since 1.0
     */
    @NonNull Collection<GameProfile> gameProfiles();

    /**
     * Sets a {@linkplain Collection collection} of {@linkplain GameProfile game profiles} of the player.
     *
     * @param gameProfiles the collection
     * @since 1.0
     */
    void gameProfiles(@NonNull Collection<GameProfile> gameProfiles);

    /**
     * Finishes the session.
     *
     * @since 1.0
     */
    void finish();
}