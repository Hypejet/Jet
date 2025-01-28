package net.hypejet.jet.server.world.chunk.section;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a part of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public final class ChunkSection {

    private final short nonAirBlockCount;

    private final ChunkPalette<JetBlockState> blockStatePalette;
    private final ChunkPalette<JetRegistryEntry<Biome>> biomePalette;

    /**
     * Constructs the {@linkplain ChunkSection chunk section}.
     *
     * @param blockStatePalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @since 1.0
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     */
    public ChunkSection(@NonNull ChunkPalette<JetBlockState> blockStatePalette,
                        @NonNull ChunkPalette<JetRegistryEntry<Biome>> biomePalette) {
        this(calculateNonAirBlockCount(blockStatePalette), blockStatePalette, biomePalette);
    }

    /**
     * Constructs the {@linkplain ChunkSection chunk section}.
     *
     * @param nonAirBlockStateCount a count of non-air block states of the chunk section
     * @param blockStatePalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @since 1.0
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     */
    private ChunkSection(short nonAirBlockStateCount, @NonNull ChunkPalette<JetBlockState> blockStatePalette,
                         @NonNull ChunkPalette<JetRegistryEntry<Biome>> biomePalette) {
        NullabilityUtil.requireNonNull(blockStatePalette, "block state palette");
        NullabilityUtil.requireNonNull(biomePalette, "biome palette");

        if (blockStatePalette.type() != ChunkPaletteType.BLOCK_STATE)
            throw new IllegalArgumentException("Type of the block state palette is not a block state palette type");
        if (biomePalette.type() != ChunkPaletteType.BIOME)
            throw new IllegalArgumentException("Type of the biome palette is not a biome palette type");

        this.blockStatePalette = blockStatePalette;
        this.biomePalette = biomePalette;
        this.nonAirBlockCount = nonAirBlockStateCount;
    }

    /**
     * Gets a count of non-air block states of this chunk section.
     *
     * @return the count
     * @since 1.0
     */
    public short nonAirBlockCount() {
        return this.nonAirBlockCount;
    }

    /**
     * Gets a chunk palette of block states of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    public @NonNull ChunkPalette<JetBlockState> blockStatePalette() {
        return this.blockStatePalette;
    }

    /**
     * Gets a chunk palette of biomes of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    public @NonNull ChunkPalette<JetRegistryEntry<Biome>> biomePalette() {
        return this.biomePalette;
    }

    /**
     * Creates a new {@linkplain ChunkSection chunk section}, which is a copy of this chunk section, which uses a new
     * block state chunk palette, which was created by applying updates specified.
     *
     * @param updates the updates
     * @return the chunk section
     * @since 1.0
     */
    @SafeVarargs
    public final @NonNull ChunkSection withBlockStatePaletteUpdates(
            @NonNull ChunkPaletteUpdate<JetBlockState> @NonNull ... updates
    ) {
        return this.withReplacedBlockStatePalette(this.blockStatePalette.withUpdates(updates));
    }

    /**
     * Creates a new {@linkplain ChunkSection chunk section}, which is a copy of this chunk section, which uses
     * a block state chunk palette specified.
     *
     * @param palette the block state chunk palette
     * @return the chunk section
     * @since 1.0
     */
    public @NonNull ChunkSection withReplacedBlockStatePalette(@NonNull ChunkPalette<JetBlockState> palette) {
        return new ChunkSection(palette, this.biomePalette);
    }

    /**
     * Creates a new {@linkplain ChunkSection chunk section}, which is a copy of this chunk section, which uses a new
     * biome palette, which was created by applying updates specified.
     *
     * @param updates the updates
     * @return the chunk section
     * @since 1.0
     */
    @SafeVarargs
    public final @NonNull ChunkSection withBiomePaletteUpdates(
            @NonNull ChunkPaletteUpdate<JetRegistryEntry<Biome>> @NonNull ... updates
    ) {
        return this.withReplacedBiomePalette(this.biomePalette.withUpdates(updates));
    }

    /**
     * Creates a new {@linkplain ChunkSection chunk section}, which is a copy of this chunk section, which uses
     * a biome chunk palette specified.
     *
     * @param palette the biome chunk palette
     * @return the chunk section
     * @since 1.0
     */
    public @NonNull ChunkSection withReplacedBiomePalette(@NonNull ChunkPalette<JetRegistryEntry<Biome>> palette) {
        return new ChunkSection(this.nonAirBlockCount, this.blockStatePalette, palette);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChunkSection otherSection)) return false;
        return this.nonAirBlockCount == otherSection.nonAirBlockCount
                && Objects.equals(this.blockStatePalette, otherSection.blockStatePalette)
                && Objects.equals(this.biomePalette, otherSection.biomePalette);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nonAirBlockCount, this.blockStatePalette, this.biomePalette);
    }

    @Override
    public String toString() {
        return "ChunkSection{" +
                "nonAirBlockCount=" + this.nonAirBlockCount +
                ", blockStatePalette=" + this.blockStatePalette +
                ", biomePalette=" + this.biomePalette +
                '}';
    }

    private static short calculateNonAirBlockCount(@NonNull ChunkPalette<JetBlockState> blockStatePalette) {
        NullabilityUtil.requireNonNull(blockStatePalette, "block state palette");
        short nonAirBlockCount = 0;

        for (JetBlockState blockState : blockStatePalette.elementCountMap().keySet()) {
            if (blockState.isAir()) continue;
            nonAirBlockCount++;
        }

        return nonAirBlockCount;
    }
}