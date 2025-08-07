package net.hypejet.jet.server.entity.acquisition.gamemode;

import net.hypejet.concurrency.Acquirable;
import net.hypejet.concurrency.Acquisition;
import java.util.Objects;
import net.hypejet.jet.entity.acquisition.gamemode.GameModeAcquisition;
import net.hypejet.jet.entity.acquisition.gamemode.WriteGameModeAcquisition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.world.event.world.events.ChangeGameModeWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents {@linkplain Acquirable an acquirable}, which guards {@linkplain Player.GameMode a game mode}
 * of {@linkplain JetPlayer a player}.
 *
 * @since 1.0
 * @see Acquirable
 * @see Player.GameMode
 * @see JetPlayer
 */
public final class GameModeAcquirable
        extends Acquirable<GameModeAcquisition, WriteGameModeAcquisition> {

    private final JetPlayer player;

    private Player.@NonNull GameMode gameMode;
    private Player.@Nullable GameMode previousGameMode;

    /**
     * Constructs the {@linkplain GameModeAcquirable game mode acquirable}.
     *
     * @param player a player that the game mode is guarded for
     * @param gameMode an initial game mode that the player should have
     * @param previousGameMode a previous game mode that the player had
     * @since 1.0
     */
    public GameModeAcquirable(@NonNull JetPlayer player, Player.@NonNull GameMode gameMode,
                              Player.@Nullable GameMode previousGameMode) {
        this.player = Objects.requireNonNull(player, "player");
        this.gameMode = Objects.requireNonNull(gameMode, "game mode");
        this.previousGameMode = previousGameMode;
    }

    @Override
    protected @NotNull GameModeAcquisition createReadAcquisition() {
        return new GameModeAcquisitionImpl(this, Acquisition.AcquisitionType.READ);
    }

    @Override
    protected @NotNull WriteGameModeAcquisition createWriteAcquisition() {
        return new WriteGameModeAcquisitionImpl(this);
    }

    @Override
    protected @NotNull GameModeAcquisition reuseReadAcquisition(@NotNull GameModeAcquisition originalAcquisition) {
        return new ReusedGameModeAcquisition<>(originalAcquisition);
    }

    @Override
    protected @NotNull WriteGameModeAcquisition reuseWriteAcquisition(
            @NotNull WriteGameModeAcquisition originalAcquisition
    ) {
        return new ReusedWriteGameModeAcquisition(originalAcquisition);
    }

    @Override
    protected @NotNull WriteGameModeAcquisition createUpgradedAcquisition(
            @NotNull GameModeAcquisition originalAcquisition
    ) {
        return new UpgradedGameModeAcquisition(originalAcquisition, this);
    }

    @Override
    protected @Nullable WriteGameModeAcquisition castToWriteAcquisition(@NotNull GameModeAcquisition acquisition) {
        if (acquisition instanceof WriteGameModeAcquisition writeAcquisition)
            return writeAcquisition;
        return null;
    }

    /**
     * Represents an implementation of {@linkplain AbstractAcquisition an abstract acquisition}
     * and {@linkplain GameModeAcquisition a game mode acquisition}.
     *
     * @since 1.0
     * @see AbstractAcquisition
     * @see GameModeAcquisition
     */
    private static class GameModeAcquisitionImpl
            extends AbstractAcquisition<GameModeAcquisition, GameModeAcquirable>
            implements GameModeAcquisition {
        /**
         * Constructs the {@linkplain GameModeAcquisitionImpl game mode acquisition implementation}.
         *
         * @param acquirable an acquirable whose game mode is guarded by the lock
         * @param type a type, of which the acquisition should be
         * @since 1.0
         */
        private GameModeAcquisitionImpl(@NotNull GameModeAcquirable acquirable, @NotNull AcquisitionType type) {
            super(acquirable, type);
        }

        @Override
        public final Player.@Nullable GameMode previous() {
            this.ensurePermittedAndLocked();
            return this.acquirable.previousGameMode;
        }

        @Override
        public final @NotNull Player.GameMode get() {
            this.ensurePermittedAndLocked();
            return this.acquirable.gameMode;
        }

        @Override
        protected final @NotNull GameModeAcquisition cast() {
            return this;
        }
    }

    /**
     * Represents an implementation of {@linkplain GameModeAcquisitionImpl a game mode acquisition implementation}
     * and {@linkplain WriteGameModeAcquisition a write game mode acquisition}.
     *
     * @since 1.0
     * @see WriteGameModeAcquisition
     */
    private static final class WriteGameModeAcquisitionImpl extends GameModeAcquisitionImpl
            implements SetOperationImplementation {
        /**
         * Constructs the {@linkplain WriteGameModeAcquisitionImpl write game mode acquisition implementation}.
         *
         * @param acquirable an acquirable whose game mode is guarded by the lock
         * @since 1.0
         */
        private WriteGameModeAcquisitionImpl(@NotNull GameModeAcquirable acquirable) {
            super(acquirable, AcquisitionType.WRITE);
        }

        @Override
        public @NonNull GameModeAcquirable acquirable() {
            return this.acquirable;
        }
    }

    /**
     * Represents an implementation of {@linkplain UpgradedAcquisition an upgraded acquisition}
     * and {@linkplain WriteGameModeAcquisition a write game mode acquisition}.
     *
     * @since .10
     * @see WriteGameModeAcquisition
     * @see UpgradedAcquisition
     */
    private static final class UpgradedGameModeAcquisition
            extends UpgradedAcquisition<GameModeAcquisition, GameModeAcquirable>
            implements SetOperationImplementation {
        /**
         * Constructs the {@linkplain UpgradedGameModeAcquisition upgraded game mode acquisition}.
         *
         * @param originalAcquisition an original acquisition that should be reused
         * @param acquirable an acquirable, which owns the acquisition that should be reused
         * @since 1.0
         */
        private UpgradedGameModeAcquisition(@NotNull GameModeAcquisition originalAcquisition,
                                              @NotNull GameModeAcquirable acquirable) {
            super(originalAcquisition, acquirable);
        }

        @Override
        public Player.@Nullable GameMode previous() {
            return this.originalAcquisition.previous();
        }

        @Override
        public Player.@NotNull GameMode get() {
            return this.originalAcquisition.get();
        }

        @Override
        public @NonNull GameModeAcquirable acquirable() {
            return this.acquirable;
        }
    }

    /**
     * Represents an implementation of {@linkplain ReusedAcquisition a reused acquisition}
     * and {@linkplain GameModeAcquisition a game mode acquisition}.
     *
     * @param <A> a type of game mode acquisition that is being reused
     * @since 1.0
     * @see ReusedAcquisition
     * @see GameModeAcquisition
     */
    private static class ReusedGameModeAcquisition<A extends GameModeAcquisition> extends ReusedAcquisition<A>
            implements GameModeAcquisition {
        /**
         * Constructs the {@linkplain ReusedGameModeAcquisition reused game mode acquisition}.
         *
         * @param originalAcquisition an original acquisition should be reused
         * @since 1.0
         */
        protected ReusedGameModeAcquisition(@NotNull A originalAcquisition) {
            super(originalAcquisition);
        }

        @Override
        public final Player.@Nullable GameMode previous() {
            return this.originalAcquisition.previous();
        }

        @Override
        public final @NotNull Player.GameMode get() {
            return this.originalAcquisition.get();
        }
    }

    /**
     * Represents an implementation of {@linkplain ReusedGameModeAcquisition a reused game mode acquisition}
     * and {@linkplain WriteGameModeAcquisition a write game mode acquisition}.
     *
     * @since 1.0
     * @see ReusedGameModeAcquisition
     * @see WriteGameModeAcquisition
     */
    private static final class ReusedWriteGameModeAcquisition
            extends ReusedGameModeAcquisition<WriteGameModeAcquisition>
            implements WriteGameModeAcquisition {
        /**
         * Constructs the {@linkplain ReusedWriteGameModeAcquisition reused write game mode acquisition}.
         *
         * @param originalAcquisition an original acquisition should be reused
         * @since 1.0
         */
        private ReusedWriteGameModeAcquisition(@NotNull WriteGameModeAcquisition originalAcquisition) {
            super(originalAcquisition);
        }

        @Override
        public void set(Player.@NotNull GameMode value) {
            this.originalAcquisition.set(value);
        }
    }

    /**
     * Represents a common implementation of {@linkplain WriteGameModeAcquisition a write game mode acquisition}.
     *
     * @since 1.0
     * @see WriteGameModeAcquisition
     */
    private interface SetOperationImplementation extends WriteGameModeAcquisition {
        @Override
        default void set(Player.@NotNull GameMode value) {
            this.ensurePermittedAndLocked();
            Objects.requireNonNull(value, "value");

            GameModeAcquirable acquirable = this.acquirable();
            acquirable.player.sendPacket(new ServerWorldEventPlayPacket(new ChangeGameModeWorldEvent(value)));

            acquirable.previousGameMode = acquirable.gameMode;
            acquirable.gameMode = value;

            // TODO: Update abilities
        }

        /**
         * Gets {@linkplain GameModeAcquirable a game mode acquirable} that owns this acquisition.
         *
         * @return the game mode acquirable
         * @since 1.0
         */
        @NonNull GameModeAcquirable acquirable();
    }
}