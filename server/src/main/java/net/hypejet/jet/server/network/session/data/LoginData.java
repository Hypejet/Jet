package net.hypejet.jet.server.network.session.data;

import net.hypejet.jet.session.login.profile.GameProfileProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a data specified
 * in {@linkplain net.hypejet.jet.server.network.session.task.LoginSessionTask a login session task}
 * that {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} associated with it should have.
 *
 * @param username a username that the player should have
 * @param uniqueId a unique identifier that the player should have
 * @param properties properties of a game profile that the player should have
 * @since 1.0
 * @see net.hypejet.jet.server.network.session.task.LoginSessionTask
 * @see net.hypejet.jet.server.entity.player.JetPlayer
 */
public record LoginData(@NonNull String username, @NonNull UUID uniqueId,
                        @NonNull Collection<GameProfileProperty> properties) {
    /**
     * Constructs the {@linkplain LoginData login data}.
     *
     * @param username a username that the player should have
     * @param uniqueId a unique identifier that the player should have
     * @param properties properties of a game profile that the player should have
     * @since 1.0
     */
    public LoginData {
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(uniqueId, "unique identifier");
        properties = Set.copyOf(Objects.requireNonNull(properties, "properties"));
    }
}