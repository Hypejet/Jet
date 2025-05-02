package net.hypejet.jet.world.chunk;

import net.hypejet.jet.world.World;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Represents part of {@linkplain World a world}.
 *
 * @param <BS> a type of block states that block state palettes of chunk sections contain
 * @since 1.0
 * @see World
 */
public interface Chunk<BS> {
    /**
     * Gets {@linkplain List a list} of {@linkplain ChunkSection chunk sections} of this chunk. The list is ordered
     * by their height, the lowest index corresponds the lowest section, the highest index corresponds the highest
     * section.
     *
     * @return the list
     * @since 1.0
     */
    @NonNull List<? extends ChunkSection<BS>> sections();

    /**
     * Gets {@linkplain List a list} of {@linkplain LightSection light sections} of this chunk. The list is ordered
     * by their height, the lowest index corresponds the lowest section, the highest index corresponds the highest
     * section.
     *
     * @return the list
     * @since 1.0
     */
    @NonNull List<? extends LightSection> lightSections();

    /**
     * Gets {@linkplain Map a map}, which maps {@linkplain ChunkRelativeBlockPosition chunk-relative block positions}
     * of blocks to data of their block entities.
     *
     * @return the map
     * @since 1.0
     */
    @NonNull Map<ChunkRelativeBlockPosition, CompoundBinaryTag> blockEntities();
}