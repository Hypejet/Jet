package net.hypejet.jet.server.world.chunk.light;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.concurrency.empty.EmptyAcquirable;
import net.hypejet.concurrency.empty.EmptyAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.update.LightUpdate;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import javax.annotation.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

        /* There is always one light section above the highest chunk section
           and one light section below the lowest chunk section. */
        int expectedSectionCount = ChunkSectionList.createSectionCount(dimensionType) + 2;
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
     * @param updates the updates
     * @return the copy
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull LightSectionList withUpdates(@NonNull Collection<LightUpdate> updates) {
        if (updates.isEmpty())
            return this;

        List<JetLightSection> sections = new ArrayList<>(this.sections);

        IntObjectMap<List<LightStorageUpdate>> indexToSkyLightUpdateMap = new IntObjectHashMap<>();
        IntObjectMap<List<LightStorageUpdate>> indexToBlockLightUpdateMap = new IntObjectHashMap<>();

        for (LightUpdate update : updates) {
            ChunkRelativeBlockPosition position = update.position();

            int sectionY = ChunkSectionList.createSectionY(position.absoluteY(), ChunkPaletteType.BLOCK_STATE);
            int sectionIndex = createSectionIndex(sectionY, this.dimensionType);

            IntObjectMap<List<LightStorageUpdate>> indexToLightUpdateMap = switch (update.lightType()) {
                case SKY -> indexToSkyLightUpdateMap;
                case BLOCK -> indexToBlockLightUpdateMap;
            };

            List<LightStorageUpdate> lightStorageUpdates;
            if (indexToLightUpdateMap.containsKey(sectionIndex)) {
                lightStorageUpdates = indexToLightUpdateMap.get(sectionIndex);
            } else {
                lightStorageUpdates = new ArrayList<>();
                indexToLightUpdateMap.put(sectionIndex, lightStorageUpdates);
            }

            ChunkPaletteRelativePosition paletteRelativePosition = ChunkPaletteRelativePosition.from(position);
            lightStorageUpdates.add(new LightStorageUpdate(paletteRelativePosition, update.lightValue()));
        }

        boolean sectionListUpdated = false;
        for (int index = 0; index < sections.size(); index++) {
            List<LightStorageUpdate> skyLightUpdates = indexToSkyLightUpdateMap.get(index);
            List<LightStorageUpdate> blockLightUpdates = indexToBlockLightUpdateMap.get(index);

            if (skyLightUpdates == null && blockLightUpdates == null)
                continue;

            if (skyLightUpdates == null)
                skyLightUpdates = List.of();
            if (blockLightUpdates == null)
                blockLightUpdates = List.of();

            JetLightSection section = sections.get(index);
            JetLightSection updatedSection = section.withUpdates(skyLightUpdates, blockLightUpdates);

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
}