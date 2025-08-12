package net.hypejet.jet.server.entity.movement.acquisition;

import net.hypejet.concurrency.Acquirable;
import net.hypejet.concurrency.Acquisition;
import net.hypejet.jet.entity.movement.acquisition.MovementAcquisition;
import net.hypejet.jet.entity.movement.flag.RelativeFlag;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.util.collection.SetUtil;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents {@linkplain Acquirable an acquirable}, which guards {@linkplain Position a position}
 * and {@linkplain Vector a vector} of delta movement of {@linkplain net.hypejet.jet.entity.Entity an entity}.
 *
 * @since 1.0
 * @see Position
 * @see Vector
 * @see net.hypejet.jet.entity.Entity
 */
public final class MovementAcquirable extends Acquirable<MovementAcquisition, InternalWriteMovementAcquisition> {

    private static final double MINIMUM_HORIZONTAL_VALUE = -3.0E7;
    private static final double MAXIMUM_HORIZONTAL_VALUE = 3.0E7;

    private final JetEntity entity;

    private @NonNull Position position;
    private @NonNull Vector deltaMovement = new Vector(0, 0, 0);

    /**
     * Constructs the {@linkplain MovementAcquirable movement acquirable}.
     *
     * @param entity an entity that the position and delta movement should be guarded for
     * @param initialPosition an initial position that the entity should be at
     * @since 1.0
     */
    public MovementAcquirable(@NonNull JetEntity entity, @NonNull Position initialPosition) {
        this.entity = Objects.requireNonNull(entity, "entity");
        this.position = Objects.requireNonNull(initialPosition, "initial position");
    }

    @Override
    protected @NotNull MovementAcquisition createReadAcquisition() {
        return new MovementAcquisitionImpl(this, Acquisition.AcquisitionType.READ);
    }

    @Override
    protected @NotNull InternalWriteMovementAcquisition createWriteAcquisition() {
        return new WriteMovementAcquisitionImpl(this);
    }

    @Override
    protected @NotNull MovementAcquisition reuseReadAcquisition(@NotNull MovementAcquisition originalAcquisition) {
        return new ReusedMovementAcquisition<>(originalAcquisition);
    }

    @Override
    protected @NotNull InternalWriteMovementAcquisition reuseWriteAcquisition(
            @NotNull InternalWriteMovementAcquisition originalAcquisition
    ) {
        return new WriteReusedMovementAcquisition(originalAcquisition);
    }

    @Override
    protected @NotNull InternalWriteMovementAcquisition createUpgradedAcquisition(
            @NotNull MovementAcquisition originalAcquisition
    ) {
        return new UpgradedMovementAcquisition(originalAcquisition, this);
    }

    @Override
    protected @Nullable InternalWriteMovementAcquisition castToWriteAcquisition(
            @NotNull MovementAcquisition acquisition
    ) {
        if (acquisition instanceof InternalWriteMovementAcquisition writeAcquisition)
            return writeAcquisition;
        return null;
    }

    /**
     * Represents an implementation of {@linkplain AbstractAcquisition an abstract acquisition}
     * and {@linkplain MovementAcquisition a movement acquisition}.
     *
     * @since 1.0
     * @see AbstractAcquisition
     * @see MovementAcquisition
     */
    private static class MovementAcquisitionImpl extends AbstractAcquisition<MovementAcquisition, MovementAcquirable>
            implements ReadImplementation {
        /**
         * Constructs the {@linkplain MovementAcquisitionImpl movement acquisition implementation}.
         *
         * @param acquirable an acquirable whose state is guarded by the lock
         * @param type a type, of which the acquisition should be
         * @since 1.0
         */
        private MovementAcquisitionImpl(@NotNull MovementAcquirable acquirable, @NotNull AcquisitionType type) {
            super(acquirable, type);
        }

        @Override
        public @NonNull MovementAcquirable acquirable() {
            return this.acquirable;
        }

        @Override
        protected @NotNull MovementAcquisition cast() {
            return this;
        }
    }

    /**
     * Represents an implementation of {@linkplain MovementAcquisitionImpl a movement acquisition implementation}
     * and {@linkplain InternalWriteMovementAcquisition an internal write movement acquisition}.
     *
     * @since 1.0
     * @see MovementAcquisitionImpl
     * @see InternalWriteMovementAcquisition
     */
    private static final class WriteMovementAcquisitionImpl extends MovementAcquisitionImpl
            implements WriteImplementation {
        /**
         * Constructs the {@linkplain WriteMovementAcquisitionImpl write movement acquisition implementation}.
         *
         * @param acquirable an acquirable whose state is guarded by the lock
         * @since 1.0
         */
        private WriteMovementAcquisitionImpl(@NotNull MovementAcquirable acquirable) {
            super(acquirable, AcquisitionType.WRITE);
        }
    }

    /**
     * Represents an implementation of {@linkplain UpgradedAcquisition an upgraded acquisition}
     * and {@linkplain InternalWriteMovementAcquisition an internal write movement acquisition}.
     *
     * @since 1.0
     * @see UpgradedAcquisition
     * @see InternalWriteMovementAcquisition
     */
    private static final class UpgradedMovementAcquisition
            extends UpgradedAcquisition<MovementAcquisition, MovementAcquirable>
            implements WriteImplementation {
        /**
         * Constructs the {@linkplain UpgradedMovementAcquisition upgraded movement acquisition}.
         *
         * @param originalAcquisition an original acquisition that should be reused
         * @param acquirable an acquirable, which owns the acquisition that should be reused
         * @since 1.0
         */
        private UpgradedMovementAcquisition(@NotNull MovementAcquisition originalAcquisition,
                                            @NotNull MovementAcquirable acquirable) {
            super(originalAcquisition, acquirable);
        }

        @Override
        public @NonNull MovementAcquirable acquirable() {
            return this.acquirable;
        }
    }

    /**
     * Represents an implementation of {@linkplain ReusedAcquisition a reused acquisition}
     * and {@linkplain MovementAcquisition a movement acquisition}.
     *
     * @param <A> a type of movement acquisition that is being reused
     * @since 1.0
     * @see ReusedAcquisition
     * @see MovementAcquisition
     */
    private static class ReusedMovementAcquisition<A extends MovementAcquisition> extends ReusedAcquisition<A>
            implements MovementAcquisition {
        /**
         * Constructs the {@linkplain ReusedMovementAcquisition reused movement acquisition}.
         *
         * @param originalAcquisition an original acquisition should be reused
         * @since 1.0
         */
        protected ReusedMovementAcquisition(@NotNull A originalAcquisition) {
            super(originalAcquisition);
        }

        @Override
        public @NonNull Position position() {
            return this.originalAcquisition.position();
        }

        @Override
        public @NonNull Vector deltaMovement() {
            return this.originalAcquisition.deltaMovement();
        }
    }

    /**
     * Represents an implementation of {@linkplain ReusedMovementAcquisition a reused movement acquisition}
     * and {@linkplain InternalWriteMovementAcquisition an internal write movement acquisition}.
     *
     * @since 1.0
     * @see ReusedMovementAcquisition
     * @see InternalWriteMovementAcquisition
     */
    private static final class WriteReusedMovementAcquisition
            extends ReusedMovementAcquisition<InternalWriteMovementAcquisition>
            implements InternalWriteMovementAcquisition {
        /**
         * Constructs the {@linkplain WriteReusedMovementAcquisition write reused movement acquisition}.
         *
         * @param originalAcquisition an original acquisition should be reused
         * @since 1.0
         */
        private WriteReusedMovementAcquisition(@NotNull InternalWriteMovementAcquisition originalAcquisition) {
            super(originalAcquisition);
        }

        @Override
        public void setView(float yaw, float pitch) {
            this.originalAcquisition.setView(yaw, pitch);
        }

        @Override
        public void teleport(@NonNull Position position) {
            this.originalAcquisition.teleport(position);
        }

        @Override
        public void update(@NonNull Position position, @NonNull Vector deltaMovement,
                           @NonNull RelativeFlag @NonNull ... flags) {
            this.originalAcquisition.update(position, deltaMovement, flags);
        }

        @Override
        public void update(@NonNull Position position, @NonNull Vector deltaMovement,
                           @NonNull Collection<RelativeFlag> flags) {
            this.originalAcquisition.update(position, deltaMovement, flags);
        }

        @Override
        public void setPosition(@NonNull Position position) {
            this.originalAcquisition.setPosition(position);
        }
    }

    /**
     * Represents a common implementation of {@linkplain MovementAcquisition a movement acquisition}.
     *
     * @since 1.0
     * @see MovementAcquisition
     */
    private interface ReadImplementation extends MovementAcquisition {
        @Override
        default @NonNull Position position() {
            this.ensurePermittedAndLocked();
            return this.acquirable().position;
        }

        @Override
        default @NonNull Vector deltaMovement() {
            this.ensurePermittedAndLocked();
            return this.acquirable().deltaMovement;
        }

        /**
         * Gets {@linkplain MovementAcquirable a movement acquirable} that owns this acquisition.
         *
         * @return the movement acquirable
         * @since 1.0
         */
        @NonNull MovementAcquirable acquirable();
    }

    /**
     * Represents a common implementation of
     * {@linkplain InternalWriteMovementAcquisition an internal write movement acquisition}.
     *
     * @since 1.0
     * @see InternalWriteMovementAcquisition
     */
    private interface WriteImplementation extends ReadImplementation, InternalWriteMovementAcquisition {
        @Override
        default void setView(float yaw, float pitch) {
            this.update(
                    new Position(0, 0, 0, yaw, pitch),
                    new Vector(0, 0, 0),
                    SetUtil.subtract(RelativeFlag.ALL, RelativeFlag.VIEW)
            );
        }

        @Override
        default void teleport(@NonNull Position position) {
            this.update(position, this.deltaMovement(), Set.of());
        }

        @Override
        default void update(@NonNull Position position, @NonNull Vector deltaMovement,
                            @NonNull RelativeFlag @NonNull ... flags) {
            this.update(position, deltaMovement, Set.of(flags));
        }

        @Override
        default void update(@NonNull Position position, @NonNull Vector deltaMovement,
                            @NonNull Collection<RelativeFlag> flags) {
            this.update(position, deltaMovement, flags, true);
        }

        @Override
        default void setPosition(@NonNull Position position) {
            this.update(position, Vector.zero(), SetUtil.subtract(RelativeFlag.ALL, RelativeFlag.POSITION), false);
        }

        private void update(@NonNull Position position, @NonNull Vector deltaMovement,
                            @NonNull Collection<RelativeFlag> flags, boolean synchronize) {
            Objects.requireNonNull(position, "position");
            Objects.requireNonNull(deltaMovement, "delta movement");
            Objects.requireNonNull(flags, "flags");

            this.ensurePermittedAndLocked();
            MovementAcquirable acquirable = this.acquirable();

            position = clampPosition(position);
            if (position.equals(acquirable.position) && deltaMovement.equals(acquirable.deltaMovement)) return;

            acquirable.position = position;
            acquirable.deltaMovement = deltaMovement;

            // TODO: Send updates to viewers

            if (acquirable.entity instanceof JetPlayer player) {
                player.chunkBatchHandler().handlePositionUpdate(position);
                if (!synchronize) return;
                player.movementHandler().synchronize(this.position(), deltaMovement, flags);
            }
        }

        private static @NonNull Position clampPosition(@NonNull Position position) {
            double clampedX = clampHorizontalPositionValue(position.x());
            double clampedZ = clampHorizontalPositionValue(position.z());

            if (clampedX == position.x() && clampedZ == position.z())
                return position;

            return new Position(clampedX, position.y(), clampedZ, position.yaw(), position.pitch());
        }

        private static double clampHorizontalPositionValue(double value) {
            return Math.clamp(value, MINIMUM_HORIZONTAL_VALUE, MAXIMUM_HORIZONTAL_VALUE);
        }
    }
}