package net.hypejet.jet.server.world.chunk;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.LightData;
import net.hypejet.jet.server.world.chunk.entity.BlockEntity;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Represents part of {@linkplain ??? a world}.
 *
 * @param chunkX an {@code X} value of coordinates of the chunk
 * @param chunkZ an {@code Z} value of coordinates of the chunk
 * @param heightmaps a compound binary tag that contains heightmaps that the chunk should have
 * @param sections a list of sections that the chunk should have, the list is ordered from lowest to highest section
 * @param blockEntities a set of block entities that the chunk should have
 * @param lightData a light data that the chunk should have
 * @since 1.0
 * @see ???
 */
public record Chunk(int chunkX, int chunkZ, @NotNull CompoundBinaryTag heightmaps,
                    @NotNull List<ChunkSection> sections, @NotNull Set<BlockEntity> blockEntities,
                    @NotNull LightData lightData) {
    /**
     * Constructs the {@linkplain Chunk chunk}.
     *
     * @param chunkX an {@code X} value of coordinates of the chunk
     * @param chunkZ an {@code Z} value of coordinates of the chunk
     * @param heightmaps a compound binary tag that contains heightmaps that the chunk should have
     * @param sections a list of sections that the chunk should have, the list is ordered from lowest to highest
     *                 section
     * @param blockEntities a set of block entities that the chunk should have
     * @param lightData a light data that the chunk should have
     * @since 1.0
     */
    public Chunk {
        NullabilityUtil.requireNonNull(heightmaps, "heightmaps");
        NullabilityUtil.requireNonNull(sections, "sections");
        NullabilityUtil.requireNonNull(blockEntities, "block entities");
        NullabilityUtil.requireNonNull(lightData, "light data");

        sections = List.copyOf(sections);
        blockEntities = Set.copyOf(blockEntities);
    }
}