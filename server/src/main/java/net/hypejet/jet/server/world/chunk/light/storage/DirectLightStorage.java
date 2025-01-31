package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortMaps;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain LightStorage a light storage}, which stores all light values directly in a data array.
 *
 * @since 1.0
 * @see LightStorage
 */
public final class DirectLightStorage extends LightStorage {

    private final UnmodifiableByteArray data;
    private final Byte2ShortMap valueCountMap;

    /**
     * Constructs the {@linkplain DirectLightStorage direct light storage}.
     *
     * @param data a data that the direct light storage should have
     * @param valueCountMap a map, which maps all light values to their count in the storage
     * @since 1.0
     */
    DirectLightStorage(byte @NonNull [] data, @NonNull Byte2ShortMap valueCountMap) {
        NullabilityUtil.requireNonNull(data, "data");
        NullabilityUtil.requireNonNull(valueCountMap, "value count map");

        if (data.length != DATA_ARRAY_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "Length of the data array specified is invalid (%d != %d)",
                    data.length, DATA_ARRAY_LENGTH
            ));
        }

        this.data = new UnmodifiableByteArray(data);
        this.valueCountMap = Byte2ShortMaps.unmodifiable(new Byte2ShortOpenHashMap(valueCountMap));
    }

    @Override
    public byte getValue(byte x, byte y, byte z) {
        return LightStorage.getValue(
                this.data.array(),
                ChunkPalette.calculateElementIndex(ChunkPaletteType.BLOCK_STATE.axisLength(), x, y, z)
        );
    }

    @Override
    public byte @NonNull [] data() {
        return this.data.array();
    }

    @Override
    protected @NonNull Byte2ShortMap valueCountMap() {
        return this.valueCountMap;
    }

    /**
     * Creates {@linkplain DirectLightStorage a direct light storage} with a light value array specified. The array
     * should store light value for each block state index of a chunk section.
     *
     * @param values the values
     * @return the direct light storage
     * @since 1.0
     * @throws IllegalArgumentException if length of the value array specified is invalid
     */
    public static @NonNull DirectLightStorage create(byte @NonNull [] values) {
        return LightStorage.createDirectStorage(values);
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