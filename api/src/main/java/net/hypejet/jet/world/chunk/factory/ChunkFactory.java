package net.hypejet.jet.world.chunk.factory;

import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a factory of {@linkplain Chunk chunks}.
 *
 * @param <CS> a type of chunk sections that chunks should have
 * @param <LS> a type of light sections that chunks should have
 * @param <BS> a type of block states that block state palettes of chunk sections contain
 * @since 1.0
 * @see Chunk
 */
public interface ChunkFactory<CS extends ChunkSection<BS>, LS extends LightSection, BS> {
    /**
     * Creates an instance of {@linkplain Chunk a chunk}.
     *
     * @param dimensionType a dimension type of worlds that the chunk is created for
     * @param chunkSections a list of chunk sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param lightSections a list of light sections that the chunk should have, where the lowest index is the lowest
     *                      section and the highest index is the highest section
     * @param blockEntities a map, which maps chunk-relative block positions of blocks to block entities that
     *                      the blocks should have
     * @return the chunk
     * @since 1.0
     */
    @NonNull Chunk<BS> createChunk(@NonNull DimensionType dimensionType,
                                   @NonNull List<CS> chunkSections, @NonNull List<LS> lightSections,
                                   @NonNull Map<ChunkRelativeBlockPosition, BlockEntity> blockEntities);
}