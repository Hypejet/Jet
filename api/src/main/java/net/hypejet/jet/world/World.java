package net.hypejet.jet.world;

import net.hypejet.jet.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a container of Minecraft {@linkplain net.hypejet.jet.data.block.Block blocks}
 * and {@linkplain net.hypejet.jet.entity.Entity entities}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.data.block.Block
 * @see net.hypejet.jet.entity.Entity
 */
public interface World {
    /**
     * Gets a {@linkplain UUID unique identifier} of the world.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Gets entities, which are in this world.
     *
     * @return the entities
     */
    @NonNull Collection<Entity> entities();
}