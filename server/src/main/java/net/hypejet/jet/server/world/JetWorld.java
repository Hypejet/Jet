package net.hypejet.jet.server.world;

import io.netty.util.collection.LongObjectMap;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.acquirable.map.longs.LongObjectHashMapAcquirable;
import net.hypejet.jet.server.util.acquisition.NotNullObjectMappedAcquisition;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.Chunk;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

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
    private final JetMinecraftServer server;

    private final LongObjectHashMapAcquirable<Chunk> chunks = new LongObjectHashMapAcquirable<>();

    /**
     * Constructs the {@linkplain JetWorld world}.
     *
     * @param uniqueId a unique identifier that the world should have
     * @param dimensionType a dimension type, of which type the world should be
     * @param server a server that should own the world
     * @since 1.0
     */
    public JetWorld(@NonNull UUID uniqueId, @NonNull JetRegistryEntry<DimensionType> dimensionType,
                    @NonNull JetMinecraftServer server) {
        this.uniqueId = NullabilityUtil.requireNonNull(uniqueId, "unique identifier");
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.server = NullabilityUtil.requireNonNull(server, "server"); // TODO: Remove me?
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
        return new NotNullObjectMappedAcquisition<>(this.chunks.acquireRead(), acquisition -> {
            Chunk chunk = chunk(acquisition.map(), position);
            byte sectionRelativeX = Chunk.createSectionRelativeBiomeCoordinate(position.blockX());
            byte sectionRelativeZ = Chunk.createSectionRelativeBiomeCoordinate(position.blockZ());
            return chunk.getBlockState(sectionRelativeX, (short) position.blockY(), sectionRelativeZ);
        });
    }

    @Override
    public void setBlockState(@NonNull BlockPosition position, @NonNull BlockState blockState) {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockState, "block state");

        if (!(blockState instanceof JetBlockState validatedBlockState))
            throw new IllegalArgumentException("The block state specified is not a valid block state");

        try (MapAcquisition<?, ?, LongObjectMap<Chunk>> acquisition = this.chunks.acquireWrite()) {
            LongObjectMap<Chunk> chunks = acquisition.map();
            Chunk chunk = chunk(chunks, position);

            byte sectionRelativeX = Chunk.createSectionRelativeBiomeCoordinate(position.blockX());
            byte sectionRelativeZ = Chunk.createSectionRelativeBlockCoordinate(position.blockZ());

            Chunk newChunk = chunk.withUpdates(
                    Set.of(new BlockStateUpdate(
                            sectionRelativeX, (short) position.blockY(),
                            sectionRelativeZ, validatedBlockState
                    )), Set.of(), Set.of()
            );

            if (chunk.equals(newChunk)) return;

            long packedChunkPosition = createPackedChunkPosition(position);
            chunks.put(packedChunkPosition, newChunk);
        }
    }

    private static @NonNull Chunk chunk(@NonNull LongObjectMap<Chunk> map, @NonNull BlockPosition position) {
        Chunk chunk = map.get(createPackedChunkPosition(position));
        if (chunk == null)
            throw new IllegalArgumentException(String.format("No chunk was loaded at block position of %s", position));
        return chunk;
    }

    private static long createPackedChunkPosition(@NonNull BlockPosition position) {
        int chunkX = blockToChunkCoordinate(position.blockX());
        int chunkZ = blockToChunkCoordinate(position.blockZ());
        return createPackedChunkPosition(chunkX, chunkZ);
    }

    private static int blockToChunkCoordinate(int blockCoordinate) {
        return Math.floorDiv(blockCoordinate, ChunkPaletteType.BLOCK_STATE.axisLength());
    }

    private static long createPackedChunkPosition(int chunkX, int chunkZ) {
        return ((long) chunkX << Integer.SIZE) | chunkZ;
    }
}