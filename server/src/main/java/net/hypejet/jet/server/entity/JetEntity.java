package net.hypejet.jet.server.entity;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.EntityType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

/**
 * Represents an implementation of {@linkplain Entity entity}.
 *
 * @since 1.0
 * @author Codestecj
 * @see Entity
 */
public class JetEntity implements Entity {

    private static final AtomicInteger NEXT_ENTITY_ID = new AtomicInteger();

    private final EntityType entityType;
    private final int entityId;

    private final Identity identity;
    private final Pointers pointers;

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType a type of the entity
     * @param uniqueId an unique identifier of the entity
     * @since 1.0
     */
    public JetEntity(@NonNull EntityType entityType, @NonNull UUID uniqueId) {
        this.entityType = entityType;
        this.entityId = NEXT_ENTITY_ID.getAndIncrement();

        this.identity = Identity.identity(uniqueId);

        Pointers.Builder pointersBuilder = Pointers.builder();
        pointersBuilder.withStatic(Identity.UUID, uniqueId);
        this.applyPointers(pointersBuilder);

        this.pointers = pointersBuilder.build();
    }

    @Override
    public @NonNull EntityType entityType() {
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
     * Applies additional pointers of the entity to a {@linkplain Pointers.Builder pointers builder}.
     *
     * @param builder the builder
     * @since 1.0
     */
    public void applyPointers(Pointers.@NonNull Builder builder) {}
}