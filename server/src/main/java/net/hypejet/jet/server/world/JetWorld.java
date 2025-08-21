package net.hypejet.jet.server.world;

import net.hypejet.concurrency.collection.CollectionAcquirable;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.collection.set.HashSetAcquirable;
import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.acquisition.worldmap.WorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.data.WorldData;
import net.hypejet.jet.world.dimension.DimensionType;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain World a world}.
 *
 * @since 1.0
 * @see World
 */
public final class JetWorld implements World {

    private final Holder.Reference<DimensionType> dimensionType;
    private final WorldData worldData;

    private final ChunkLoader chunkLoader;
    private final JetRegistryManager registryManager;

    private final MapAcquirable<ChunkPosition, JetChunk, ?> chunks = new HashMapAcquirable<>();
    private final CollectionAcquirable<JetEntity, Set<JetEntity>> entities = new HashSetAcquirable<>();

    private Position defaultSpawnPosition = Position.zero();

    /**
     * Constructs the {@linkplain JetWorld world}.
     *
     * @param dimensionType a holder referencing to a dimension type, of which type the world should be
     * @param worldData an additional world data that the world should have
     * @param chunkLoader a chunk loader that should be used for loading and saving chunks of the world
     * @param registryManager a registry manager of the server that the world is being constructed for
     * @since 1.0
     */
    public JetWorld(Holder.@NonNull Reference<DimensionType> dimensionType, @NonNull WorldData worldData,
                    @NonNull ChunkLoader chunkLoader, @NonNull JetRegistryManager registryManager) {
        this.dimensionType = Objects.requireNonNull(dimensionType, "dimension type");
        this.worldData = Objects.requireNonNull(worldData, "world data");
        this.chunkLoader = Objects.requireNonNull(chunkLoader, "chunk loader");
        this.registryManager = Objects.requireNonNull(registryManager, "registry manager");
    }

    @Override
    public Holder.@NonNull Reference<DimensionType> dimensionType() {
        return this.dimensionType;
    }

    @Override
    public @NonNull WorldData worldData() {
        return this.worldData;
    }

    @Override
    public @NonNull Position defaultSpawnPosition() {
        return this.defaultSpawnPosition;
    }

    @Override
    public void defaultSpawnPosition(@NonNull Position position) {
        this.defaultSpawnPosition = Objects.requireNonNull(position, "position");
    }

    @Override
    public @NonNull CollectionAcquisition<JetEntity, ?> entities() {
        return this.entities.acquireRead();
    }

    @Override
    public @NonNull WorldMapAcquisitionImpl acquireWorldMapRead() {
        return new WorldMapAcquisitionImpl(this, this.chunks.acquireRead());
    }

    @Override
    public @NonNull WriteWorldMapAcquisitionImpl acquireWorldMapWrite() {
        return new WriteWorldMapAcquisitionImpl(this, this.chunks.acquireWrite());
    }

    /**
     * Gets {@linkplain ChunkLoader a chunk loader}, which should be used for loading and saving
     * {@linkplain net.hypejet.jet.world.chunk.Chunk chunks} of this world.
     *
     * @return the chunk provider
     * @since 1.0
     */
    public @NonNull ChunkLoader chunkLoader() {
        return this.chunkLoader;
    }

    /**
     * Gets a {@linkplain JetRegistryManager registry manager} of the {@linkplain JetMinecraftServer server}
     * that this {@linkplain JetWorld world} was created for.
     *
     * @return the registry manager
     * @since 1.0
     */
    public @NonNull JetRegistryManager registryManager() {
        return this.registryManager;
    }

    /**
     * Adds {@linkplain JetPlayer a player} specified into this {@linkplain JetWorld world}.
     *
     * @param player the player
     * @since 1.0
     */
    public void addPlayer(@NonNull JetPlayer player) {
        Objects.requireNonNull(player, "player");
        try (CollectionAcquisition<?, Set<JetEntity>> entitiesAcquisition = this.entities.acquireWrite()) {
            Set<JetEntity> entities = entitiesAcquisition.collection();
            if (!entities.add(player))
                throw new IllegalArgumentException("The player specified has been already initialized in this world");
        }
    }

    /**
     * Removes {@linkplain JetPlayer a player} specified from this {@linkplain JetWorld world}.
     *
     * @param player the player
     * @since 1.0
     */
    public void removePlayer(@NonNull JetPlayer player) {
        Objects.requireNonNull(player, "player");
        try (CollectionAcquisition<?, Set<JetEntity>> entitiesAcquisition = this.entities.acquireWrite()) {
            Set<JetEntity> entities = entitiesAcquisition.collection();
            if (!entities.remove(player))
                throw new IllegalArgumentException("The player specified has not been initialized in this world");
        }
    }
}