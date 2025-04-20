package net.hypejet.jet.server.world.chunk.light;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import net.hypejet.concurrency.empty.EmptyAcquirable;
import net.hypejet.concurrency.empty.EmptyAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import javax.annotation.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a storage of {@linkplain JetLightSection light sections} of {@linkplain JetChunk a chunk}.
 *
 * @since 1.0
 * @see JetLightSection
 * @see JetChunk
 */
public final class LightSectionList {

    private final DimensionType dimensionType;
    private final List<JetLightSection> sections;

    private @MonotonicNonNull @GuardedBy("serializationDataLock") LightSerializationData serializationData;
    private final EmptyAcquirable serializationDataLock = new EmptyAcquirable();

    /**
     * Constructs the {@linkplain LightSectionList light section list}.
     *
     * @param dimensionType a dimension type of world of a chunk that the light section list is created for
     * @param sections a list of sections that the light section list should have
     * @since 1.0
     */
    public LightSectionList(@NonNull DimensionType dimensionType, @NonNull List<JetLightSection> sections) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.sections = List.copyOf(NullabilityUtil.requireNonNull(sections, "sections"));

        int expectedSectionCount = createSectionCount(dimensionType);
        int actualSectionCount = this.sections.size();

        if (actualSectionCount != expectedSectionCount) {
            throw new IllegalArgumentException(String.format(
                    "Number of light sections specified (%d) is invalid, expected (%d)",
                    actualSectionCount, expectedSectionCount
            ));
        }
    }

    /**
     * Gets {@linkplain List a list} of {@linkplain JetLightSection light sections}
     * that this {@linkplain LightSectionList light section list} stores.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<JetLightSection> sections() {
        return this.sections;
    }

    /**
     * Gets {@linkplain JetLightSection a light section} that
     * {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified belongs to.
     *
     * @param position the chunk-relative block position
     * @return the light section
     * @since 1.0
     */
    public @NonNull JetLightSection sectionFor(@NonNull ChunkRelativeBlockPosition position) {
        return this.section(ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE));
    }

    /**
     * Gets {@linkplain JetLightSection a light section} at a section-Y coordinate specified.
     *
     * @param sectionY the coordinate
     * @return the light section
     * @throws IndexOutOfBoundsException if the section-Y specified is invalid for this light section list
     * @since 1.0
     */
    public @NonNull JetLightSection section(int sectionY) {
        int sectionIndex = createSectionIndex(sectionY, this.dimensionType);
        if (sectionIndex <= 0 || sectionIndex >= this.sections.size())
            throw new IndexOutOfBoundsException("Section-Y specified is invalid for this light section list");

        JetLightSection section = this.sections.get(sectionIndex);
        if (section == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a light section for a section-Y specified (%d)",
                    sectionY
            ));
        }

        return section;
    }

    /**
     * Gets {@linkplain LightSerializationData a light serialization data}
     * of this {@linkplain LightSectionList light section list}.
     *
     * @return the light serialization data
     * @since 1.0
     */
    public @NonNull LightSerializationData serializationData() {
        try (EmptyAcquisition ignoredAcquisition = this.serializationDataLock.acquireWrite()) {
            if (this.serializationData == null)
                this.serializationData = LightSerializationData.create(this);
            return this.serializationData;
        }
    }

    /**
     * Creates a copy of this {@linkplain LightSectionList light section list} with updates specified applied.
     *
     * @param skyLightUpdates a map which maps chunk-relative block positions to skylight level values that blocks
     *                        located at these positions should have
     * @param blockLightUpdates a map which maps chunk-relative block positions to block light level values that blocks
     *                          located at these positions should have
     * @return the copy
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull LightSectionList withUpdates(
            @NonNull Object2ByteMap<ChunkRelativeBlockPosition> skyLightUpdates,
            @NonNull Object2ByteMap<ChunkRelativeBlockPosition> blockLightUpdates
    ) {
        if (skyLightUpdates.isEmpty() && blockLightUpdates.isEmpty())
            return this;

        List<JetLightSection> sections = new ArrayList<>(this.sections);

        IntObjectMap<Set<LightStorageUpdate>> skyLightUpdatesMap = createIndexToLightUpdatesMap(skyLightUpdates);
        IntObjectMap<Set<LightStorageUpdate>> blockLightUpdatesMap = createIndexToLightUpdatesMap(blockLightUpdates);

        boolean sectionListUpdated = false;
        for (int index = 0; index < sections.size(); index++) {
            Set<LightStorageUpdate> skyLightStorageUpdates = skyLightUpdatesMap.get(index);
            Set<LightStorageUpdate> blockLightStorageUpdates = blockLightUpdatesMap.get(index);

            if (skyLightStorageUpdates == null && blockLightStorageUpdates == null)
                continue;

            if (skyLightStorageUpdates == null)
                skyLightStorageUpdates = Set.of();
            if (blockLightStorageUpdates == null)
                blockLightStorageUpdates = Set.of();

            JetLightSection section = sections.get(index);
            JetLightSection updatedSection = section.withUpdates(skyLightStorageUpdates, blockLightStorageUpdates);

            if (section.equals(updatedSection))
                continue;
            if (!sectionListUpdated)
                sectionListUpdated = true;

            sections.set(index, updatedSection);
        }

        if (!sectionListUpdated)
            return this;
        return new LightSectionList(this.dimensionType, sections);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof LightSectionList otherList)) return false;
        return Objects.equals(this.dimensionType, otherList.dimensionType)
                && Objects.equals(this.sections, otherList.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dimensionType, this.sections);
    }

    @Override
    public String toString() {
        return "LightSectionList{" +
                "dimensionType=" + this.dimensionType +
                ", sections=" + this.sections +
                '}';
    }

    /**
     * Creates a number, which is a count of {@linkplain JetLightSection light sections} that worlds
     * with {@linkplain DimensionType a dimension type} specified have.
     *
     * @param dimensionType the dimension type
     * @return the number
     * @since 1.0
     */
    public static int createSectionCount(@NonNull DimensionType dimensionType) {
        /* There is always one light section above the highest chunk section
           and one light section below the lowest chunk section. */
        return ChunkSectionList.createSectionCount(dimensionType) + 2;
    }

    /**
     * Creates an index of {@linkplain net.hypejet.jet.world.chunk.light.LightSection a light section}
     * at a section-Y specified in {@linkplain DimensionType a dimension type} specified.
     *
     * @param sectionY the section-Y
     * @param dimensionType the dimension type
     * @return the index
     * @since 1.0
     */
    public static int createSectionIndex(int sectionY, @NonNull DimensionType dimensionType) {
        /* The result is a sum of chunk section index with 1, since there is one light section that is present
           below lowest chunk section and one light section that is present above highest chunk section. */
        return ChunkSectionList.createSectionIndex(sectionY, dimensionType) + 1;
    }

    private @NonNull IntObjectMap<Set<LightStorageUpdate>> createIndexToLightUpdatesMap(
            @NonNull Object2ByteMap<ChunkRelativeBlockPosition> updates
    ) {
        IntObjectMap<Set<LightStorageUpdate>> indexToLightUpdatesMap = new IntObjectHashMap<>();
        for (Object2ByteMap.Entry<ChunkRelativeBlockPosition> entry : updates.object2ByteEntrySet()) {
            ChunkRelativeBlockPosition position = entry.getKey();

            int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
            int sectionIndex = createSectionIndex(sectionY, this.dimensionType);

            Set<LightStorageUpdate> storageUpdates = indexToLightUpdatesMap.get(sectionIndex);
            if (storageUpdates == null) {
                storageUpdates = new HashSet<>();
                indexToLightUpdatesMap.put(sectionIndex, storageUpdates);
            }

            ChunkPaletteRelativePosition palettePosition = ChunkPaletteRelativePosition.from(position);
            storageUpdates.add(new LightStorageUpdate(palettePosition, entry.getByteValue()));
        }
        return indexToLightUpdatesMap;
    }
}