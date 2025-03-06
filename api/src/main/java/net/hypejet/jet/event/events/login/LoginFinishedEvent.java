package net.hypejet.jet.event.events.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an event called when a login state
 * of {@linkplain net.hypejet.jet.network.PlayerConnection a player connection} is being finished.
 *
 * @since 1.0
 * @see net.hypejet.jet.network.PlayerConnection
 */
public final class LoginFinishedEvent {

    private final PlayerConnection connection;

    private Result result = Result.success();

    /**
     * Constructs the {@linkplain LoginFinishedEvent player login event}.
     *
     * @param connection a player connection that the login state is being finished for
     * @since 1.0
     */
    public LoginFinishedEvent(@NonNull PlayerConnection connection) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    /**
     * Gets {@linkplain PlayerConnection a player connection} that the login state is being finished for.
     *
     * @return the player connection
     * @since 1.0
     */
    public @NonNull PlayerConnection connection() {
        return this.connection;
    }

    /**
     * Gets {@linkplain Result a result} of the login.
     *
     * @return the result
     * @since 1.0
     */
    public @NonNull Result getResult() {
        return this.result;
    }

    /**
     * Sets {@linkplain Result a result} of the login.
     *
     * @param result the result
     * @since 1.0
     */
    public void setResult(@NonNull Result result) {
        this.result = NullabilityUtil.requireNonNull(result, "result");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LoginFinishedEvent that)) return false;
        return Objects.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.connection, result);
    }

    @Override
    public String toString() {
        return "LoginFinishedEvent{" +
                "result=" + this.result +
                '}';
    }


    /**
     * Represents a result of a login state
     * of {@linkplain net.hypejet.jet.network.PlayerConnection a player connection}.
     *
     * @since 1.0
     * @see net.hypejet.jet.network.PlayerConnection
     */
    public sealed interface Result {
        /**
         * Represents a result, which disconnects a player that is logging to the server.
         *
         * @param disconnectReason a reason of the disconnection
         * @since 1.0
         */
        record Fail(@NonNull Component disconnectReason) implements Result {
            /**
             * Constructs the {@linkplain Fail fail login result}.
             *
             * @param disconnectReason a reason of the disconnection
             * @since 1.0
             */
            public Fail {
                NullabilityUtil.requireNonNull(disconnectReason, "disconnect reason");
            }
        }

        /**
         * Represents a successful login result.
         *
         * @since 1.0
         */
        final class Success implements Result {
            private static final Success INSTANCE = new Success();
            private Success() {}
        }

        /**
         * Gets an instance of the {@linkplain Success success login result}.
         *
         * @return the instance
         * @since 1.0
         */
        static @NonNull Success success() {
            return Success.INSTANCE;
        }
    }
}