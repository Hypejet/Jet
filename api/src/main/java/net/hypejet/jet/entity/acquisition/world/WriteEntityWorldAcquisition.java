package net.hypejet.jet.entity.acquisition.world;

import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain WriteNotNullObjectAcquisition a write not-null object acquisition}, which allows
 * setting a guarded {@linkplain World world} of {@linkplain net.hypejet.jet.entity.Entity an entity}.
 *
 * @since 1.0
 * @see WriteNotNullObjectAcquisition
 * @see World
 * @see net.hypejet.jet.entity.Entity
 */
public interface WriteEntityWorldAcquisition extends WriteNotNullObjectAcquisition<World> {
    /**
     * Changes {@linkplain World a world} for {@linkplain net.hypejet.jet.entity.Entity an entity} associated
     * with this acquisition and keeps their attributes and metadata after the world change. An initial position
     * after the change is a default spawn position of the world.
     *
     * @param value a world that the entity should be in
     * @since 1.0
     */
    @Override
    void set(@NotNull World value);

    /**
     * Changes {@linkplain World a world} for {@linkplain net.hypejet.jet.entity.Entity an entity} associated
     * with this acquisition and keeps their attributes and metadata after the world change.
     *
     * @param world a world that the entity should be in
     * @param position an initial position that the entity should spawn at
     * @since 1.0
     */
    void set(@NonNull World world, @NonNull Position position);

    /**
     * Changes {@linkplain World a world} for {@linkplain net.hypejet.jet.entity.Entity an entity} associated
     * with this acquisition.
     *
     * @param world a world that the entity should be in
     * @param position an initial position that the entity should spawn at
     * @param keepAttributes whether attributes of the entity should be kept after the world change
     * @param keepMetadata whether metadata of the entity should be kept after the world change
     * @since 1.0
     */
    void set(@NonNull World world, @NonNull Position position, boolean keepAttributes, boolean keepMetadata);
}