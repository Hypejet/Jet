package net.hypejet.jet.event.events.login;

import net.hypejet.jet.login.LoginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event related to {@linkplain net.hypejet.jet.network.PlayerConnectionState#LOGIN a login player
 * connection state}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.network.PlayerConnectionState#LOGIN
 */
public interface LoginEvent {
    /**
     * {@linkplain LoginManager A login manager} that is managing the login connection state.
     *
     * @return the login manager
     * @since 1.0
     */
    @NonNull LoginManager loginManager();
}