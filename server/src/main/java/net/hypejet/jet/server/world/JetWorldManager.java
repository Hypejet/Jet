package net.hypejet.jet.server.world;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.acquisition.BooleanMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.CollectionMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.NullableObjectMappedAcquisition;
import net.hypejet.jet.util.exception.AlreadyExistsException;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.WorldManager;
import net.hypejet.jet.world.chunk.ChunkProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain WorldManager a world manager}.
 *
 * @since 1.0
 * @see WorldManager
 */
public final class JetWorldManager implements WorldManager {

    private final JetMinecraftServer server;
    private final MapAcquirable<UUID, JetWorld, ?> worldAcquirable = new HashMapAcquirable<>();

    /**
     * Constructs the {@linkplain JetWorldManager world manager}.
     *
     * @param server a server that the world management should be done for
     * @since 1.0
     */
    public JetWorldManager(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

    @Override
    public @NonNull NullableObjectAcquisition<JetWorld> getWorld(@NonNull UUID uniqueId) {
        return new NullableObjectMappedAcquisition<>(
                this.worldAcquirable.acquireRead(),
                acquisition -> acquisition.map().get(uniqueId)
        );
    }

    @Override
    public @NonNull JetWorld createAndRegisterWorld(@NonNull UUID uniqueId,
                                                    @NonNull RegistryEntry<DimensionType> dimensionType,
                                                    @NonNull ChunkProvider chunkProvider) {
        JetWorld world = this.createUnregisteredWorld(uniqueId, dimensionType, chunkProvider);
        this.registerWorld(world);
        return world;
    }

    @Override
    public @NonNull JetWorld createUnregisteredWorld(@NonNull UUID uniqueId,
                                                     @NonNull RegistryEntry<DimensionType> dimensionType,
                                                     @NonNull ChunkProvider chunkProvider) {
        if (!(dimensionType instanceof JetRegistryEntry<DimensionType> validatedDimensionType)) {
            throw new IllegalArgumentException("The dimension type registry entry" +
                    " specified is not a valid registry entry");
        }
        return new JetWorld(uniqueId, validatedDimensionType, chunkProvider, this);
    }

    @Override
    public void registerWorld(@NonNull World world) {
        JetWorld validatedWorld = validateWorld(world);
        try (MapAcquisition<UUID, JetWorld, ?> acquisition = this.worldAcquirable.acquireWrite()) {
            UUID uniqueId = world.uniqueId();
            Map<UUID, JetWorld> map = acquisition.map();
            
            if (map.containsKey(uniqueId)) {
                throw new AlreadyExistsException(String.format(
                        "A world with unique identifier of %s already exists",
                        uniqueId
                ));
            }

            map.put(uniqueId, validatedWorld);
        }
    }

    @Override
    public void unregisterWorld(@NonNull UUID uniqueId) {
        try (MapAcquisition<UUID, JetWorld, ?> acquisition = this.worldAcquirable.acquireWrite()) {
            acquisition.map().remove(uniqueId); // TODO: Safety checks
        }
    }

    @Override
    public @NonNull CollectionAcquisition<? extends World, ?> worlds() {
        return new CollectionMappedAcquisition<>(
                this.worldAcquirable.acquireRead(),
                acquisition -> Collections.unmodifiableCollection(acquisition.map().values())
        );
    }

    @Override
    public @NonNull BooleanAcquisition isRegistered(@NonNull World world) {
        validateWorld(world);
        return new BooleanMappedAcquisition<>(
                this.worldAcquirable.acquireRead(),
                acquisition -> Objects.equals(world, acquisition.map().get(world.uniqueId()))
        );
    }

    /**
     * Gets {@linkplain JetMinecraftServer a Minecraft server} that owns
     * this {@linkplain JetWorldManager world manager}.
     *
     * @return the Minecraft server
     * @since 1.0
     */
    public @NonNull JetMinecraftServer server() {
        return this.server;
    }

    private static @NonNull JetWorld validateWorld(@NonNull World world) {
        NullabilityUtil.requireNonNull(world, "world");
        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The world specified is not a valid world");
        return validatedWorld;
    }
}