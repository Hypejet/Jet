package net.hypejet.jet.server.world.chunk.section;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.collection.ListUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.List;
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
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     * @since 1.0
     */
    private ChunkSection(@NonNull ChunkPalette<JetBlockState> blockStatePalette,
                         @NonNull ChunkPalette<JetRegistryEntry<Biome>> biomePalette) {
        this(calculateNonAirBlockCount(blockStatePalette), blockStatePalette, biomePalette);
    }

    /**
     * Constructs the {@linkplain ChunkSection chunk section}.
     *
     * @param nonAirBlockStateCount a count of non-air block states of the chunk section
     * @param blockStatePalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     * @since 1.0
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
     * block state chunk palette and a new biome chunk palette, which were created by applying updates specified.
     *
     * @param blockStateUpdates updates that should be applied to the block state chunk palette
     * @param biomeUpdates updates that should be applied to the biome chunk palette
     * @return the chunk section
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull ChunkSection withUpdates(
            @NonNull Collection<ChunkPaletteUpdate<JetBlockState>> blockStateUpdates,
            @NonNull Collection<ChunkPaletteUpdate<JetRegistryEntry<Biome>>> biomeUpdates
    ) {
        if (blockStateUpdates.isEmpty() && biomeUpdates.isEmpty())
            return this;

        ChunkPalette<JetBlockState> blockStatePalette = this.blockStatePalette.withUpdates(blockStateUpdates);
        ChunkPalette<JetRegistryEntry<Biome>> biomePalette = this.biomePalette.withUpdates(biomeUpdates);

        boolean blockStatePaletteUnchanged = this.blockStatePalette.equals(blockStatePalette);
        if (blockStatePaletteUnchanged && this.biomePalette.equals(biomePalette))
            return this;

        if (blockStatePaletteUnchanged)
            return new ChunkSection(this.nonAirBlockCount, blockStatePalette, biomePalette);
        return new ChunkSection(blockStatePalette, biomePalette);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
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

    /**
     * Represents a builder of {@linkplain ChunkSection a chunk section}.
     *
     * @since 1.0
     * @see ChunkSection
     */
    public static final class Builder {

        private final List<JetBlockState> blockStates;
        private final List<JetRegistryEntry<Biome>> biomes;

        /**
         * Constructs the {@linkplain Builder chunk section builder}.
         *
         * @param defaultBlockState a default block state that should be used in places where a block state
         *                          has not been set
         * @param defaultBiome a registry entry of a biome that should be used where a biome has not been set
         * @since 1.0
         */
        public Builder(@NonNull JetBlockState defaultBlockState, @NonNull JetRegistryEntry<Biome> defaultBiome) {
            NullabilityUtil.requireNonNull(defaultBlockState, "default block state");
            NullabilityUtil.requireNonNull(defaultBiome, "default biome");

            int blockStateListElementCount = ChunkPaletteType.BLOCK_STATE.elementCount();
            this.blockStates = ListUtil.filledArrayList(defaultBlockState, blockStateListElementCount);

            int biomeListElementCount = ChunkPaletteType.BIOME.elementCount();
            this.biomes = ListUtil.filledArrayList(defaultBiome, biomeListElementCount);
        }

        /**
         * Sets {@linkplain JetBlockState a block state} specified at a position specified.
         *
         * @param position the position
         * @param blockState the block state to set
         * @since 1.0
         */
        public void setBlockState(@NonNull ChunkPaletteRelativePosition position, @NonNull JetBlockState blockState) {
            NullabilityUtil.requireNonNull(position, "position");
            NullabilityUtil.requireNonNull(blockState, "block state");

            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException("The chunk-relative position specified" +
                        " is not a position created for block state chunk palettes");
            }

            this.blockStates.set(ChunkPalette.calculateElementIndex(position), blockState);
        }

        /**
         * Sets {@linkplain JetBlockState a block state} specified at a position specified.
         *
         * @param position the position
         * @param biome the biome to set
         * @since 1.0
         */
        public void setBiome(@NonNull ChunkPaletteRelativePosition position, @NonNull JetRegistryEntry<Biome> biome) {
            NullabilityUtil.requireNonNull(position, "position");
            NullabilityUtil.requireNonNull(biome, "biome");

            if (position.paletteType() != ChunkPaletteType.BIOME) {
                throw new IllegalArgumentException("The chunk-relative position specified" +
                        " is not a position created for biome chunk palettes");
            }

            this.biomes.set(ChunkPalette.calculateElementIndex(position), biome);
        }

        /**
         * Builds {@linkplain ChunkSection a chunk section} with data set in this builder.
         *
         * @param blockStateOrder an element order of block states that should be used for the block state palette
         * @param biomeOrder an element order of biomes that should be used for the biome palette
         * @return the chunk section
         * @since 1.0
         */
        @Contract(pure = true)
        public @NonNull ChunkSection build(@NonNull ElementOrder<JetBlockState> blockStateOrder,
                                           @NonNull ElementOrder<JetRegistryEntry<Biome>> biomeOrder) {
            return new ChunkSection(
                    ChunkPalette.create(ChunkPaletteType.BLOCK_STATE, blockStateOrder, this.blockStates),
                    ChunkPalette.create(ChunkPaletteType.BIOME, biomeOrder, this.biomes)
            );
        }
    }
}