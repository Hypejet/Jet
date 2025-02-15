package net.hypejet.jet.world;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.chunk.ChunkProvider;
import net.hypejet.jet.world.data.WorldData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents something that manages {@linkplain World worlds}.
 *
 * @since 1.0
 * @see World
 */
public interface WorldManager {
    /**
     * Creates {@linkplain NullableObjectAcquisition a nullable object acquisition}
     * of {@linkplain World a world} with {@linkplain UUID an unique identifier} specified.
     *
     * @param uniqueId the unique identifier
     * @return the nullable object acquisition, which contains the world or {@code null} if a world with the unique
     *         identifier specified does not exist
     * @since 1.0
     */
    @NonNull NullableObjectAcquisition<? extends World> getWorld(@NonNull UUID uniqueId);

    /**
     * Creates and registers {@linkplain World a world} with {@linkplain UUID an unique identifier} specified.
     *
     * @param uniqueId the unique identifier
     * @param dimensionType a registry entry of a dimension type that the world type should have
     * @param worldData an additional world data that the world should have
     * @param chunkProvider a chunk provider that the world should use for chunk loading
     * @return the world
     * @throws net.hypejet.jet.util.exception.AlreadyExistsException if a world with the unique identifier specified
     *                                                               has been already registered
     * @since 1.0
     */
    @NonNull World createAndRegisterWorld(@NonNull UUID uniqueId, @NonNull RegistryEntry<DimensionType> dimensionType,
                                          @NonNull WorldData worldData, @NonNull ChunkProvider chunkProvider);

    /**
     * Creates {@linkplain World a world} with {@linkplain UUID an unique identifier} specified without registering it.
     *
     * @param uniqueId the unique identifier
     * @param dimensionType a registry entry of a dimension type that the world type should have
     * @param worldData an additional world data that the world should have
     * @param chunkProvider a chunk provider that the world should use for chunk loading
     * @return the world
     * @since 1.0
     */
    @NonNull World createUnregisteredWorld(@NonNull UUID uniqueId, @NonNull RegistryEntry<DimensionType> dimensionType,
                                           @NonNull WorldData worldData, @NonNull ChunkProvider chunkProvider);

    /**
     * Registers {@linkplain World a world} specified in this {@linkplain WorldManager world manager}.
     *
     * @param world the world
     * @throws net.hypejet.jet.util.exception.AlreadyExistsException if a world with a unique identifier of the world
     *                                                               specified already exists
     * @since 1.0
     */
    void registerWorld(@NonNull World world);

    /**
     * Unregisters {@linkplain World a world} with {@linkplain UUID an unique identifier} specified. Does nothing
     * if a world with the unique identifier specified does not exist.
     *
     * @param uniqueId the unique identifier
     * @since 1.0
     */
    void unregisterWorld(@NonNull UUID uniqueId);

    /**
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of {@linkplain World worlds} registered
     * in this {@linkplain WorldManager world manager}.
     *
     * @return the collection acquisition
     * @since 1.0
     */
    @NonNull CollectionAcquisition<? extends World, ?> worlds();

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, whose value represents
     * whether {@linkplain World a world} specified is registered in this {@linkplain WorldManager world manager}.
     *
     * @param world the world
     * @return the boolean acquisition with value of {@code true} if the world specified has been registered in this
     *         world manager or {@code false} otherwise
     * @since 1.0
     */
    @NonNull BooleanAcquisition isRegistered(@NonNull World world);
}