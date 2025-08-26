package net.hypejet.jet.event.events.entity.world;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.event.events.CancellableEvent;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain CancellableEvent cancellable event} called just before
 * a {@linkplain World world} is being changed for an {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see World
 * @see Entity
 */
@NullMarked
public final class EntityPreWorldChangeEvent extends CancellableEvent {

    private final Entity entity;

    private World world;
    private Position startingPosition;

    /**
     * Constructs the {@linkplain EntityPreWorldChangeEvent entity pre-world-change event}.
     *
     * @param entity an entity that the world is being changed for
     * @param world a world that the entity should be in after the change
     * @param startingPosition a position where the entity should be right after the world change
     * @since 1.0
     */
    public EntityPreWorldChangeEvent(Entity entity, World world, Position startingPosition) {
        this.entity = Objects.requireNonNull(entity, "entity");
        this.world = Objects.requireNonNull(world, "world");
        this.startingPosition = Objects.requireNonNull(startingPosition, "starting position");
    }

    /**
     * Gets an {@linkplain Entity entity} that the world is being changed for.
     *
     * @return the entity
     * @since 1.0
     */
    public Entity entity() {
        return this.entity;
    }

    /**
     * Gets a {@linkplain World world} where the {@linkplain Entity entity} should be after the change.
     *
     * @return the world
     * @since 1.0
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Sets a {@linkplain World world} where the {@linkplain Entity entity} should be after the change.
     *
     * @param world the new world
     * @since 1.0
     */
    public void setWorld(World world) {
        this.world = Objects.requireNonNull(world, "new world");
    }

    /**
     * Gets a {@linkplain Position position} where the {@linkplain Entity entity}
     * should be right after the world change.
     *
     * @return the position
     * @since 1.0
     */
    public Position getStartingPosition() {
        return this.startingPosition;
    }

    /**
     * Sets a {@linkplain Position position} where the {@linkplain Entity entity}
     * should be right after the world change.
     *
     * @param startingPosition the new position where the entity should be after the world change
     * @since 1.0
     */
    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = Objects.requireNonNull(startingPosition, "starting position");
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityPreWorldChangeEvent event)) return false;
        return Objects.equals(this.entity, event.entity)
                && Objects.equals(this.world, event.world)
                && Objects.equals(this.startingPosition, event.startingPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.entity, this.world, this.startingPosition);
    }

    @Override
    public String toString() {
        return "PreWorldSwitchEvent{" +
                "entity=" + this.entity +
                ", world=" + this.world +
                ", startingPosition=" + this.startingPosition +
                '}';
    }
}