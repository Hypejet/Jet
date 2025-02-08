package net.hypejet.jet.server.world.chunk.light;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.storage.LightStorage;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents light data of block states stored in an associated
 * {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 */
public final class LightSection {

    private final LightStorage skyLightStorage;
    private final LightStorage blockLightStorage;

    /**
     * Constructs the {@linkplain LightSection light section}.
     *
     * @param skyLightStorage a light storage of skylight of block states of the section
     * @param blockLightStorage a light storage of block light of block states of the section
     * @since 1.0
     */
    private LightSection(@NonNull LightStorage skyLightStorage, @NonNull LightStorage blockLightStorage) {
        this.skyLightStorage = NullabilityUtil.requireNonNull(skyLightStorage, "skylight storage");
        this.blockLightStorage = NullabilityUtil.requireNonNull(blockLightStorage, "block light storage");
    }

    /**
     * Gets {@linkplain LightStorage a light storage}, which stores skylight of block states of the section.
     *
     * @return the light storage
     * @since 1.0
     */
    public @NonNull LightStorage skyLightStorage() {
        return this.skyLightStorage;
    }

    /**
     * Gets {@linkplain LightStorage a light storage}, which stores block light of block states of the section.
     *
     * @return the light storage
     * @since 1.0
     */
    public @NonNull LightStorage blockLightStorage() {
        return this.blockLightStorage;
    }

    /**
     * Creates a new {@linkplain LightSection light section} with updates specified that should be applied to
     * the skylight storage and to the block light storage.
     *
     * @param skyLightStorageUpdates updates that should be applied to the skylight storage
     * @param blockLightStorageUpdates updates that should be applied to the block light storage
     * @return the new light section
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull LightSection withUpdates(@NonNull Collection<LightStorageUpdate> skyLightStorageUpdates,
                                             @NonNull Collection<LightStorageUpdate> blockLightStorageUpdates) {
        NullabilityUtil.requireNonNull(skyLightStorageUpdates, "sky light storage updates");
        NullabilityUtil.requireNonNull(blockLightStorageUpdates, "block light storage updates");

        LightStorage newSkyLightStorage = this.skyLightStorage.withUpdates(skyLightStorageUpdates);
        LightStorage newBlockLightStorage = this.blockLightStorage.withUpdates(blockLightStorageUpdates);

        if (newSkyLightStorage == this.skyLightStorage && newBlockLightStorage == this.blockLightStorage)
            return this;
        return new LightSection(newSkyLightStorage, newBlockLightStorage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LightSection otherSection)) return false;
        return Objects.equals(this.skyLightStorage, otherSection.skyLightStorage)
                && Objects.equals(this.blockLightStorage, otherSection.blockLightStorage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.skyLightStorage, this.blockLightStorage);
    }

    @Override
    public String toString() {
        return "LightSection{" +
                "skyLightStorage=" + this.skyLightStorage +
                ", blockLightStorage=" + this.blockLightStorage +
                '}';
    }

    /**
     * Represents a builder of {@linkplain LightSection a light section}.
     *
     * @since 1.0
     * @see LightSection
     */
    public static final class Builder {

        private static final int ELEMENT_COUNT = ChunkPaletteType.BLOCK_STATE.elementCount();

        private final byte[] skyLightValues = new byte[ELEMENT_COUNT];
        private final byte[] blockLightValues = new byte[ELEMENT_COUNT];

        /**
         * Sets a skylight value that should be set for a block
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param value the skylight value
         * @since 1.0
         */
        public void setSkyLight(@NonNull ChunkPaletteRelativePosition position, byte value) {
            setLight(this.skyLightValues, position, value);
        }

        /**
         * Sets a block light value that should be set for a block
         * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
         *
         * @param position the chunk-palette-relative position
         * @param value the block light value
         * @since 1.0
         */
        public void setBlockLight(@NonNull ChunkPaletteRelativePosition position, byte value) {
            setLight(this.blockLightValues, position, value);
        }

        /**
         * Builds {@linkplain LightSection a light section} with data set in this builder.
         *
         * @return the light section
         * @since 1.0
         */
        @Contract(pure = true)
        public @NonNull LightSection build() {
            return new LightSection(
                    LightStorage.create(this.skyLightValues),
                    LightStorage.create(this.blockLightValues)
            );
        }

        private static void setLight(byte @NonNull [] values, @NonNull ChunkPaletteRelativePosition position,
                                     byte value) {
            NullabilityUtil.requireNonNull(values, "values");
            NullabilityUtil.requireNonNull(position, "position");

            if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
                throw new IllegalArgumentException("The chunk-relative position specified" +
                        " must be a position created for block state chunk palettes");
            }

            values[ChunkPalette.calculateElementIndex(position)] = value;
        }
    }
}