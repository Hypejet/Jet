package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.NibbleArray;
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
     * Represents count of light values that one light storage has.
     *
     * @since 1.0
     */
    protected static final short DATA_VALUE_COUNT = ChunkPaletteType.BLOCK_STATE.elementCount();

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

        NibbleArray.Builder arrayBuilder = this.data().toBuilder();
        Byte2ShortMap valueCountMap = new Byte2ShortOpenHashMap(this.valueCountMap());

        boolean lightChanged = false;
        for (LightStorageUpdate update : updates) {
            ChunkPaletteRelativePosition position = update.position();

            byte previousValue = this.getValue(position);
            byte newValue = update.value();

            if (previousValue == newValue) continue;
            if (!lightChanged) lightChanged = true;

            arrayBuilder.set(AbstractChunkPalette.calculateElementIndex(position), newValue);

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
        return create(valueCountMap, arrayBuilder.build());
    }

    /**
     * Gets a light-level value for a block state
     * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
     *
     * @param position the position
     * @return the light value
     * @since 1.0
     */
    public abstract byte getValue(@NonNull ChunkPaletteRelativePosition position);

    /**
     * Gets a map, which maps all light-level values to their count in this storage.
     *
     * @return the map
     * @since 1.0
     */
    @Contract(pure = true)
    protected abstract @NonNull Byte2ShortMap valueCountMap();

    /**
     * Creates {@linkplain AbstractLightStorage a light storage} for {@linkplain NibbleArray a nibble array}
     * of light-level values specified.
     *
     * @param array the nibble array
     * @return the light storage
     * @since 1.0
     */
    public static @NonNull AbstractLightStorage create(@NonNull NibbleArray array) {
        NullabilityUtil.requireNonNull(array, "array");
        long length = array.length();

        if (length != DATA_VALUE_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Length of the light nibble array specified is invalid (%d != %d)",
                    length, DATA_VALUE_COUNT
            ));
        }

        return create(createCountMap(array), array);
    }

    private static @NonNull AbstractLightStorage create(@NonNull Byte2ShortMap valueCountMap,
                                                        @NonNull NibbleArray array) {
        if (valueCountMap.size() == 1 && valueCountMap.containsKey((byte) 0))
            return EmptyLightStorage.INSTANCE;
        return new DirectLightStorage(array, valueCountMap);
    }

    private static @NotNull Byte2ShortMap createCountMap(@NonNull NibbleArray array) {
        Byte2ShortMap valueCountMap = new Byte2ShortOpenHashMap();
        for (int paletteIndex = 0; paletteIndex < DATA_VALUE_COUNT; paletteIndex++) {
            byte value = array.get(paletteIndex);

            short valueCount = 1;
            if (valueCountMap.containsKey(value))
                valueCount += valueCountMap.get(value);

            valueCountMap.put(value, valueCount);
        }
        return valueCountMap;
    }
}