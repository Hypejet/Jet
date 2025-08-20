package net.hypejet.jet.server.entity;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.acquisition.world.WriteEntityWorldAcquisition;
import net.hypejet.jet.entity.movement.flag.RelativeFlag;
import net.hypejet.jet.server.entity.acquisition.world.EntityWorldAcquisition;
import net.hypejet.jet.server.entity.acquisition.world.WriteEntityWorldAcquisitionImpl;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

/**
 * An implementation the {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
public class JetEntity implements Entity {

    private static final AtomicInteger NEXT_ENTITY_ID = new AtomicInteger(); // FIXME: Not the best solution

    private final Key entityType;
    private final int entityId;

    private final Identity identity;
    private final Pointers pointers;

    private @NonNull Position position;
    private @NonNull Vector velocity = Vector.zero();

    private final NotNullObjectAcquirable<JetWorld> world;

    /**
     * Constructs the {@linkplain JetEntity entity}.
     *
     * @param entityType an identifier of a type of the entity
     * @param uniqueId a unique identifier of the entity
     * @param position an initial position that the entity should spawn at
     * @param world an initial world that the entity should spawn in
     * @since 1.0
     */
    public JetEntity(@NonNull Key entityType, @NonNull UUID uniqueId,
                     @NonNull Position position, @NonNull JetWorld world) {
        this(
                entityType, uniqueId,
                Pointers.builder()
                        .withStatic(Identity.UUID, uniqueId)
                        .build(),
                position, world
        );
    }

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType an identifier of a type of the entity
     * @param uniqueId a unique identifier of the entity
     * @param pointers a pointers of the entity
     * @param position an initial position that the entity should spawn at
     * @param world an initial world that the entity should spawn in
     * @since 1.0
     */
    public JetEntity(@NonNull Key entityType, @NonNull UUID uniqueId, @NonNull Pointers pointers,
                     @NonNull Position position, @NonNull JetWorld world) {
        this.entityType = Objects.requireNonNull(entityType, "entity type");
        this.identity = Identity.identity(Objects.requireNonNull(uniqueId, "unique identifier"));
        this.pointers = Objects.requireNonNull(pointers, "pointers");
        this.entityId = NEXT_ENTITY_ID.getAndIncrement();
        this.position = Objects.requireNonNull(position, "position");
        this.world = new NotNullObjectAcquirable<>(Objects.requireNonNull(world, "world"));
    }

    @Override
    public @NonNull Key entityType() {
        return this.entityType;
    }

    @Override
    public int entityId() {
        return this.entityId;
    }

    @Override
    public @NonNull UUID uniqueId() {
        return this.identity.uuid();
    }

    @Override
    public @NonNull Position position() {
        return this.position;
    }

    @Override
    public @NonNull Vector velocity() {
        return this.velocity;
    }

    @Override
    public void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                               @NonNull RelativeFlag @NonNull ... flags) {
        this.updatePosition(position, velocity, Set.of(flags));
    }

    @Override
    public void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                               @NonNull Collection<RelativeFlag> flags) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
        Objects.requireNonNull(flags, "relative flags");

        float initialYaw = this.position.yaw();
        float initialPitch = this.position.pitch();

        this.position = Position.create(
                position.x() + (flags.contains(RelativeFlag.X) ? this.position.x() : 0D),
                position.y() + (flags.contains(RelativeFlag.Y) ? this.position.y() : 0D),
                position.z() + (flags.contains(RelativeFlag.Z) ? this.position.z() : 0D),
                initialYaw + (flags.contains(RelativeFlag.YAW) ? this.position.yaw() : 0f),
                initialPitch + (flags.contains(RelativeFlag.PITCH) ? this.position.pitch() : 0f)
        );

        if (flags.contains(RelativeFlag.ROTATE_DELTA)) {
            float pitchRotationAngle = (float) Math.toRadians(initialPitch - this.position.pitch());
            float yawRotationAngle = (float) Math.toRadians(initialYaw - this.position.yaw());
            this.velocity = this.velocity.rotateAroundX(pitchRotationAngle).rotateAroundY(yawRotationAngle);
        }

        this.velocity = velocity.add(
                flags.contains(RelativeFlag.DELTA_X) ? this.velocity.x() : 0D,
                flags.contains(RelativeFlag.DELTA_Y) ? this.velocity.y() : 0D,
                flags.contains(RelativeFlag.DELTA_Z) ? this.velocity.z() : 0D
        );

        // TODO: Send update to viewers

        if (this instanceof JetPlayer player) {
            player.movementHandler().synchronize(position, velocity, flags);
        }
    }

    @Override
    public @NonNull EntityWorldAcquisition<?> acquireWorldRead() {
        return new EntityWorldAcquisition<>(this.world.acquireRead());
    }

    @Override
    public @NonNull WriteEntityWorldAcquisition acquireWorldWrite() {
        return new WriteEntityWorldAcquisitionImpl(this.world.acquireWrite(), this);
    }

    @Override
    public final @NonNull String scoreboardName() {
        // TODO: Check entity type instead of the entity being an instance of player
        return this instanceof JetPlayer player ? player.username() : this.uniqueId().toString();
    }

    @Override
    public @NonNull Identity identity() {
        return this.identity;
    }

    @Override
    public @NonNull Pointers pointers() {
        return this.pointers;
    }

    @Override
    public @NonNull Key key() {
        return this.entityType.key();
    }

    @Override
    public @NonNull HoverEvent<HoverEvent.ShowEntity> asHoverEvent(@NonNull UnaryOperator<HoverEvent.ShowEntity> op) {
        // TODO: Custom names
        return HoverEvent.showEntity(op.apply(HoverEvent.ShowEntity.showEntity(this.entityType, this.uniqueId())));
    }

    /**
     * Sets a {@linkplain Position position} of this {@linkplain JetEntity entity}
     * without sending any updates to clients.
     *
     * @param position the position where the entity should be
     * @since 1.0
     */
    public void setPosition(@NonNull Position position) {
        this.position = Objects.requireNonNull(position, "position");
    }
}