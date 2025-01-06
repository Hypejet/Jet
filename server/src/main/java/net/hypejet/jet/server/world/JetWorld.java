package net.hypejet.jet.server.world;

import io.netty.util.collection.LongObjectMap;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.acquirable.map.longs.LongObjectHashMapAcquirable;
import net.hypejet.jet.server.util.acquisition.NotNullObjectMappedAcquisition;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.Chunk;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain World a world}.
 *
 * @since 1.0
 * @see World
 */
public final class JetWorld implements World {

    private static final byte CHUNK_AXIS_LENGTH = 16;

    private final UUID uniqueId;
    private final RegistryEntry<DimensionType> dimensionType;
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
    public JetWorld(@NotNull UUID uniqueId, @NotNull RegistryEntry<DimensionType> dimensionType,
                    @NotNull JetMinecraftServer server) {
        this.uniqueId = NullabilityUtil.requireNonNull(uniqueId, "unique identifier");
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

    @Override
    public @NotNull UUID uniqueId() {
        return this.uniqueId;
    }

    @Override
    public @NotNull RegistryEntry<DimensionType> dimensionType() {
        return this.dimensionType;
    }

    @Override
    public @NotNull NotNullObjectAcquisition<Integer> getBlock(@NotNull BlockPosition position) {
        // TODO: Replace integer with a block object
        return new NotNullObjectMappedAcquisition<>(this.chunks.acquireRead(), acquisition -> {
            Chunk chunk = chunk(acquisition.map(), position);
            ChunkSection chunkSection = this.chunkSection(chunk, position.blockY());
            return chunkSection.blockPalette().getElement(
                    createSectionRelativeCoordinate(position.blockX()),
                    createSectionRelativeCoordinate(position.blockY()),
                    createSectionRelativeCoordinate(position.blockZ())
            );
        });
    }

    @Override
    public void setBlock(@NotNull BlockPosition position, int block) {
        // TODO: Replace integer with a block object
        try (MapAcquisition<?, ?, LongObjectMap<Chunk>> acquisition = this.chunks.acquireWrite()) {
            LongObjectMap<Chunk> chunks = acquisition.map();
            Chunk chunk = chunk(chunks, position);

            ChunkSection chunkSection = this.chunkSection(chunk, position.blockY());
            ChunkPalette blockPalette = chunkSection.blockPalette();

            byte sectionX = createSectionRelativeCoordinate(position.blockX());
            byte sectionY = createSectionRelativeCoordinate(position.blockY());
            byte sectionZ = createSectionRelativeCoordinate(position.blockZ());

            int previousBlockStateId = blockPalette.getElement(sectionX, sectionY, sectionZ);
            if (previousBlockStateId == block) return;

            ChunkPaletteUpdate update = new ChunkPaletteUpdate(sectionX, sectionY, sectionZ, block);
            ChunkPalette newPalette = blockPalette.withUpdates(update);

            short newBlockCount = recalculateBlockCount(block, previousBlockStateId, chunkSection);

            ChunkSection newChunkSection = new ChunkSection(newBlockCount, newPalette, chunkSection.biomePalette());
            List<ChunkSection> newChunkSections = new ArrayList<>(chunk.sections());
            newChunkSections.set(this.createChunkSectionIndex(position.blockY()), newChunkSection);

            // TODO: Create new heightmaps and block entities
            long packedChunkPosition = createPackedChunkPosition(position);
            Chunk newChunk = new Chunk(chunk.chunkX(), chunk.chunkZ(), chunk.heightmaps(),
                    newChunkSections, chunk.blockEntities(), chunk.lightData());
            chunks.put(packedChunkPosition, newChunk);
        }
    }

    private short recalculateBlockCount(int blockStateId, int previousBlockStateId,
                                        @NonNull ChunkSection chunkSection) {
        ElementOrder<JetBlockState> blockStateOrder = this.server.registryManager().blockStateOrder();

        JetBlockState previousBlockState = blockStateOrder.get(previousBlockStateId);
        if (previousBlockState == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block state with identifier of %d", previousBlockStateId
            ));
        }

        JetBlockState blockState = blockStateOrder.get(blockStateId);
        if (blockState == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block state with identifier of %d", previousBlockStateId
            ));
        }

        boolean wasAir = previousBlockState.isAir();
        boolean isAir = blockState.isAir();

        short newBlockCount = chunkSection.blockCount();
        if (!(wasAir && isAir)) {
            if (isAir) newBlockCount--;
            else newBlockCount++;
        }

        return newBlockCount;
    }

    private @NotNull ChunkSection chunkSection(@NotNull Chunk chunk, int blockY) {
        ChunkSection section = chunk.sections().get(this.createChunkSectionIndex(blockY));
        if (section == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a chunk section at block height of %d",
                    blockY
            ));
        }
        return section;
    }

    private int createChunkSectionIndex(int blockY) {
        int minimumSectionY = Math.floorDiv(this.dimensionType.value().minY(), CHUNK_AXIS_LENGTH);
        return Math.floorDiv(blockY, CHUNK_AXIS_LENGTH) - minimumSectionY;
    }

    private static @NotNull Chunk chunk(@NotNull LongObjectMap<Chunk> map, @NotNull BlockPosition position) {
        Chunk chunk = map.get(createPackedChunkPosition(position));
        if (chunk == null)
            throw new IllegalArgumentException(String.format("No chunk was loaded at block position of %s", position));
        return chunk;
    }

    private static byte createSectionRelativeCoordinate(int blockCoordinate) {
        return (byte) (blockCoordinate % CHUNK_AXIS_LENGTH);
    }

    private static long createPackedChunkPosition(@NotNull BlockPosition position) {
        int chunkX = blockToChunkCoordinate(position.blockX());
        int chunkZ = blockToChunkCoordinate(position.blockZ());
        return createPackedChunkPosition(chunkX, chunkZ);
    }

    private static int blockToChunkCoordinate(int blockCoordinate) {
        return Math.floorDiv(blockCoordinate, CHUNK_AXIS_LENGTH);
    }

    private static long createPackedChunkPosition(int chunkX, int chunkZ) {
        return ((long) chunkX << Integer.SIZE) | chunkZ;
    }
}