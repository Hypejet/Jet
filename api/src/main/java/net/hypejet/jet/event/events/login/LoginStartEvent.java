package net.hypejet.jet.event.events.login;

import net.hypejet.jet.session.login.LoginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents {@linkplain LoginEvent a login event}, which is called when a login state is started
 * for {@linkplain net.hypejet.jet.network.PlayerConnection a player connection}.
 *
 * @param username a username received from a client backed by the player connection
 * @param uniqueId a unique identifier received from a client backed by the player connection
 * @param loginManager a login manager of the login state
 * @since 1.0
 * @see LoginEvent
 */
public record LoginStartEvent(@NonNull String username, @NonNull UUID uniqueId, @NonNull LoginManager loginManager)
        implements LoginEvent {
    /**
     * Constructs the {@linkplain LoginStartEvent login start event}.
     *
     * @param username a username received from a client backed by the player connection
     * @param uniqueId a unique identifier received from a client backed by the player connection
     * @param loginManager a login manager of the login state
     * @since 1.0
     */
    public LoginStartEvent {
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(uniqueId, "unique identifier");
        Objects.requireNonNull(loginManager, "login manager");
    }
}