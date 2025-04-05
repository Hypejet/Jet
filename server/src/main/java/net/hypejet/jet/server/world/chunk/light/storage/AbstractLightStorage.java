package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.hypejet.jet.world.chunk.light.LightStorage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents an abstract implementation of {@linkplain LightStorage a light storage}.
 *
 * @since 1.0
 * @see LightStorage
 */
public sealed abstract class AbstractLightStorage implements LightStorage
        permits DirectLightStorage, EmptyLightStorage {
    /**
     * A number of bits that a single light value should use.
     *
     * @since 1.0
     */
    private static final byte VALUE_BITS = Byte.SIZE / 2;

    /**
     * A maximum value that a light value can be.
     *
     * @since 1.0
     */
    private static final byte MAX_VALUE_ALLOWED = (byte) (MathUtil.power(2, VALUE_BITS) - 1);

    /**
     * Represents count of light values that one light storage has.
     *
     * @since 1.0
     */
    protected static final short DATA_VALUE_COUNT = ChunkPaletteType.BLOCK_STATE.elementCount();

    /**
     * Represents length of a light data array, whose elements represent two light values.
     *
     * @since 1.0
     */
    protected static final short DATA_ARRAY_LENGTH = (short) Math.ceilDiv(DATA_VALUE_COUNT, 2); // 2 values per element

    @Override
    public final byte @NonNull [] data() {
        return this.unmodifiableData().array();
    }

    @Override
    public final byte @NonNull [] uniqueValues() {
        return this.valueCountMap().keySet().toByteArray();
    }

    @Override
    public final short valueCount(byte value) {
        return this.valueCountMap().get(value);
    }

    /**
     * Creates a new copy of this {@linkplain AbstractLightStorage light storage}
     * with {@linkplain LightStorageUpdate light updates} specified applied.
     *
     * @param updates the light updates
     * @return the copy
     * @since 1.0
     */
    @Contract(pure = true)
    public final @NonNull AbstractLightStorage withUpdates(@NonNull Collection<LightStorageUpdate> updates) {
        NullabilityUtil.requireNonNull(updates, "updates");
        if (updates.isEmpty()) return this;

        byte[] data = this.data();
        Byte2ShortMap valueCountMap = new Byte2ShortOpenHashMap(this.valueCountMap());

        boolean lightChanged = false;

        for (LightStorageUpdate update : updates) {
            int paletteElementIndex = AbstractChunkPalette.calculateElementIndex(update.position());

            int dataIndex = createDataArrayElementIndex(paletteElementIndex);
            byte dataElement = data[dataIndex];

            byte previousValue = extractValue(dataElement, paletteElementIndex);
            byte newValue = update.value();

            if (previousValue == newValue) continue;
            if (!lightChanged) lightChanged = true;

            data[dataIndex] = updatedValue(dataElement, newValue, paletteElementIndex);

            short newValueCount = valueCountMap.containsKey(newValue) ? (short) (valueCountMap.get(newValue) + 1) : 1;
            valueCountMap.put(newValue, newValueCount);

            if (!valueCountMap.containsKey(previousValue))
                continue;

            short previousValueCount = (short) (valueCountMap.get(previousValue) - 1);
            if (previousValueCount <= 0) {
                valueCountMap.remove(previousValue);
                continue;
            }

            valueCountMap.put(previousValue, previousValueCount);
        }

        if (!lightChanged) return this;
        return create(valueCountMap, data);
    }

    /**
     * Gets a light value for a block state
     * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
     *
     * @param position the position
     * @return the light value
     * @since 1.0
     */
    public abstract byte getValue(@NonNull ChunkPaletteRelativePosition position);

    /**
     * Gets light values stored in this light storage as a light-value array with preserved order. The array is wrapped
     * with {@linkplain UnmodifiableByteArray an unmodifiable byte array}
     *
     * @return the light-value array, wrapped with an unmodifiable byte array
     * @since 1.0
     */
    public abstract @NonNull UnmodifiableByteArray unmodifiableData();

    /**
     * Gets a map, which maps all light values to their count in this storage.
     *
     * @return the map
     * @since 1.0
     */
    @Contract(pure = true)
    protected abstract @NonNull Byte2ShortMap valueCountMap();

    /**
     * Creates {@linkplain AbstractLightStorage a light storage} for a light-value array specified.
     *
     * @param values the light-value array
     * @return the light storage
     * @since 1.0
     */
    public static @NonNull AbstractLightStorage create(byte @NonNull [] values) {
        NullabilityUtil.requireNonNull(values, "values");
        int length = values.length;

        if (length != DATA_ARRAY_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "Length of the light-value array specified is invalid (%d != %d)",
                    length, DATA_ARRAY_LENGTH
            ));
        }

        return create(createCountMap(values), values);
    }

    /**
     * Gets a light value for a block state with a palette element index specified from a data array specified.
     *
     * <p>The data array must contain two values per element.</p>
     *
     * @param data the data array
     * @param paletteElementIndex the
     * @return the light value
     * @since 1.0
     */
    protected static byte getValue(byte @NonNull [] data, int paletteElementIndex) {
        int valueArrayElementIndex = createDataArrayElementIndex(paletteElementIndex);
        return extractValue(data[valueArrayElementIndex], paletteElementIndex);
    }

    private static @NonNull AbstractLightStorage create(@NonNull Byte2ShortMap valueCountMap, byte @NonNull [] data) {
        if (valueCountMap.size() == 1 && valueCountMap.containsKey((byte) 0))
            return EmptyLightStorage.INSTANCE;
        return new DirectLightStorage(data, valueCountMap);
    }

    private static @NotNull Byte2ShortMap createCountMap(byte @NotNull [] values) {
        Byte2ShortMap valueCountMap = new Byte2ShortOpenHashMap();
        for (int paletteIndex = 0; paletteIndex < DATA_VALUE_COUNT; paletteIndex++) {
            byte packedValue = values[createDataArrayElementIndex(paletteIndex)];
            byte value = extractValue(packedValue, paletteIndex);

            short valueCount = 1;
            if (valueCountMap.containsKey(value))
                valueCount += valueCountMap.get(value);

            valueCountMap.put(value, valueCount);
        }
        return valueCountMap;
    }

    /* In Minecraft one light value data array stores two light values, so calculating position of an element
       is the palette element index divided by 2. The position in a bit however depends on whether the index is even.
       --------------------------------------------------------------------------------------------------------------
       For odd numbers light value is stored on the right half of a bit, for even numbers however it is stored
       on the left side. */

    private static int createDataArrayElementIndex(int paletteElementIndex) {
        return paletteElementIndex / 2; // One element in the array stores 2 values
    }

    private static byte extractValue(byte lightElement, int paletteElementIndex) {
        if (paletteElementIndex % 2 == 0)
            return (byte) (lightElement >>> VALUE_BITS);
        return (byte) (lightElement & MAX_VALUE_ALLOWED);
    }

    private static byte updatedValue(byte lightElement, byte value, int paletteElementIndex) {
        if (paletteElementIndex % 2 == 0) {
            lightElement &= MAX_VALUE_ALLOWED;
            lightElement |= (byte) (value << VALUE_BITS);
            return lightElement;
        }

        lightElement &= (byte) (MAX_VALUE_ALLOWED << VALUE_BITS);
        lightElement |= value;
        return lightElement;
    }
}