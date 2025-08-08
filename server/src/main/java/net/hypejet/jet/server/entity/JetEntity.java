package net.hypejet.jet.server.entity;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import java.util.Objects;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.acquisition.world.WriteEntityWorldAcquisition;
import net.hypejet.jet.entity.movement.acquisition.MovementAcquisition;
import net.hypejet.jet.server.entity.acquisition.world.EntityWorldAcquisition;
import net.hypejet.jet.server.entity.acquisition.world.WriteEntityWorldAcquisitionImpl;
import net.hypejet.jet.server.entity.movement.acquisition.InternalWriteMovementAcquisition;
import net.hypejet.jet.server.entity.movement.acquisition.MovementAcquirable;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.world.coordinate.Position;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

/**
 * Represents an implementation of {@linkplain Entity an entity}.
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

    private final MovementAcquirable movement;
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
        this.movement = new MovementAcquirable(this, Objects.requireNonNull(position, "position"));
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
    public @NonNull MovementAcquisition acquireMovementRead() {
        return this.movement.acquireRead();
    }

    @Override
    public @NonNull InternalWriteMovementAcquisition acquireMovementWrite() {
        return this.movement.acquireWrite();
    }

    @Override
    public @NonNull EntityWorldAcquisition<?> acquireWorldRead() {
        return new EntityWorldAcquisition<>(this.world.acquireRead());
    }

    @Override
    public @NonNull WriteEntityWorldAcquisition acquireWorldWrite() {
        return new WriteEntityWorldAcquisitionImpl(
                this.world.acquireWrite(), this.movement.acquireWrite(), this
        );
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
}