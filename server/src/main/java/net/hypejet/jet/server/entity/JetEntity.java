package net.hypejet.jet.server.entity;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.event.events.entity.world.EntityPreWorldChangeEvent;
import net.hypejet.jet.event.events.entity.world.EntityWorldChangeEvent;
import net.hypejet.jet.server.JetMinecraftServer;
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
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

/**
 * An implementation the {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public class JetEntity implements Entity {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetEntity.class);

    private final JetMinecraftServer server;

    private final Key entityType;
    private final int entityId;

    private final Identity identity;
    private final Pointers pointers;

    private JetWorld world;
    private Position position;
    private Vector velocity = Vector.zero();

    /**
     * Constructs the {@linkplain JetEntity entity}.
     *
     * @param entityType an identifier of a type of the entity
     * @param uniqueId a unique identifier of the entity
     * @param position an initial position that the entity should spawn at
     * @param world an initial world that the entity should spawn in
     * @param server the server that the entity should be part of
     * @since 1.0
     */
    public JetEntity(Key entityType, UUID uniqueId, Position position, JetWorld world, JetMinecraftServer server) {
        this(
                entityType, uniqueId,
                Pointers.builder()
                        .withStatic(Identity.UUID, uniqueId)
                        .build(),
                position, world, server
        );
        // TODO: Add this entity to world entity set, but only if it not a player
    }

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType an identifier of a type of the entity
     * @param uniqueId a unique identifier of the entity
     * @param pointers a pointers of the entity
     * @param position an initial position that the entity should spawn at
     * @param world an initial world that the entity should spawn in
     * @param server the server that the entity should be part of
     * @since 1.0
     */
    public JetEntity(Key entityType, UUID uniqueId, Pointers pointers,
                     Position position, JetWorld world, JetMinecraftServer server) {
        this.entityType = Objects.requireNonNull(entityType, "entity type");
        this.entityId = server.nextEntityId();
        this.identity = Identity.identity(Objects.requireNonNull(uniqueId, "unique identifier"));
        this.pointers = Objects.requireNonNull(pointers, "pointers");
        this.position = Objects.requireNonNull(position, "position");
        this.world = Objects.requireNonNull(world, "world");
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public Key entityType() {
        return this.entityType;
    }

    @Override
    public UUID uniqueId() {
        return this.identity.uuid();
    }

    @Override
    public Position position() {
        return this.position;
    }

    @Override
    public Vector velocity() {
        return this.velocity;
    }

    @Override
    public void updatePosition(Position position, Vector velocity, RelativeFlag... flags) {
        Objects.requireNonNull(flags, "relative flags");
        this.updatePosition(position, velocity, Set.of(flags));
    }

    @Override
    public void updatePosition(Position position, Vector velocity, Collection<RelativeFlag> flags) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
        Objects.requireNonNull(flags, "relative flags");
        this.updateRawPositionAndVelocity(position, velocity, flags);
    }

    @Override
    public final JetWorld world() {
        return this.world;
    }

    @Override
    public final void teleport(World world) {
        Objects.requireNonNull(world, "world");
        this.teleport(world, world.defaultSpawnPosition());
    }

    @Override
    public final void teleport(World world, Position position) {
        this.teleport(world, position, true, true);
    }

    @Override
    public final void teleport(World world, Position position, boolean keepAttributes, boolean keepMetadata) {
        // TODO: Ensure integrity with vanilla
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(position, "position");

        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The specified world is not a valid world");
        JetWorld initialWorld = this.world;

        EntityPreWorldChangeEvent preChangeEvent = new EntityPreWorldChangeEvent(this, this.world(), position);
        this.server.eventNode().call(preChangeEvent);

        if (preChangeEvent.getWorld() instanceof JetWorld validatedEventWorld) {
            validatedWorld = validatedEventWorld;
        } else {
            LOGGER.warn("An invalid world has been specified in an entity" +
                    " pre-world-change event, falling back to the initially specified world");
        }

        position = preChangeEvent.getStartingPosition();

        this.world.removeEntity(this);
        this.world = validatedWorld;

        // TODO: Handle "keepAttributes" and "keepMetadata" fields when entity system is implemented

        this.postWorldChange(initialWorld, position, keepAttributes, keepMetadata);
        this.world.addEntity(this);
        this.server.eventNode().call(new EntityWorldChangeEvent(this, initialWorld));
    }

    @Override
    public final String scoreboardName() {
        // TODO: Check entity type instead of the entity being an instance of player
        return this instanceof JetPlayer player ? player.username() : this.uniqueId().toString();
    }

    @Override
    public final JetMinecraftServer server() {
        return this.server;
    }

    @Override
    public final Identity identity() {
        return this.identity;
    }

    @Override
    public final Pointers pointers() {
        return this.pointers;
    }

    @Override
    public final Key key() {
        return this.entityType.key();
    }

    @Override
    public HoverEvent<HoverEvent.ShowEntity> asHoverEvent(UnaryOperator<HoverEvent.ShowEntity> op) {
        // TODO: Custom names
        return HoverEvent.showEntity(op.apply(HoverEvent.ShowEntity.showEntity(this.entityType, this.uniqueId())));
    }

    /**
     * Gets an identifier of this {@linkplain JetEntity entity}.
     *
     * @return the entity identifier
     * @since 1.0
     */
    public int entityId() {
        return this.entityId;
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
    public final void updateRawPositionAndVelocity(Position position, Vector velocity,
                                                   Collection<RelativeFlag> flags) {
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
    protected final void updateRawPosition(Position position) {
        this.position = Objects.requireNonNull(position, "position");
    }

    /**
     * Executes additional tasks that should be executed just after a {@linkplain JetWorld world}
     * has been changed for this {@linkplain JetEntity entity}. This also changes {@linkplain Position position}
     * of the entity to the initial position that was specified for the entity to spawn after the world change.
     *
     * @param previousWorld the previous world that the entity was in just before the world change
     * @param initialPosition a position that was specified for the entity to spawn at after the world change
     * @param keepAttributes whether it was specified to keep attributes of the entity after the world change
     * @param keepMetadata whether it was specified to keep metadata of the entity after the world change
     * @since 1.0
     */
    protected void postWorldChange(JetWorld previousWorld, Position initialPosition,
                                   boolean keepAttributes, boolean keepMetadata) {
        this.updateRawPositionAndVelocity(initialPosition, Vector.zero(), Set.of());
    }
}