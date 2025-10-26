package net.hypejet.jet.world.chunk.factory;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.chunk.builder.ChunkBuilder;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.hypejet.jet.world.dimension.DimensionType;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents a factory of {@linkplain Chunk chunks}.
 *
 * @since 1.0
 * @see Chunk
 */
public interface ChunkFactory {
    /**
     * Creates an instance of {@linkplain Chunk a chunk}.
     *
     * @param dimensionType a dimension type of worlds that the chunk is created for
     * @param chunkSections a list of chunk sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param lightSections a list of light sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param blockEntities a map, which maps chunk-relative block positions of blocks to data of block entities that
     *                      the blocks should have
     * @return the chunk
     * @since 1.0
     */
    @NonNull Chunk createChunk(@NonNull DimensionType dimensionType,
                               @NonNull List<ChunkSection> chunkSections, @NonNull List<LightSection> lightSections,
                               @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities);

    /**
     * Creates {@linkplain ChunkBuilder a chunk builder}, which is initially filled with an air block with default
     * properties and no block entity. The chunk builder is also going to be filled with a plains biome.
     *
     * @param dimensionType a dimension type of worlds that the chunk is created for
     * @return the chunk builder
     * @since 1.0
     */
    @NonNull ChunkBuilder createChunkBuilder(@NonNull DimensionType dimensionType);

    /**
     * Creates a {@linkplain ChunkBuilder chunk builder} and fills it initially with a block
     * with the specified data and fills it with the specified biome.
     *
     * @param dimensionType a dimension type of worlds that the chunk is created for
     * @param blockState a block state of which the block should be
     * @param defaultBiome a holder referencing to the biome that the chunk builder should be initially filled with
     * @param defaultBlockEntity a data of a block entity that the block should have, {@code null} if the block
     *                           should not have a block entity
     * @return the chunk builder
     * @since 1.0
     */
    @NonNull ChunkBuilder createChunkBuilder(
            @NonNull DimensionType dimensionType, @NonNull BlockState blockState,
            Holder.@NonNull Reference<Biome> defaultBiome, @Nullable CompoundBinaryTag defaultBlockEntity
    );
}