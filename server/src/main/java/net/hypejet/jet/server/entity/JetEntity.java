package net.hypejet.jet.server.entity;

import net.hypejet.jet.data.entity.type.EntityType;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.world.World;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.lock.qual.GuardedBy;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

    private final ReentrantReadWriteLock worldLock = new ReentrantReadWriteLock();
    private @MonotonicNonNull @GuardedBy("worldLock") JetWorld world;

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType a type of the entity
     * @param uniqueId an unique identifier of the entity
     * @since 1.0
     */
    public JetEntity(@NonNull EntityType entityType, @NonNull UUID uniqueId) {
        this(entityType, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, uniqueId)
                .build());
    }

    /**
     * Constructs an {@linkplain JetEntity entity}.
     *
     * @param entityType a type of the entity
     * @param uniqueId an unique identifier of the entity
     * @param pointers a pointers of the entity
     * @since 1.0
     */
    public JetEntity(@NonNull EntityType entityType, @NonNull UUID uniqueId, @NonNull Pointers pointers) {
        this.entityType = entityType;
        this.entityId = NEXT_ENTITY_ID.getAndIncrement();
        this.identity = Identity.identity(uniqueId);
        this.pointers = pointers;
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
    public @Nullable World getWorld() {
        this.worldLock.readLock().lock();
        try {
            return this.world;
        } finally {
            this.worldLock.readLock().unlock();
        }
    }

    @Override
    public void setWorld(@NonNull World world) {
        Objects.requireNonNull(world, "The world must not be null");

        if (!(world instanceof JetWorld jetWorld))
            throw new IllegalArgumentException("The world is not a valid jet world");

        this.worldLock.writeLock().lock();

        try {
            if (this.world != null)
                this.world.removeEntity(this);

            this.world = jetWorld;
            jetWorld.addEntity(this);
        } finally {
            this.worldLock.writeLock().unlock();
        }
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