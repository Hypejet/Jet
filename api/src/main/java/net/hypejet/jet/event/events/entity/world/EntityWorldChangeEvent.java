package net.hypejet.jet.event.events.entity.world;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.World;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An event called just after a {@linkplain World world} has been changed for an {@linkplain Entity entity}.
 *
 * <p>To get the new {@linkplain World world} of the {@linkplain Entity entity} use {@link Entity#world()}.</p>
 *
 * @param entity an entity that the world has been changed to
 * @param previousWorld a world that the entity was in just before the world change
 * @since 1.0
 * @see World
 */
@NullMarked
public record EntityWorldChangeEvent(Entity entity, World previousWorld) {
    /**
     * Constructs the {@linkplain EntityWorldChangeEvent entity world change event}.
     *
     * @param entity an entity that the world has been changed to
     * @param previousWorld a world that the entity was in just before the world change
     * @since 1.0
     */
    public EntityWorldChangeEvent {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(previousWorld, "previous world");
    }
}