package net.hypejet.jet.entity;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.coordinate.Position;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

/**
 * Something managing creation of {@linkplain Entity entities}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface EntityManager {
    /**
     * Creates an {@linkplain Entity entity}.
     *
     * <p>The entity is not tracked or registered by default by the server, therefore
     * it exists in memory for as long as any {@linkplain Player player} is a viewer of it,
     * or it is tracked by {@linkplain Plugin plugins}</p>
     *
     * <p>The initial {@linkplain Position position} of the {@linkplain Entity entity}
     * is going to be {@link Position#zero()} and that {@linkplain Entity entity} is going
     * to have a randomly generated {@linkplain UUID unique identifier}.</p>
     *
     * @param entityType a holder referencing to an entity type of which the entity should be
     * @return the created entity
     * @since 1.0
     */
    Entity createEntity(Holder.Reference<EntityType> entityType);

    /**
     * Creates an {@linkplain Entity entity}.
     *
     * <p>The entity is not tracked or registered by default by the server, therefore
     * it exists in memory for as long as any {@linkplain Player player} is a viewer of it,
     * or it is tracked by {@linkplain Plugin plugins}</p>
     *
     * <p>The initial {@linkplain Position position} of the {@linkplain Entity entity}
     * is going to be {@link Position#zero()}.</p>
     *
     * @param entityType a holder referencing to an entity type of which the entity should be
     * @param uniqueId a unique identifier that the entity should have
     * @return the created entity
     * @since 1.0
     */
    Entity createEntity(Holder.Reference<EntityType> entityType, UUID uniqueId);

    /**
     * Creates an {@linkplain Entity entity}.
     *
     * <p>The entity is not tracked or registered by default by the server, therefore
     * it exists in memory for as long as any {@linkplain Player player} is a viewer of it,
     * or it is tracked by {@linkplain Plugin plugins}</p>
     *
     * @param entityType a holder referencing to an entity type of which the entity should be
     * @param uniqueId a unique identifier that the entity should have
     * @param position an initial position that the entity should be at
     * @return the created entity
     * @since 1.0
     */
    Entity createEntity(Holder.Reference<EntityType> entityType, UUID uniqueId, Position position);
}