package net.hypejet.jet.server.entity;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.component.EntityDataComponentRegistration;
import net.hypejet.jet.server.entity.component.EntityDataComponentRegistry;
import net.hypejet.jet.server.entity.metadata.EntityMetadata;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerRemoveEntitiesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSpawnEntityPlayPacket;
import net.hypejet.jet.server.util.game.entity.EntityTypePredicate;
import net.hypejet.jet.server.util.viewable.JetViewable;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.event.HoverEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

/**
 * An implementation the {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public class JetEntity implements Entity, JetViewable {

    private final JetMinecraftServer server;

    private final Holder.Reference<EntityType> entityType;
    private final int entityId;
    private final EntityMetadata entityMetadata;

    private final Identity identity;
    private final Pointers pointers;

    private final Set<JetPlayer> viewers = ConcurrentHashMap.newKeySet();

    private Position position;
    private Vector velocity = Vector.zero();

    /**
     * Constructs the {@linkplain JetEntity entity}.
     *
     * @param entityType a holder referencing to an entity type of which the entity should be
     * @param uniqueId a unique identifier of the entity
     * @param position an initial position that the entity should spawn at
     * @param server the server that the entity should be part of
     * @since 1.0
     */
    public JetEntity(Holder.Reference<EntityType> entityType, UUID uniqueId,
                     Position position, JetMinecraftServer server) {
        this(
                entityType, uniqueId,
                Pointers.builder()
                        .withStatic(Identity.UUID, uniqueId)
                        .build(),
                position, server
        );
    }

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType a holder referencing to an entity type of which the entity should be
     * @param uniqueId a unique identifier of the entity
     * @param pointers a pointers of the entity
     * @param position an initial position that the entity should spawn at
     * @param server the server that the entity should be part of
     * @since 1.0
     */
    public JetEntity(Holder.Reference<EntityType> entityType, UUID uniqueId,
                     Pointers pointers, Position position, JetMinecraftServer server) {
        this.entityType = Objects.requireNonNull(entityType, "entity type");
        this.entityId = server.nextEntityId();
        this.entityMetadata = new EntityMetadata(server, entityType, this.entityId, this::sendPacketToViewersAndSelf);
        this.identity = Identity.identity(Objects.requireNonNull(uniqueId, "unique identifier"));
        this.pointers = Objects.requireNonNull(pointers, "pointers");
        this.position = Objects.requireNonNull(position, "position");
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public Holder.Reference<EntityType> entityType() {
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
    public <V> @Nullable V component(EntityDataComponent<V> component) {
        V value = this.component(this.ensureComponentSupported(component));
        if (!component.nullable() && value == null) {
            throw new IllegalStateException(String.format(
                    "Entity data component \"%s\" is not nullable, but its value is null",
                    component.name()
            ));
        }
        return value;
    }

    @Override
    public <V> void component(EntityDataComponent<V> component, @Nullable V value) {
        this.server.ticker().ensureRunsInTickLoop();
        if (!component.nullable() && value == null) {
            throw new IllegalArgumentException(String.format(
                    "Entity data component \"%s\" cannot take null values",
                    component.name()
            ));
        }
        this.component(this.ensureComponentSupported(component), value);
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
    public final Set<JetPlayer> viewers() {
        return Set.copyOf(this.viewers);
    }

    @Override
    public final boolean addViewer(Player player) {
        Objects.requireNonNull(player, "player");
        JetPlayer castPlayer = JetPlayer.cast(player);
        this.server.ticker().ensureRunsInTickLoop();

        if (!this.viewers.add(castPlayer))
            return false;
        castPlayer.addViewedObject(this);

        List<ServerPacket> packetBundle = new ArrayList<>();
        packetBundle.add(this.spawnPacket());

        ServerPacket metadataInitializationPacket = this.entityMetadata.createInitializationPacket();
        if (metadataInitializationPacket != null)
            packetBundle.add(metadataInitializationPacket);

        castPlayer.connection().sendPacketBundle(packetBundle.toArray(ServerPacket[]::new));
        return true;
    }

    @Override
    public final boolean removeViewer(Player player) {
        Objects.requireNonNull(player, "player");
        JetPlayer castPlayer = JetPlayer.cast(player);
        this.server.ticker().ensureRunsInTickLoop();

        if (!this.viewers.remove(castPlayer))
            return false;

        castPlayer.removeViewedObject(this);
        castPlayer.sendPacket(new ServerRemoveEntitiesPlayPacket(this.entityId));
        return true;
    }

    @Override
    public final void handleViewerRemoval(JetPlayer player) {
        this.viewers.remove(player);
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
        return HoverEvent.showEntity(op.apply(HoverEvent.ShowEntity.showEntity(
                this.entityType.key(),
                this.uniqueId()
        )));
    }

    /**
     * Gets an identifier of this {@linkplain JetEntity entity}.
     *
     * @return the entity identifier
     * @since 1.0
     */
    public final int entityId() {
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

    private ServerSpawnEntityPlayPacket spawnPacket() {
        // TODO: Cache
        return new ServerSpawnEntityPlayPacket(
                this.entityId,
                this.uniqueId(),
                this.server.registryManager()
                        .registry(RegistryReference.ENTITY_TYPE)
                        .indexOf(this.entityType),
                this.position,
                0f /* TODO */,
                0 /* TODO */,
                this.velocity
        );
    }

    private <V, MV extends EntityMetadataValue> @Nullable V component(
            EntityDataComponentRegistration<V, MV> registration
    ) {
        MV metadataValue = this.entityMetadata.value(registration.metadataIndex(), registration.metadataValueClass());
        return registration.componentValueDecoder().decode(metadataValue);
    }

    private <V, MV extends EntityMetadataValue> void component(
            EntityDataComponentRegistration<V, MV> registration,
            @Nullable V value
    ) {
        this.entityMetadata.value(
                registration.metadataIndex(), registration.metadataValueClass(),
                currentMetadataValue -> registration.componentValueEncoder().encode(currentMetadataValue, value)
        );
    }

    private <V> EntityDataComponentRegistration<V, ?> ensureComponentSupported(EntityDataComponent<V> component) {
        Objects.requireNonNull(component, "component");
        EntityDataComponentRegistration<V, ?> registration = EntityDataComponentRegistry.registration(component);

        if (!EntityTypePredicate.test(registration.entityTypePredicate(), this.entityType, this.server)) {
            throw new IllegalArgumentException(String.format(
                    "Entity data component \"%s\" is unsupported by \"%s\" entity types",
                    component.name(), this.entityType.key()
            ));
        }

        return registration;
    }

    private void sendPacketToViewersAndSelf(ServerPacket packet) {
        // TODO: Cache the packet
        if (this instanceof JetPlayer player)
            player.sendPacket(packet);
        this.viewers.forEach(player -> player.sendPacket(packet));
    }

    /**
     * Cast the specified {@linkplain Entity entity} to the {@linkplain JetEntity entity implementation}.
     * Throws a detailed exception if the specified {@linkplain Entity entity} does not use the correct implementation.
     *
     * @param entity the entity to cast
     * @return the entity cast to the implementation
     * @throws IllegalArgumentException if the specified entity uses an invalid implementation
     * @since 1.0
     */
    public static JetEntity cast(Entity entity) {
        if (!(entity instanceof JetEntity castEntity))
            throw new IllegalArgumentException("The specified entity is not a valid entity");
        return castEntity;
    }
}