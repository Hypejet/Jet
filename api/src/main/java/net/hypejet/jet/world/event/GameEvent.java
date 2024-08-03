package net.hypejet.jet.world.event;

import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft game event.
 *
 * @since 1.0
 * @author Codestech
 */
public sealed interface GameEvent {
    /**
     * Represents a {@linkplain GameEvent game event} used when a {@linkplain Player.GameMode game mode} is being
     * changed for a {@linkplain Player player}.
     *
     * @param gameMode the new game mode
     * @author Codestech
     * @since 1.0
     */
    record ChangeGameMode(Player.@NonNull GameMode gameMode) implements GameEvent {}

    /**
     * Represents a {@linkplain GameEvent game event} used when a {@linkplain Player player} wins the game.
     *
     * @param rollCredits whether the credits should be rolled or a player should be just respawned
     * @author Codestech
     * @since 1.0
     */
    record WinGame(boolean rollCredits) implements GameEvent {}

    /**
     * Represents a {@linkplain GameEvent game event}, which triggers a {@linkplain Event Minecraft demo event}.
     *
     * @param event the Minecraft demo event
     * @author Codestech
     * @since 1.0
     */
    record DemoEvent(@NonNull Event event) implements GameEvent {
        /**
         * Represents a Minecraft demo event.
         *
         * @author Codestech
         * @since 1.0
         */
        public enum Event {
            /**
             * A demo event that will show a welcome screen.
             *
             * @since 1.0
             */
            WELCOME_TO_DEMO_SCREEN,
            /**
             * A demo event that will show movement controls.
             *
             * @since 1.0
             */
            TELL_MOVEMENT_CONTROLS,
            /**
             * A demo event that will show jump control.
             *
             * @since 1.0
             */
            TELL_JUMP_CONTROL,
            /**
             * A demo event that will show inventory control.
             *
             * @since 1.0
             */
            TELL_INVENTORY_CONTROL,
            /**
             * A demo event telling that the demo is over and printing a message how to take a screenshot.
             *
             * @since 1.0
             */
            DEMO_OVER
        }
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which changes a rain level on a client.
     *
     * @param level the rain level
     * @author Codestech
     * @since 1.0
     */
    record RainLevelChange(float level) implements GameEvent { }

    /**
     * Represents a {@linkplain GameEvent game event}, which changes a thunder level on a client.
     *
     * @param level the thunder level
     * @author Codestech
     * @since 1.0
     */
    record ThunderLevelChange(float level) implements GameEvent { }

    /**
     * Represents a {@linkplain GameEvent game event}, which changes whether the respawn screen is enabled on the
     * server.
     *
     * @param enable whether the respawn screen is enabled
     * @author Codestech
     * @since 1.0
     */
    record EnableRespawnScreen(boolean enable) implements GameEvent { }

    /**
     * Represents a {@linkplain GameEvent game event}, which changes whether only unlocked recipes should be able
     * to be used.
     *
     * @param enable whether only unlocked recipes should be able to be used
     * @author Codestech
     * @since 1.0
     */
    record EnableLimitedCrafting(boolean enable) implements GameEvent { }

    /**
     * Represents a {@linkplain GameEvent game event}, which plays a pufferfish sting sound.
     *
     * @author Codestech
     * @since 1.0
     */
    final class PlayPufferfishStingSound implements GameEvent {
        private static final PlayPufferfishStingSound INSTANCE = new PlayPufferfishStingSound();
        private PlayPufferfishStingSound() {}
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which plays an elder guardian mob appearance effect.
     *
     * @author Codestech
     * @since 1.0
     */
    final class PlayElderGuardianMobAppearance implements GameEvent {
        private static final PlayElderGuardianMobAppearance INSTANCE = new PlayElderGuardianMobAppearance();
        private PlayElderGuardianMobAppearance() {}
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which is sent when any {@linkplain Player player} is being
     * struck by an arrow.
     *
     * @author Codestech
     * @since 1.0
     */
    final class ArrowHitPlayer implements GameEvent {
        private static final ArrowHitPlayer INSTANCE = new ArrowHitPlayer();
        private ArrowHitPlayer() {}
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which displays a message informing a player that they
     * have no home bed or charged respawn anchor.
     *
     * @author Codestech
     * @since 1.0
     */
    final class NoRespawnBlockAvailable implements GameEvent {
        private static final NoRespawnBlockAvailable INSTANCE = new NoRespawnBlockAvailable();
        private NoRespawnBlockAvailable() {}
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which starts playing a raining effect.
     *
     * @author Codestech
     * @since 1.0
     */
    final class BeginRaining implements GameEvent {
        private static final BeginRaining INSTANCE = new BeginRaining();
        private BeginRaining() {}
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which stops playing a raining effect.
     *
     * @author Codestech
     * @since 1.0
     */
    final class EndRaining implements GameEvent {
        private static final EndRaining INSTANCE = new EndRaining();
        private EndRaining() {}
    }

    /**
     * Represents a {@linkplain GameEvent game event}, which instructs the client to wait for chunks of a world.
     *
     * @author Codestech
     * @since 1.0
     */
    final class StartWaitingForWorldChunks implements GameEvent {
        private static final StartWaitingForWorldChunks INSTANCE = new StartWaitingForWorldChunks();
        private StartWaitingForWorldChunks() {}
    }

    /**
     * Gets an instance of a {@link PlayPufferfishStingSound}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull PlayPufferfishStingSound pufferfishStringSound() {
        return PlayPufferfishStingSound.INSTANCE;
    }

    /**
     * Gets an instance of a {@link PlayElderGuardianMobAppearance}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull PlayElderGuardianMobAppearance elderGuardianMobAppearance() {
        return PlayElderGuardianMobAppearance.INSTANCE;
    }

    /**
     * Gets an instance of a {@link ArrowHitPlayer}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull ArrowHitPlayer arrowHitPlayer() {
        return ArrowHitPlayer.INSTANCE;
    }

    /**
     * Gets an instance of a {@link NoRespawnBlockAvailable}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull NoRespawnBlockAvailable noRespawnBlockAvailable() {
        return NoRespawnBlockAvailable.INSTANCE;
    }

    /**
     * Gets an instance of a {@link BeginRaining}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull BeginRaining beginRaining() {
        return BeginRaining.INSTANCE;
    }

    /**
     * Gets an instance of a {@link EndRaining}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull EndRaining endRaining() {
        return EndRaining.INSTANCE;
    }

    /**
     * Gets an instance of a {@link StartWaitingForWorldChunks}.
     *
     * @return the instance
     * @since 1.0
     */
    static @NonNull StartWaitingForWorldChunks waitForWorldChunks() {
        return StartWaitingForWorldChunks.INSTANCE;
    }
}