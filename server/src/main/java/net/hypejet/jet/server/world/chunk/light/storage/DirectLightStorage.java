package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortMaps;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.NibbleArray;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain AbstractLightStorage a light storage}, which stores all light-level values
 * directly in a data array.
 *
 * @since 1.0
 * @see AbstractLightStorage
 */
public final class DirectLightStorage extends AbstractLightStorage {

    private final NibbleArray data;
    private final Byte2ShortMap valueCountMap;

    /**
     * Constructs the {@linkplain DirectLightStorage direct light storage}.
     *
     * @param data a nibble array of light-level values that the direct light storage should have
     * @param valueCountMap a map, which maps all light-level values to their count in the storage
     * @since 1.0
     */
    DirectLightStorage(@NonNull NibbleArray data, @NonNull Byte2ShortMap valueCountMap) {
        NullabilityUtil.requireNonNull(data, "data");
        NullabilityUtil.requireNonNull(valueCountMap, "value count map");

        if (data.length() != DATA_VALUE_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Length of the data nibble array specified is invalid (%d != %d)",
                    data.length(), DATA_VALUE_COUNT
            ));
        }

        this.data = NullabilityUtil.requireNonNull(data, "data");
        this.valueCountMap = Byte2ShortMaps.unmodifiable(new Byte2ShortOpenHashMap(valueCountMap));
    }

    @Override
    public @NonNull NibbleArray data() {
        return this.data;
    }

    @Override
    public byte getValue(@NonNull ChunkPaletteRelativePosition position) {
        if (position.paletteType() != ChunkPaletteType.BLOCK_STATE) {
            throw new IllegalArgumentException(
                    "The chunk-palette-relative position specified has not been created for block state chunk palettes"
            );
        }
        return this.data.get(AbstractChunkPalette.calculateElementIndex(position));
    }

    @Override
    protected @NonNull Byte2ShortMap valueCountMap() {
        return this.valueCountMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectLightStorage otherStorage)) return false;
        return Objects.equals(this.data, otherStorage.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.data);
    }

    @Override
    public String toString() {
        return "DirectLightStorage{" +
                "data=" + this.data +
                '}';
    }
}