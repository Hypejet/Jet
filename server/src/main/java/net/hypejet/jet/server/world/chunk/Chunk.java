package net.hypejet.jet.server.world.chunk;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.entity.BlockEntity;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMapType;
import net.hypejet.jet.server.world.chunk.light.LightSection;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents part of {@linkplain net.hypejet.jet.server.world.JetWorld a world}.
 *
 * @param chunkX an {@code X} value of coordinates of the chunk
 * @param chunkZ an {@code Z} value of coordinates of the chunk
 * @param heightmaps a set of height maps that the chunk should have
 * @param sections a list of sections that the chunk should have, the list is ordered from lowest to highest section
 * @param blockEntities a set of block entities that the chunk should have
 * @param lightSections a list of light sections that the chunk should have, each chunk section should have a light
 *                      section associated in this list and additionally one light section above the highest chunk
 *                      section and one light section below the lowest chunk section
 * @since 1.0
 * @see net.hypejet.jet.server.world.JetWorld
 */
public record Chunk(int chunkX, int chunkZ, @NotNull Set<HeightMap> heightmaps,
                    @NotNull List<ChunkSection> sections, @NotNull Set<BlockEntity> blockEntities,
                    @NotNull List<LightSection> lightSections) {
    /**
     * Constructs the {@linkplain Chunk chunk}.
     *
     * @param chunkX an {@code X} value of coordinates of the chunk
     * @param chunkZ an {@code Z} value of coordinates of the chunk
     * @param heightmaps a set of height maps that the chunk should have
     * @param sections a list of sections that the chunk should have, the list is ordered from lowest to highest
     *                 section
     * @param blockEntities a set of block entities that the chunk should have
     * @param lightSections a list of light sections that the chunk should have, each chunk section should have a light
     *                      section associated in this list and additionally one light section above the highest chunk
     *                      section and one light section below the lowest chunk section
     * @since 1.0
     */
    public Chunk {
        NullabilityUtil.requireNonNull(heightmaps, "heightmaps");
        NullabilityUtil.requireNonNull(sections, "sections");
        NullabilityUtil.requireNonNull(blockEntities, "block entities");
        NullabilityUtil.requireNonNull(lightSections, "light sections");

        heightmaps = Set.copyOf(heightmaps);
        sections = List.copyOf(sections);
        blockEntities = Set.copyOf(blockEntities);
        lightSections = List.copyOf(lightSections);
    }
}