package net.hypejet.jet.event.events.login;

import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents an event called when a {@linkplain net.hypejet.jet.session.LoginSession login session is being
 * initialized}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.session.LoginSession
 */
public final class LoginSessionInitializeEvent {

    private final PlayerConnection connection;

    private @MonotonicNonNull LoginSessionHandler sessionHandler;

    /**
     * Constructs the {@linkplain LoginSessionInitializeEvent login session initialize event}.
     *
     * @param connection a connection, for which the login session is being initialized
     * @since 1.0
     */
    public LoginSessionInitializeEvent(@NonNull PlayerConnection connection) {
        this.connection = connection;
    }

    /**
     * Gets a connection, for which the login session is being initialized.
     *
     * @return the connection
     * @since 1.0
     */
    public @NonNull PlayerConnection getConnection() {
        return this.connection;
    }

    /**
     * Gets a {@linkplain LoginSessionHandler login session handler}, which will be used to handle the login session.
     *
     * @return the login session handler, {@code null} if not initialized yet
     * @since 1.0
     */
    public @Nullable LoginSessionHandler getSessionHandler() {
        return this.sessionHandler;
    }

    /**
     * Sets a {@linkplain LoginSessionHandler login session handler}, which will be used to handle the login session.
     *
     * @param sessionHandler the login session handler
     * @since 1.0
     */
    public void setSessionHandler(@NonNull LoginSessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginSessionInitializeEvent that)) return false;
        return Objects.equals(this.connection, that.connection)
                && Objects.equals(this.sessionHandler, that.sessionHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.connection, this.sessionHandler);
    }

    @Override
    public String toString() {
        return "StartLoginEvent{" +
                "connection=" + this.connection +
                ", sessionHandler=" + this.sessionHandler +
                '}';
    }
}