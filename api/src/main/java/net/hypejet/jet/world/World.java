package net.hypejet.jet.world;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.acquisition.worldmap.WorldMapAcquisition;
import net.hypejet.jet.world.acquisition.worldmap.WriteWorldMapAcquisition;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.data.WorldData;
import net.hypejet.jet.world.dimension.DimensionType;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft world.
 *
 * @since 1.0
 */
public interface World {
    /**
     * Gets a {@linkplain Holder.Reference holder referencing to}
     * a {@link DimensionType dimension type} that this world uses.
     *
     * @return the holder reference
     * @since 1.0
     */
    Holder.@NonNull Reference<DimensionType> dimensionType();

    /**
     * Gets an additional {@linkplain WorldData world data} of this world.
     *
     * @return the world data
     * @since 1.0
     */
    @NonNull WorldData worldData();

    /**
     * Gets the default {@linkplain Position position} where {@linkplain Entity entities} should spawn
     * when teleporting to this {@linkplain World world} without initial position explicitly set.
     *
     * @return the default spawn position
     * @since 1.0
     */
    @NonNull Position defaultSpawnPosition();

    /**
     * Sets the default {@linkplain Position position} where {@linkplain Entity entities} should spawn
     * when teleporting to this {@linkplain World world} without initial position explicitly set.
     *
     * @param position the default spawn position that the world should have
     * @since 1.0
     */
    void defaultSpawnPosition(@NonNull Position position);

    /**
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of {@linkplain Entity entities} which
     * are in this world.
     *
     * @return the collection acquisition
     * @since 1.0
     */
    @NonNull CollectionAcquisition<? extends Entity, ?> entities();

    /**
     * Creates {@linkplain WorldMapAcquisition a world-map acquisition} of contents this world.
     *
     * @return the world-map acquisition
     * @since 1.0
     */
    @NonNull WorldMapAcquisition acquireWorldMapRead();

    /**
     * Creates {@linkplain WriteWorldMapAcquisition a write world-map acquisition} of contents of this world.
     *
     * @return the write world-map acquisition
     * @since 1.0
     */
    @NonNull WriteWorldMapAcquisition acquireWorldMapWrite();
}