package net.hypejet.jet.event.events.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when {@linkplain net.hypejet.jet.network.PlayerConnectionState#LOGIN a login connection
 * state} is being finished and {@linkplain Player a player} has been created.
 *
 * @since 1.0
 * @see Player
 * @see net.hypejet.jet.network.PlayerConnectionState#LOGIN
 */
public final class LoginFinishedEvent {

    private final Player player;

    private Result result = Result.success();

    /**
     * Constructs the {@linkplain LoginFinishedEvent player login event}.
     *
     * @param player a {@linkplain Player player} that is logging into the server
     * @since 1.0
     */
    public LoginFinishedEvent(@NonNull Player player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
    }

    /**
     * Gets {@linkplain Player a player} that is logging into the server.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull Player player() {
        return this.player;
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

    /**
     * Represents a result of {@linkplain net.hypejet.jet.network.PlayerConnectionState#LOGIN a login connection
     * state}.
     *
     * @since 1.0
     * @see net.hypejet.jet.network.PlayerConnectionState#LOGIN
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