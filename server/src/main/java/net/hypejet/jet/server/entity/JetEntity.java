package net.hypejet.jet.server.entity;

import it.unimi.dsi.fastutil.Pair;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
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

    private @NonNull JetWorld world;
    private @NonNull Position position;
    private @NonNull Vector velocity = Vector.zero();

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
        this.world = Objects.requireNonNull(world, "world");
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
        Objects.requireNonNull(flags, "relative flags");
        this.updatePosition(position, velocity, Set.of(flags));
    }

    @Override
    public void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                               @NonNull Collection<RelativeFlag> flags) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
        Objects.requireNonNull(flags, "relative flags");
        this.updateRawPositionAndVelocity(position, velocity, flags);
    }

    @Override
    public final @NonNull JetWorld world() {
        return this.world;
    }

    @Override
    public final void teleport(@NonNull World world) {
        Objects.requireNonNull(world, "world");
        this.teleport(world, world.defaultSpawnPosition());
    }

    @Override
    public final void teleport(@NonNull World world, @NonNull Position position) {
        this.teleport(world, position, true, true);
    }

    @Override
    public final void teleport(@NonNull World world, @NonNull Position position,
                               boolean keepAttributes, boolean keepMetadata) {
        // TODO: Ensure integrity with vanilla
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(position, "position");

        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The specified world is not a valid world");
        JetWorld initialWorld = this.world;

        Pair<JetWorld, Position> finalTeleportData = this.preWorldChange(validatedWorld, position);
        position = finalTeleportData.second();
        this.world = finalTeleportData.first();

        // TODO: Handle "keepAttributes" and "keepMetadata" fields when entity system is implemented

        this.postWorldChange(initialWorld, position, keepAttributes, keepMetadata);
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
     * Updates a {@linkplain Position position} and a velocity {@linkplain Vector vector}
     * of this {@linkplain JetEntity entity} without sending any updates to clients.
     *
     * @param position the position that the entity should have
     * @param velocity the velocity that the entity should have
     * @param flags the relative flags specifying which values of the specified position
     *              and velocity should be recognised as relative to current ones
     * @since 1.0
     */
    public final void updateRawPositionAndVelocity(@NonNull Position position, @NonNull Vector velocity,
                                                   @NonNull Collection<RelativeFlag> flags) {
        float initialYaw = this.position.yaw();
        float initialPitch = this.position.pitch();

        this.updateRawPosition(Position.create(
                position.x() + (flags.contains(RelativeFlag.X) ? this.position.x() : 0D),
                position.y() + (flags.contains(RelativeFlag.Y) ? this.position.y() : 0D),
                position.z() + (flags.contains(RelativeFlag.Z) ? this.position.z() : 0D),
                initialYaw + (flags.contains(RelativeFlag.YAW) ? this.position.yaw() : 0f),
                initialPitch + (flags.contains(RelativeFlag.PITCH) ? this.position.pitch() : 0f)
        ));

        if (flags.contains(RelativeFlag.ROTATE_VELOCITY)) {
            float pitchRotationAngle = (float) Math.toRadians(initialPitch - this.position.pitch());
            float yawRotationAngle = (float) Math.toRadians(initialYaw - this.position.yaw());
            this.velocity = this.velocity.rotateAroundX(pitchRotationAngle).rotateAroundY(yawRotationAngle);
        }

        this.velocity = velocity.add(
                flags.contains(RelativeFlag.VELOCITY_X) ? this.velocity.x() : 0D,
                flags.contains(RelativeFlag.VELOCITY_Y) ? this.velocity.y() : 0D,
                flags.contains(RelativeFlag.VELOCITY_Z) ? this.velocity.z() : 0D
        );
    }

    /**
     * Updates a {@linkplain Position position} of this {@linkplain JetEntity entity}
     * without sending any updates to clients.
     *
     * @param position the position that the entity should have
     * @since 1.0
     */
    protected final void updateRawPosition(@NonNull Position position) {
        this.position = Objects.requireNonNull(position, "position");
    }

    /**
     * Executes additional tasks that should be executed just before this {@linkplain JetEntity entity}
     * switches to a different {@linkplain JetWorld world}. This also gets a final {@linkplain JetWorld world}
     * and a final {@linkplain Position position} where the entity should spawn at.
     *
     * @param newWorld the world that the entity is being teleported to
     * @param initialPosition a position where the entity should spawn at after the world change
     * @return a pair containing the final world that the entity should be teleported to
     *         and the final position where the entity should spawn at after the world change
     * @since 1.0
     */
    protected @NonNull Pair<JetWorld, Position> preWorldChange(@NonNull JetWorld newWorld,
                                                               @NonNull Position initialPosition) {
        return Pair.of(newWorld, initialPosition);
    }

    /**
     * Executes additional tasks that should be executed just after this {@linkplain JetEntity entity}
     * switches to a different {@linkplain JetWorld world}. This also changes {@linkplain Position position}
     * of the entity to the initial position that was specified for the entity to spawn after the world change.
     *
     * @param previousWorld the previous world that the entity was in just before the world change
     * @param initialPosition a position that was specified for the entity to spawn at after the world change
     * @param keepAttributes whether it was specified to keep attributes of the entity after the world change
     * @param keepMetadata whether it was specified to keep metadata of the entity after the world change
     * @since 1.0
     */
    protected void postWorldChange(@NonNull JetWorld previousWorld, @NonNull Position initialPosition,
                                   boolean keepAttributes, boolean keepMetadata) {
        this.updateRawPositionAndVelocity(initialPosition, Vector.zero(), Set.of());
    }
}