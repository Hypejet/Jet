package net.hypejet.jet.server.world;

import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.acquisition.BooleanMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.NotNullObjectMappedAcquisition;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.Chunk;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.ChunkProvider;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain World a world}.
 *
 * @since 1.0
 * @see World
 */
public final class JetWorld implements World {

    private final UUID uniqueId;
    private final JetRegistryEntry<DimensionType> dimensionType;

    private final ChunkProvider chunkProvider;
    private final JetMinecraftServer server;

    private final MapAcquirable<ChunkPosition, Chunk, ?> chunks = new HashMapAcquirable<>();

    /**
     * Constructs the {@linkplain JetWorld world}.
     *
     * @param uniqueId a unique identifier that the world should have
     * @param dimensionType a dimension type, of which type the world should be
     * @param chunkProvider a chunk provider that should be used for loading chunks of the world
     * @param server a server that should own the world
     * @since 1.0
     */
    public JetWorld(@NonNull UUID uniqueId, @NonNull JetRegistryEntry<DimensionType> dimensionType,
                    @NonNull ChunkProvider chunkProvider, @NonNull JetMinecraftServer server) {
        this.uniqueId = NullabilityUtil.requireNonNull(uniqueId, "unique identifier");
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.chunkProvider = NullabilityUtil.requireNonNull(chunkProvider, "chunk provider");
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

    @Override
    public @NonNull UUID uniqueId() {
        return this.uniqueId;
    }

    @Override
    public @NonNull JetRegistryEntry<DimensionType> dimensionType() {
        return this.dimensionType;
    }

    @Override
    public @NonNull NotNullObjectAcquisition<JetBlockState> getBlockState(@NonNull BlockPosition position) {
        return new NotNullObjectMappedAcquisition<>(
                this.loadChunk(position),
                acquisition -> acquisition.get().chunkSectionList().getBlockState(ChunkRelativePosition.from(position))
        );
    }

    @Override
    public void setBlockState(@NonNull BlockPosition position, @NonNull BlockState blockState) {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockState, "block state");

        if (!(blockState instanceof JetBlockState validatedBlockState))
            throw new IllegalArgumentException("The block state specified is not a valid block state");

        try (
                MapAcquisition<ChunkPosition, Chunk, ?> mapAcquisition = this.chunks.acquireWrite();
                NotNullObjectAcquisition<Chunk> chunkAcquisition = this.loadChunk(position)
        ) {
            Map<ChunkPosition, Chunk> chunks = mapAcquisition.map();
            Chunk chunk = chunkAcquisition.get();

            Chunk newChunk = chunk.withUpdates(
                    Set.of(new BlockStateUpdate(ChunkRelativePosition.from(position), validatedBlockState)),
                    Set.of(), Set.of()
            );

            if (chunk.equals(newChunk)) return;
            chunks.put(ChunkPosition.fromBlockPosition(position), newChunk);
        }
    }

    /**
     * Creates {@linkplain NotNullObjectAcquisition not-null object acquisition} of {@linkplain Chunk a chunk}
     * that {@linkplain BlockPosition a block position} specified belong to. If the chunk has not been loaded it is
     * loaded with {@linkplain ChunkProvider a chunk provider} of this world.
     *
     * @param position the block position
     * @return the acquisition of the chunk
     * @since 1.0
     */
    public @NonNull NotNullObjectAcquisition<Chunk> loadChunk(@NonNull BlockPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        return this.loadChunk(ChunkPosition.fromBlockPosition(position));
    }

    /**
     * Creates {@linkplain NotNullObjectAcquisition not-null object acquisition} of {@linkplain Chunk a chunk}
     * at {@linkplain ChunkPosition a chunk position} specified. If the chunk has not been loaded it is loaded
     * with {@linkplain ChunkProvider a chunk provider} of this world.
     *
     * @param position the chunk position
     * @return the acquisition of the chunk
     * @since 1.0
     */
    public @NonNull NotNullObjectAcquisition<Chunk> loadChunk(@NonNull ChunkPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        return new NotNullObjectMappedAcquisition<>(
                this.chunks.acquireRead(),
                acquisition -> acquisition.map().computeIfAbsent(position, ignored -> {
                    JetRegistryManager registryManager = this.server.registryManager();

                    int chunkX = position.chunkX();
                    int chunkZ = position.chunkZ();

                    BlockState defaultBlockState = this.chunkProvider.defaultBlockState(chunkX, chunkZ);
                    if (!(defaultBlockState instanceof JetBlockState validatedBlockState)) {
                        throw new IllegalArgumentException("The default block state" +
                                " specified is not a valid block state");
                    }

                    RegistryEntry<Biome> defaultBiome = this.chunkProvider.defaultBiome(chunkX, chunkZ);
                    if (!(defaultBiome instanceof JetRegistryEntry<Biome> validatedBiome)) {
                        throw new IllegalArgumentException("The registry entry of a default" +
                                " biome specified is not a valid registry entry");
                    }

                    Chunk.Builder builder = new Chunk.Builder(
                            this.dimensionType.value(),
                            registryManager.blockStateOrder(),
                            registryManager.biomeRegistry().elementOrder(),
                            validatedBlockState, validatedBiome
                    );

                    this.chunkProvider.provide(builder, chunkX, chunkZ, this);
                    return builder.build();
                })
        );
    }

    /**
     * Unloads {@linkplain Chunk a chunk} that owns {@linkplain BlockPosition a block position} specified.
     *
     * @param position the block position
     * @return {@code true} if the chunk has been unloaded, {@code false} otherwise
     * @since 1.0
     */
    public boolean unloadChunk(@NonNull BlockPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        return this.unloadChunk(ChunkPosition.fromBlockPosition(position));
    }

    /**
     * Unloads {@linkplain Chunk a chunk} at {@linkplain ChunkPosition a chunk position} specified.
     *
     * @param position the chunk position
     * @return {@code true} if the chunk has been unloaded, {@code false} otherwise
     * @since 1.0
     */
    public boolean unloadChunk(@NonNull ChunkPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        try (MapAcquisition<ChunkPosition, Chunk, ?> mapAcquisition = this.chunks.acquireWrite()) {
            // TODO: Do entity safety checks
            return mapAcquisition.map().remove(position) != null;
        }
    }

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, whose value represents whether
     * {@linkplain Chunk a chunk} that a {@linkplain BlockPosition a block position} specified belongs to is loaded.
     *
     * @param position the block position
     * @return the boolean acquisition, whose value is {@code true} if the chunk is loaded, or {@code false} otherwise
     * @since 1.0
     */
    public @NonNull BooleanAcquisition isChunkLoaded(@NonNull BlockPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        return this.isChunkLoaded(ChunkPosition.fromBlockPosition(position));
    }

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, whose value represents whether
     * {@linkplain Chunk a chunk} at {@linkplain ChunkPosition a chunk position} specified is loaded.
     *
     * @param position the chunk position
     * @return the boolean acquisition, whose value is {@code true} if the chunk is loaded, or {@code false} otherwise
     * @since 1.0
     */
    public @NonNull BooleanAcquisition isChunkLoaded(@NonNull ChunkPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        return new BooleanMappedAcquisition<>(
                this.chunks.acquireRead(),
                acquisition -> acquisition.map().containsKey(position)
        );
    }
}