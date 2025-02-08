package net.hypejet.jet.server.world.chunk.section;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.chunk.update.BiomeUpdate;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a storage of {@linkplain ChunkSection chunk sections}
 * for {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @since 1.0
 * @see ChunkSection
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public final class ChunkSectionList {

    private final DimensionType dimensionType;
    private final List<ChunkSection> sections;

    /**
     * Constructs the {@linkplain ChunkSectionList chunk section list}.
     *
     * @param dimensionType a dimension type of world of a chunk that the chunk section list is created for
     * @param sections a list of sections that the chunk section list should have
     * @since 1.0
     */
    private ChunkSectionList(@NonNull DimensionType dimensionType, @NonNull List<ChunkSection> sections) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.sections = List.copyOf(NullabilityUtil.requireNonNull(sections, "sections"));
    }

    /**
     * Gets {@linkplain List a list} of {@linkplain ChunkSection chunk sections}
     * that this {@linkplain ChunkSectionList chunk section list} stores.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<ChunkSection> sections() {
        return this.sections;
    }

    /**
     * Gets {@linkplain JetBlockState a block state}, which is present
     * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
     *
     * @param position the chunk-relative position
     * @return the block state
     * @since 1.0
     */
    public @NonNull JetBlockState getBlockState(@NonNull ChunkRelativePosition position) {
        return this.sectionFor(position).blockStatePalette().getElement(position.toChunkPaletteRelative());
    }

    /**
     * Gets {@linkplain ChunkSection a chunk section}, which owns
     * {@linkplain ChunkRelativePosition a chunk-relative position} specified.
     *
     * @param position the chunk-relative position
     * @return the chunk section
     * @since 1.0
     */
    public @NonNull ChunkSection sectionFor(@NonNull ChunkRelativePosition position) {
        int sectionIndex = createSectionIndex(position, this.dimensionType);

        ChunkSection section = this.sections.get(sectionIndex);
        if (section == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a chunk section for a chunk-relative position specified (%s)",
                    position
            ));
        }

        return section;
    }

    /**
     * Creates a copy of this {@linkplain ChunkSectionList chunk section list}
     * with {@linkplain BlockStateUpdate block state updates} and {@linkplain BiomeUpdate biome updates}
     * specified applied.
     *
     * @param blockStateUpdates the block state updates
     * @param biomeUpdates the biome updates
     * @return the copy
     * @since 1.0
     */
    public @NonNull ChunkSectionList withUpdates(
            @NonNull Collection<BlockStateUpdate> blockStateUpdates,
            @NonNull Collection<BiomeUpdate> biomeUpdates
    ) {
        if (blockStateUpdates.isEmpty() && biomeUpdates.isEmpty())
            return this;

        List<ChunkSection> sections = new ArrayList<>(this.sections);

        IntObjectMap<List<ChunkPaletteUpdate<JetBlockState>>> blockStatePaletteUpdates;
        IntObjectMap<List<ChunkPaletteUpdate<JetRegistryEntry<Biome>>>> biomePaletteUpdates;

        blockStatePaletteUpdates = createChunkSectionPaletteUpdateMap(
                blockStateUpdates, this.dimensionType,
                BlockStateUpdate::position, BlockStateUpdate::blockState
        );

        biomePaletteUpdates = createChunkSectionPaletteUpdateMap(
                biomeUpdates, this.dimensionType,
                BiomeUpdate::position, BiomeUpdate::biome
        );

        boolean sectionListUpdated = false;
        for (int index = 0; index < sections.size(); index++) {
            List<ChunkPaletteUpdate<JetBlockState>> blockStateUpdateList = blockStatePaletteUpdates.get(index);
            List<ChunkPaletteUpdate<JetRegistryEntry<Biome>>> biomeUpdateList = biomePaletteUpdates.get(index);

            if (blockStateUpdateList == null && biomeUpdateList == null)
                continue;

            if (blockStateUpdateList == null)
                blockStateUpdateList = List.of();
            if (biomeUpdateList == null)
                biomeUpdateList = List.of();

            ChunkSection section = sections.get(index);
            ChunkSection updatedSection = section.withUpdates(blockStateUpdateList, biomeUpdateList);

            if (section.equals(updatedSection))
                continue;
            if (!sectionListUpdated)
                sectionListUpdated = true;

            sections.set(index, updatedSection);
        }

        if (!sectionListUpdated)
            return this;
        return new ChunkSectionList(this.dimensionType, sections);
    }

    /**
     * Creates an index of {@linkplain ChunkSection a chunk section}, which owns
     * {@linkplain ChunkRelativePosition a chunk-relative position} specified.
     *
     * @param position the chunk-relative position
     * @param dimensionType a dimension type of world of the chunk section
     * @return the chunk section
     * @since 1.0
     */
    public static int createSectionIndex(@NonNull ChunkRelativePosition position,
                                         @NonNull DimensionType dimensionType) {
        int axisLength = position.paletteType().axisLength();
        byte blockStateAxisLength = ChunkPaletteType.BLOCK_STATE.axisLength();

        int positionToBlockYMultiplier = blockStateAxisLength / axisLength;
        int blockY = position.absoluteY() * positionToBlockYMultiplier;

        return Math.floorDiv(blockY - dimensionType.minY(), blockStateAxisLength);
    }

    /**
     * Creates a number, which is a count of {@linkplain ChunkSection chunk sections} that worlds
     * with {@linkplain DimensionType a dimension type} specified have.
     *
     * @param dimensionType the dimension type
     * @return the number
     * @since 1.0
     */
    public static int createSectionCount(@NonNull DimensionType dimensionType) {
        return Math.ceilDiv(dimensionType.height(), ChunkPaletteType.BLOCK_STATE.axisLength());
    }

    private static <E, U> @NonNull IntObjectMap<List<ChunkPaletteUpdate<E>>> createChunkSectionPaletteUpdateMap(
            @NonNull Collection<U> updates, @NonNull DimensionType dimensionType,
            @NonNull Function<U, ChunkRelativePosition> updateToPositionFunction,
            @NonNull Function<U, E> elementFunction
    ) {
        IntObjectMap<List<ChunkPaletteUpdate<E>>> map = new IntObjectHashMap<>();

        for (U update : updates) {
            ChunkRelativePosition position = updateToPositionFunction.apply(update);
            int sectionIndex = createSectionIndex(position, dimensionType);

            List<ChunkPaletteUpdate<E>> paletteUpdates;
            if (map.containsKey(sectionIndex)) {
                paletteUpdates = map.get(sectionIndex);
            } else {
                paletteUpdates = new ArrayList<>();
                map.put(sectionIndex, paletteUpdates);
            }

            paletteUpdates.add(new ChunkPaletteUpdate<>(
                    position.toChunkPaletteRelative(),
                    elementFunction.apply(update)
            ));
        }

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkSectionList otherSection)) return false;
        return Objects.equals(this.dimensionType, otherSection.dimensionType)
                && Objects.equals(this.sections, otherSection.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dimensionType, this.sections);
    }

    @Override
    public String toString() {
        return "ChunkSectionList{" +
                "dimensionType=" + this.dimensionType +
                ", sections=" + this.sections +
                '}';
    }

    /**
     * Represents a builder of {@linkplain ChunkSectionList a chunk section list}.
     *
     * @since 1.0
     * @see ChunkSectionList
     */
    public static final class Builder {

        private final DimensionType dimensionType;

        private final ElementOrder<JetBlockState> blockStateOrder;
        private final ElementOrder<JetRegistryEntry<Biome>> biomeOrder;

        private final JetBlockState defaultBlockState;
        private final JetRegistryEntry<Biome> defaultBiome;

        private final IntObjectMap<ChunkSection.Builder> chunkSectionBuilders = new IntObjectHashMap<>();
        private final int chunkSectionCount;

        /**
         * Constructs the {@linkplain Builder chunk section list builder}.
         *
         * @param dimensionType a dimension type of world of a chunk that the chunk section list is created for
         * @param blockStateOrder an element order of block states that should be used for creation
         *                        of block state palettes
         * @param biomeOrder an element order of biomes that should be used for creation of biome palettes
         * @param defaultBlockState a default block state that should be used in places where a block state
         *                          has not been set
         * @param defaultBiome a registry entry of a biome that should be used where a biome has not been set
         * @since 1.0
         */
        public Builder(@NonNull DimensionType dimensionType, @NonNull ElementOrder<JetBlockState> blockStateOrder,
                       @NonNull ElementOrder<JetRegistryEntry<Biome>> biomeOrder,
                       @NonNull JetBlockState defaultBlockState, JetRegistryEntry<Biome> defaultBiome) {
            this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");

            this.blockStateOrder = NullabilityUtil.requireNonNull(blockStateOrder, "block state order");
            this.biomeOrder = NullabilityUtil.requireNonNull(biomeOrder, "biome order");

            this.defaultBlockState = NullabilityUtil.requireNonNull(defaultBlockState, "default block state");
            this.defaultBiome = NullabilityUtil.requireNonNull(defaultBiome, "default biome");

            this.chunkSectionCount = createSectionCount(dimensionType);
        }

        /**
         * Sets {@linkplain JetBlockState a block state} that should be present
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param blockState the block state
         * @since 1.0
         */
        public void setBlockState(@NonNull ChunkRelativePosition position, @NonNull JetBlockState blockState) {
            int sectionIndex = createSectionIndex(position, this.dimensionType);
            if (sectionIndex >= this.chunkSectionCount || sectionIndex < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "Could not create a valid section index for a chunk-relative position specified (%s)",
                        position
                ));
            }

            ChunkPaletteRelativePosition relativePosition = position.toChunkPaletteRelative();
            this.findOrCreateChunkSectionBuilder(sectionIndex).setBlockState(relativePosition, blockState);
        }

        /**
         * Sets {@linkplain Biome a biome} that should be present
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param biome a registry entry of the biome
         * @since 1.0
         */
        public void setBiome(@NonNull ChunkRelativePosition position, @NonNull JetRegistryEntry<Biome> biome) {
            int sectionIndex = createSectionIndex(position, this.dimensionType);
            if (sectionIndex >= this.chunkSectionCount || sectionIndex < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "Could not create a valid section index for a chunk-relative position specified (%s)",
                        position
                ));
            }

            ChunkPaletteRelativePosition relativePosition = position.toChunkPaletteRelative();
            this.findOrCreateChunkSectionBuilder(sectionIndex).setBiome(relativePosition, biome);
        }

        /**
         * Builds {@linkplain ChunkSectionList a chunk section list} with data set in this builder.
         *
         * @return the chunk section list
         * @since 1.0
         */
        public @NonNull ChunkSectionList build() {
            List<ChunkSection> chunkSections = new ArrayList<>(this.chunkSectionCount);
            ChunkSection emptyChunkSection = null;

            for (int index = 0; index < this.chunkSectionCount; index++) {
                ChunkSection chunkSection;

                ChunkSection.Builder builder = this.chunkSectionBuilders.get(index);
                if (builder != null) {
                    chunkSection = builder.build(this.blockStateOrder, this.biomeOrder);
                } else {
                    if (emptyChunkSection == null) {
                        emptyChunkSection = new ChunkSection.Builder(this.defaultBlockState, this.defaultBiome)
                                .build(this.blockStateOrder, this.biomeOrder);
                    }
                    chunkSection = emptyChunkSection;
                }

                chunkSections.add(index, chunkSection);
            }

            return new ChunkSectionList(this.dimensionType, chunkSections);
        }

        private ChunkSection.@NonNull Builder findOrCreateChunkSectionBuilder(int sectionIndex) {
            return this.chunkSectionBuilders.computeIfAbsent(
                    sectionIndex,
                    ignored -> new ChunkSection.Builder(this.defaultBlockState, this.defaultBiome)
            );
        }
    }
}