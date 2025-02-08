package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

/**
 * Represents a storage, which stores light for each block state
 * of {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 */
public sealed abstract class LightStorage permits DirectLightStorage, EmptyLightStorage {

    /**
     * A maximum value that a light value can be.
     *
     * @since 1.0
     */
    public static final int MAX_VALUE_ALLOWED = 15;

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

    /**
     * Creates a new copy of this {@linkplain LightStorage light storage} with
     * {@linkplain LightStorageUpdate light updates} specified applied.
     *
     * @param updates the light updates
     * @return the copy
     * @since 1.0
     */
    @Contract(pure = true)
    public final @NonNull LightStorage withUpdates(@NonNull Collection<LightStorageUpdate> updates) {
        NullabilityUtil.requireNonNull(updates, "updates");

        if (updates.isEmpty())
            return this;

        byte[] data = this.data();
        Byte2ShortMap valueCountMap = new Byte2ShortOpenHashMap(this.valueCountMap());

        boolean lightChanged = false;

        for (LightStorageUpdate update : updates) {
            int paletteElementIndex = ChunkPalette.calculateElementIndex(update.position());

            int dataIndex = createDataArrayElementIndex(paletteElementIndex);
            byte dataElement = data[dataIndex];

            byte previousValue = extractValue(dataElement, paletteElementIndex);
            byte newValue = update.value();

            if (previousValue == newValue) continue;
            if (!lightChanged) lightChanged = true;

            data[dataIndex] = insertValue(dataElement, newValue, paletteElementIndex);

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

        if (!lightChanged)
            return this;
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
     * Gets an array, which stores all light values of this storage, where one byte stores two values.
     *
     * @return the array
     * @since 1.0
     */
    @Contract(pure = true)
    protected abstract byte @NonNull [] data();

    /**
     * Gets a map, which maps all light values to their count in this storage.
     *
     * @return the map
     * @since 1.0
     */
    @Contract(pure = true)
    protected abstract @NonNull Byte2ShortMap valueCountMap();

    /**
     * Creates {@linkplain LightStorage a light storage} for light values specified.
     *
     * @param values the light values
     * @return the light storage
     * @since 1.0
     */
    public static @NonNull LightStorage create(byte @NonNull [] values) {
        NullabilityUtil.requireNonNull(values, "values");

        int length = values.length;
        if (length != DATA_VALUE_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Length of the value array specified is invalid (%d != %d)",
                    length, DATA_VALUE_COUNT
            ));
        }

        byte[] data = new byte[DATA_ARRAY_LENGTH];
        Byte2ShortMap valueCountMap = new Byte2ShortOpenHashMap();

        for (int paletteIndex = 0; paletteIndex < values.length; paletteIndex++) {
            int dataIndex = createDataArrayElementIndex(paletteIndex);
            byte value = values[paletteIndex];
            data[dataIndex] = insertValue(data[dataIndex], value, paletteIndex);

            short valueCount = 1;
            if (valueCountMap.containsKey(value))
                valueCount += valueCountMap.get(value);
            valueCountMap.put(value, valueCount);
        }

        return create(valueCountMap, data);
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

    private static @NonNull LightStorage create(@NonNull Byte2ShortMap valueCountMap, byte @NonNull [] data) {
        if (valueCountMap.size() == 1 && valueCountMap.containsKey((byte) 0))
            return EmptyLightStorage.INSTANCE;
        return new DirectLightStorage(data, valueCountMap);
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
            return (byte) (lightElement >> 4);
        return (byte) (lightElement & MAX_VALUE_ALLOWED);
    }

    private static byte insertValue(byte lightElement, byte value, int paletteElementIndex) {
        if (paletteElementIndex % 2 == 0)
            return (byte) (lightElement | (byte) (value << 4));
        return (byte) (lightElement | value);
    }
}