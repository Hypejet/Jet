package net.hypejet.jet.server.world.chunk.light;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.concurrency.empty.EmptyAcquirable;
import net.hypejet.concurrency.empty.EmptyAcquisition;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.ChunkSectionList;
import net.hypejet.jet.server.world.chunk.update.LightUpdate;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a storage of {@linkplain LightSection light sections}
 * for a {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk}.
 *
 * @since 1.0
 * @see LightSection
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public final class LightSectionList {

    private static final LightSection EMPTY_LIGHT_SECTION = new LightSection.Builder().build();

    private final DimensionType dimensionType;
    private final List<LightSection> sections;

    private @MonotonicNonNull @GuardedBy("serializationDataLock") LightSerializationData serializationData;
    private final EmptyAcquirable serializationDataLock = new EmptyAcquirable();

    /**
     * Constructs the {@linkplain LightSectionList light section list}.
     *
     * @param dimensionType a dimension type of world of a chunk that the light section list is created for
     * @param sections a list of sections that the light section list should have
     * @since 1.0
     */
    private LightSectionList(@NonNull DimensionType dimensionType, @NonNull List<LightSection> sections) {
        this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
        this.sections = List.copyOf(NullabilityUtil.requireNonNull(sections, "sections"));
    }

    /**
     * Gets {@linkplain List a list} of {@linkplain LightSection light sections}
     * that this {@linkplain LightSectionList light section list} stores.
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<LightSection> sections() {
        return this.sections;
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
     * Creates a copy of this {@linkplain LightSectionList light section list} with updates specified appplied.
     *
     * @param updates the updates
     * @return the copy
     * @since 1.0
     */
    public @NonNull LightSectionList withUpdates(@NonNull Collection<LightUpdate> updates) {
        if (updates.isEmpty())
            return this;

        List<LightSection> sections = new ArrayList<>(this.sections);

        IntObjectMap<List<LightStorageUpdate>> indexToSkyLightUpdateMap = new IntObjectHashMap<>();
        IntObjectMap<List<LightStorageUpdate>> indexToBlockLightUpdateMap = new IntObjectHashMap<>();

        for (LightUpdate update : updates) {
            ChunkRelativePosition position = update.position();
            int sectionIndex = createSectionIndex(position, this.dimensionType);

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

            lightStorageUpdates.add(new LightStorageUpdate(position.toChunkPaletteRelative(), update.lightValue()));
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

            LightSection section = sections.get(index);
            LightSection updatedSection = section.withUpdates(skyLightUpdates, blockLightUpdates);

            if (section.equals(updatedSection))
                continue;
            if (!sectionListUpdated)
                sectionListUpdated = true;

            sections.set(index, section);
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

    private static int createSectionIndex(@NonNull ChunkRelativePosition position,
                                          @NonNull DimensionType dimensionType) {
        /* The result is a sum of chunk section index with 1, since there is one light section that is present
           below lowest chunk section and one light section that is present above highest chunk section. */
        return ChunkSectionList.createSectionIndex(position, dimensionType) + 1;
    }

    /**
     * Represents a builder of {@linkplain LightSectionList a light section list}.
     *
     * @since 1.0
     * @see LightSectionList
     */
    public static final class Builder {

        private final DimensionType dimensionType;
        private final int lightSectionCount;

        private final IntObjectMap<LightSection.Builder> sectionBuilders = new IntObjectHashMap<>();

        /**
         * Constructs the {@linkplain Builder light section list builder}.
         *
         * @param dimensionType a dimension type of world of a chunk that the light section list is created for
         * @since 1.0
         */
        public Builder(@NonNull DimensionType dimensionType) {
            this.dimensionType = NullabilityUtil.requireNonNull(dimensionType, "dimension type");
            /* The result is a sum of chunk section count with 2, since there is one light section that is present
               below lowest chunk section and one light section that is present above highest chunk section. */
            this.lightSectionCount = ChunkSectionList.createSectionCount(dimensionType) + 2;
        }

        /**
         * Sets a skylight value that should be set for a block
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param value the skylight value
         * @since 1.0
         */
        public void setSkyLight(@NonNull ChunkRelativePosition position, byte value) {
            NullabilityUtil.requireNonNull(position, "position");
            this.getOrCreateBuilder(position).setSkyLight(position.toChunkPaletteRelative(), value);
        }

        /**
         * Sets a block light value that should be set for a block
         * at {@linkplain ChunkRelativePosition a chunk-relative position} specified.
         *
         * @param position the chunk-relative position
         * @param value the block light value
         * @since 1.0
         */
        public void setBlockLight(@NonNull ChunkRelativePosition position, byte value) {
            NullabilityUtil.requireNonNull(position, "position");
            this.getOrCreateBuilder(position).setBlockLight(position.toChunkPaletteRelative(), value);
        }

        /**
         * Builds {@linkplain LightSectionList a light section list} with data set in this builder.
         *
         * @return the light section list
         * @since 1.0
         */
        public @NonNull LightSectionList build() {
            List<LightSection> lightSections = new ArrayList<>(this.lightSectionCount);

            for (int index = 0; index < this.lightSectionCount; index++) {
                LightSection lightSection;

                LightSection.Builder builder = this.sectionBuilders.get(index);
                if (builder == null) lightSection = EMPTY_LIGHT_SECTION;
                else lightSection = builder.build();

                lightSections.add(index, lightSection);
            }

            return new LightSectionList(this.dimensionType, lightSections);
        }

        private LightSection.@NonNull Builder getOrCreateBuilder(@NonNull ChunkRelativePosition position) {
            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException("The chunk-relative position specified" +
                        " must be a position created for block state chunk palettes");
            }

            int sectionIndex = createSectionIndex(position, this.dimensionType);
            if (sectionIndex >= this.lightSectionCount || sectionIndex < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "Could not create a valid section index for a chunk-relative position specified (%s)",
                        position
                ));
            }

            return this.sectionBuilders.computeIfAbsent(sectionIndex, ignored -> new LightSection.Builder());
        }
    }
}