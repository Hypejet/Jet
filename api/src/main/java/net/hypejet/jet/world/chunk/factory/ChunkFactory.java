package net.hypejet.jet.world.chunk.factory;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.chunk.builder.ChunkBuilder;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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

    /**
     * Creates {@linkplain ChunkBuilder a chunk builder}, which is initially filled with an air block with default
     * properties and no block entity. The chunk builder is also going to be filled with a plains biome.
     *
     * @param dimensionType a dimension type of worlds that the chunk is created for
     * @return the chunk builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> createChunkBuilder(@NonNull DimensionType dimensionType);

    /**
     * Creates {@linkplain ChunkBuilder a chunk builder} and fills it initially with a block with data specified
     * and fills it with a biome specified.
     *
     * @param dimensionType a dimension type of worlds that the chunk is created for
     * @param defaultBlockTypeKey a key of a block type that the block should have
     * @param properties a properties that the block should have, {@code null} if default properties
     *                   of the block type should be used
     * @param defaultBlockEntity a block entity that the block should have, {@code null} if the block should not have
     *                           a block entity
     * @param defaultBiome a registry entry of the biome
     * @return the chunk builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> createChunkBuilder(
            @NonNull DimensionType dimensionType, @NonNull Key defaultBlockTypeKey,
            @Nullable Map<String, String> properties, @Nullable BlockEntity defaultBlockEntity,
            @NonNull RegistryEntry<Biome> defaultBiome
    );
}