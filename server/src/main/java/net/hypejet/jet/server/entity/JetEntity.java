package net.hypejet.jet.server.entity;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.movement.acquisition.MovementAcquisition;
import net.hypejet.jet.server.entity.movement.acquisition.InternalWriteMovementAcquisition;
import net.hypejet.jet.server.entity.movement.acquisition.MovementAcquirable;
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

    /**
     * Constructs the {@linkplain JetEntity entity}.
     *
     * @param entityType an identifier of a type of the entity
     * @param uniqueId a unique identifier of the entity
     * @param position an initial position that the entity should be at
     * @since 1.0
     */
    public JetEntity(@NonNull Key entityType, @NonNull UUID uniqueId, @NonNull Position position) {
        this(entityType, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, uniqueId)
                .build(), position);
    }

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType an identifier of a type of the entity
     * @param uniqueId a unique identifier of the entity
     * @param pointers a pointers of the entity
     * @param position an initial position that the entity should be at
     * @since 1.0
     */
    public JetEntity(@NonNull Key entityType, @NonNull UUID uniqueId, @NonNull Pointers pointers,
                     @NonNull Position position) {
        this.entityType = NullabilityUtil.requireNonNull(entityType, "entity type");
        this.identity = Identity.identity(NullabilityUtil.requireNonNull(uniqueId, "unique identifier"));
        this.pointers = NullabilityUtil.requireNonNull(pointers, "pointers");
        this.entityId = NEXT_ENTITY_ID.getAndIncrement();
        this.movement = new MovementAcquirable(this, position);
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