package net.hypejet.jet.server.world;

import net.hypejet.concurrency.collection.CollectionAcquirable;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.collection.set.HashSetAcquirable;
import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.world.acquisition.worldmap.WorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.acquisition.worldmap.WriteWorldMapAcquisitionImpl;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.chunk.ChunkLoader;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.data.WorldData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

/**
 * Represents an implementation of {@linkplain World a world}.
 *
 * @since 1.0
 * @see World
 */
public final class JetWorld implements World {

    private final JetRegistryEntry<DimensionType> dimensionType;
    private final WorldData worldData;

    private final ChunkLoader<BlockState> chunkLoader;
    private final JetMinecraftServer server;

    private final NotNullObjectAcquirable<Position> defaultSpawnPosition;

    private final MapAcquirable<ChunkPosition, JetChunk, ?> chunks = new HashMapAcquirable<>();
    private final CollectionAcquirable<JetEntity, Set<JetEntity>> entities = new HashSetAcquirable<>();

    /**
     * Constructs the {@linkplain JetWorld world}.
     *
     * @param dimensionType a dimension type, of which type the world should be
     * @param worldData an additional world data that the world should have
     * @param chunkLoader a chunk loader that should be used for loading and saving chunks of the world
     * @param server a server that should own the world
     * @since 1.0
     */
    public JetWorld(@NonNull JetRegistryEntry<DimensionType> dimensionType, @NonNull WorldData worldData,
                    @NonNull ChunkLoader<BlockState> chunkLoader, @NonNull JetMinecraftServer server) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.worldData = NullabilityUtil.requireNonNull(worldData, "world data");
        this.chunkLoader = NullabilityUtil.requireNonNull(chunkLoader, "chunk loader");
        this.server = NullabilityUtil.requireNonNull(server, "server");
        this.defaultSpawnPosition = new NotNullObjectAcquirable<>(new Position(0, 0, 0, 0f, 0f));
    }

    @Override
    public @NonNull JetRegistryEntry<DimensionType> dimensionType() {
        return this.dimensionType;
    }

    @Override
    public @NonNull WorldData worldData() {
        return this.worldData;
    }

    @Override
    public @NonNull CollectionAcquisition<JetEntity, ?> entities() {
        return this.entities.acquireRead();
    }

    @Override
    public @NonNull NotNullObjectAcquisition<Position> acquireDefaultSpawnPositionRead() {
        return this.defaultSpawnPosition.acquireRead();
    }

    @Override
    public @NonNull WriteNotNullObjectAcquisition<Position> acquireDefaultSpawnPositionWrite() {
        return this.defaultSpawnPosition.acquireWrite();
    }

    @Override
    public @NonNull WorldMapAcquisitionImpl acquireWorldMapRead() {
        return new WorldMapAcquisitionImpl(this, this.chunks.acquireRead());
    }

    @Override
    public @NonNull WriteWorldMapAcquisitionImpl acquireWorldMapWrite() {
        return new WriteWorldMapAcquisitionImpl(this, this.chunks.acquireWrite());
    }

    @Override
    public @NonNull JetMinecraftServer server() {
        return this.server;
    }

    /**
     * Gets {@linkplain ChunkLoader a chunk loader}, which should be used for loading and saving
     * {@linkplain net.hypejet.jet.world.chunk.Chunk chunks} of this world.
     *
     * @return the chunk provider
     * @since 1.0
     */
    public @NonNull ChunkLoader<BlockState> chunkLoader() {
        return this.chunkLoader;
    }

    /**
     * Adds {@linkplain JetPlayer a player} specified into this {@linkplain JetWorld world}.
     *
     * @param player the player
     * @since 1.0
     */
    public void addPlayer(@NonNull JetPlayer player) {
        NullabilityUtil.requireNonNull(player, "player");
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
        NullabilityUtil.requireNonNull(player, "player");
        try (CollectionAcquisition<?, Set<JetEntity>> entitiesAcquisition = this.entities.acquireWrite()) {
            Set<JetEntity> entities = entitiesAcquisition.collection();
            if (!entities.remove(player))
                throw new IllegalArgumentException("The player specified has not been initialized in this world");
        }
    }
}