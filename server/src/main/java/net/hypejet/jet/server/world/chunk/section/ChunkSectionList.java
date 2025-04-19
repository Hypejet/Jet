package net.hypejet.jet.server.world.chunk.section;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.chunk.update.BiomeUpdate;
import net.hypejet.jet.server.world.chunk.update.BlockStateUpdate;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * Represents a storage of {@linkplain JetChunkSection chunk sections} of {@linkplain JetChunk a chunk}.
 *
 * @since 1.0
 * @see JetChunkSection
 * @see JetChunk
 */
public final class ChunkSectionList {

    private final DimensionType dimensionType;
    private final List<JetChunkSection> sections;

    /**
     * Constructs the {@linkplain ChunkSectionList chunk section list}.
     *
     * @param dimensionType a dimension type of world of a chunk that the chunk section list is created for
     * @param sections a list of sections that the chunk section list should have
     * @since 1.0
     */
    public ChunkSectionList(@NonNull DimensionType dimensionType, @NonNull List<JetChunkSection> sections) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.sections = List.copyOf(NullabilityUtil.requireNonNull(sections, "sections"));

        int expectedSectionCount = createSectionCount(dimensionType);
        int actualSectionCount = this.sections.size();

        if (actualSectionCount != expectedSectionCount) {
            throw new IllegalArgumentException(String.format(
                    "Number of chunk sections specified (%d) is invalid, expected (%d)",
                    actualSectionCount, expectedSectionCount
            ));
        }
    }

    /**
     * Gets {@linkplain List a list} of {@linkplain JetChunkSection chunk sections}
     * that this {@linkplain ChunkSectionList chunk section list} stores.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<JetChunkSection> sections() {
        return this.sections;
    }

    /**
     * Gets {@linkplain JetChunkSection a chunk section} that
     * {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified belongs to.
     *
     * @param position the chunk-relative block position
     * @return the chunk section
     * @since 1.0
     */
    public @NonNull JetChunkSection sectionFor(@NonNull ChunkRelativeBlockPosition position) {
        return this.section(createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE));
    }

    /**
     * Gets {@linkplain JetChunkSection a chunk section} that
     * {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position} specified belongs to.
     *
     * @param position the chunk-relative biome position
     * @return the chunk section
     * @since 1.0
     */
    public @NonNull JetChunkSection sectionFor(@NonNull ChunkRelativeBiomePosition position) {
        return this.section(createSectionY(position.absoluteY(), ChunkPaletteType.BIOME));
    }

    /**
     * Gets {@linkplain JetChunkSection a chunk section} at a section-Y coordinate specified.
     *
     * @param sectionY the coordinate
     * @return the chunk section
     * @throws IndexOutOfBoundsException if the section-Y specified is invalid for this chunk section list
     * @since 1.0
     */
    public @NonNull JetChunkSection section(int sectionY) {
        int sectionIndex = createSectionIndex(sectionY, this.dimensionType);
        if (sectionIndex <= 0 || sectionIndex >= this.sections.size())
            throw new IndexOutOfBoundsException("Section-Y specified is invalid for this chunk section list");

        JetChunkSection section = this.sections.get(sectionIndex);
        if (section == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a chunk section for a section-Y specified (%d)",
                    sectionY
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
    @Contract(pure = true)
    public @NonNull ChunkSectionList withUpdates(
            @NonNull Collection<BlockStateUpdate> blockStateUpdates,
            @NonNull Collection<BiomeUpdate> biomeUpdates
    ) {
        if (blockStateUpdates.isEmpty() && biomeUpdates.isEmpty())
            return this;

        List<JetChunkSection> sections = new ArrayList<>(this.sections);

        IntObjectMap<List<ChunkPaletteUpdate<BlockState>>> blockStatePaletteUpdates;
        IntObjectMap<List<ChunkPaletteUpdate<RegistryEntry<Biome>>>> biomePaletteUpdates;

        blockStatePaletteUpdates = createChunkSectionPaletteUpdateMap(
                blockStateUpdates, this.dimensionType,
                update -> update.position().absoluteY(),
                update -> ChunkPaletteRelativePosition.from(update.position()),
                BlockStateUpdate::blockState, ChunkPaletteType.BLOCK_STATE
        );

        biomePaletteUpdates = createChunkSectionPaletteUpdateMap(
                biomeUpdates, this.dimensionType,
                update -> update.position().absoluteY(),
                update -> ChunkPaletteRelativePosition.from(update.position()),
                BiomeUpdate::biome, ChunkPaletteType.BIOME
        );

        boolean sectionListUpdated = false;
        for (int index = 0; index < sections.size(); index++) {
            List<ChunkPaletteUpdate<BlockState>> blockStateUpdateList = blockStatePaletteUpdates.get(index);
            List<ChunkPaletteUpdate<RegistryEntry<Biome>>> biomeUpdateList = biomePaletteUpdates.get(index);

            if (blockStateUpdateList == null && biomeUpdateList == null)
                continue;

            if (blockStateUpdateList == null)
                blockStateUpdateList = List.of();
            if (biomeUpdateList == null)
                biomeUpdateList = List.of();

            JetChunkSection section = sections.get(index);
            JetChunkSection updatedSection = section.withUpdates(blockStateUpdateList, biomeUpdateList);

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
     * Creates a number, which is a count of {@linkplain JetChunkSection chunk sections} that worlds
     * with {@linkplain DimensionType a dimension type} specified have.
     *
     * @param dimensionType the dimension type
     * @return the number
     * @since 1.0
     */
    public static int createSectionCount(@NonNull DimensionType dimensionType) {
        return Math.ceilDiv(dimensionType.height(), ChunkPaletteType.BLOCK_STATE.axisLength());
    }

    /**
     * Creates an index of {@linkplain JetChunkSection a chunk section} at a section-Y coordinate specified.
     *
     * @param sectionY the section-Y coordinate
     * @param dimensionType a dimension type of world that the chunk section belongs to
     * @return the chunk section
     * @since 1.0
     */
    public static int createSectionIndex(int sectionY, @NonNull DimensionType dimensionType) {
        int minimumSectionY = createSectionY(dimensionType.minY(), ChunkPaletteType.BLOCK_STATE);
        return sectionY - minimumSectionY;
    }

    /**
     * Creates a section-Y coordinate of {@linkplain JetChunkSection a chunk section} that an {@code Y} value
     * of an absolute coordinate with {@linkplain ChunkPaletteType chunk palette type} specified belongs to.
     *
     * @param y the Y value
     * @param type the chunk palette type
     * @return the section-Y coordinate
     * @since 1.0
     */
    public static int createSectionY(int y, @NonNull ChunkPaletteType type) {
        int blockStatePaletteAxisLength = ChunkPaletteType.BLOCK_STATE.axisLength();
        int coordinateMultiplier = blockStatePaletteAxisLength / type.axisLength();
        return Math.floorDiv(coordinateMultiplier * y, blockStatePaletteAxisLength);
    }

    private static <E, U> @NonNull IntObjectMap<List<ChunkPaletteUpdate<E>>> createChunkSectionPaletteUpdateMap(
            @NonNull Collection<U> updates, @NonNull DimensionType dimensionType,
            @NonNull ToIntFunction<U> updateToAbsoluteYFunction,
            @NonNull Function<U, ChunkPaletteRelativePosition> updateToPaletteRelativePosition,
            @NonNull Function<U, E> elementFunction, @NonNull ChunkPaletteType paletteType
    ) {
        IntObjectMap<List<ChunkPaletteUpdate<E>>> map = new IntObjectHashMap<>();

        for (U update : updates) {
            int absoluteY = updateToAbsoluteYFunction.applyAsInt(update);

            int sectionY = createSectionY(absoluteY, paletteType);
            int sectionIndex = createSectionIndex(sectionY, dimensionType);

            List<ChunkPaletteUpdate<E>> paletteUpdates;
            if (map.containsKey(sectionIndex)) {
                paletteUpdates = map.get(sectionIndex);
            } else {
                paletteUpdates = new ArrayList<>();
                map.put(sectionIndex, paletteUpdates);
            }

            paletteUpdates.add(new ChunkPaletteUpdate<>(
                    updateToPaletteRelativePosition.apply(update),
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
}