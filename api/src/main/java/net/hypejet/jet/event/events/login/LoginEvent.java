package net.hypejet.jet.event.events.login;

import net.hypejet.jet.session.login.LoginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event related to logging state
 * of {@linkplain net.hypejet.jet.network.PlayerConnection a player connection}.
 *
 * @since 1.0
 * @see net.hypejet.jet.network.PlayerConnection
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