package net.hypejet.jet.event.events.login;

import net.hypejet.jet.entity.player.Player;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain net.hypejet.jet.session.LoginSession login session} is being finished
 * and a {@linkplain Player player} is being created.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.session.LoginSession
 * @see Player
 * @see Result
 */
public final class PlayerLoginEvent {

    private final Player player;

    private Result result = Result.success();

    /**
     * Constructs the {@linkplain PlayerLoginEvent player login event}.
     *
     * @param player a {@linkplain Player player} that is being logged in
     * @since 1.0
     */
    public PlayerLoginEvent(@NonNull Player player) {
        this.player = player;
    }

    /**
     * Gets a {@linkplain Player player} that is being logged in.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull Player getPlayer() {
        return this.player;
    }

    /**
     * Gets a {@linkplain Result result} of the login.
     *
     * @return the result
     * @since 1.0
     */
    public @NonNull Result getResult() {
        return this.result;
    }

    /**
     * Sets a {@linkplain Result result} of the login.
     *
     * @param result the result
     * @since 1.0
     */
    public void setResult(@NonNull Result result) {
        this.result = result;
    }

    /**
     * Represents a result of a player login.
     *
     * @since 1.0
     * @author Codestech
     */
    public sealed interface Result {
        /**
         * Represents a result, which will disconnect a player.
         *
         * @param disconnectReason a reason of the disconnection
         * @since 1.0
         * @author Codestech
         */
        record Fail(@NonNull Component disconnectReason) implements Result {}

        /**
         * Represents a successful login result.
         *
         * @since 1.0
         * @author Codestech
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