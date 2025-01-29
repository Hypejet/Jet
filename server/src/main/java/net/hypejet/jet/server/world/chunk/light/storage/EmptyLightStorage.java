package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortMaps;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain LightStorage a light storage}, which does not store light values directly and returns
 * {@code 0} for all block state positions of
 * {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section} associated with the light
 * storage.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 * @see LightStorage
 */
public final class EmptyLightStorage extends LightStorage {

    private static final byte EMPTY_VALUE = 0;

    /**
     * An instance of the {@linkplain EmptyLightStorage empty light storage}.
     *
     * @since 1.0
     */
    public static final EmptyLightStorage INSTANCE = new EmptyLightStorage();

    private EmptyLightStorage() {}

    @Override
    public byte getValue(byte x, byte y, byte z) {
        return EMPTY_VALUE;
    }

    @Override
    protected byte @NonNull [] data() {
        return new byte[DATA_ARRAY_LENGTH];
    }

    @Override
    protected @NonNull Byte2ShortMap valueCountMap() {
        return Byte2ShortMaps.singleton(EMPTY_VALUE, DATA_VALUE_COUNT);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyLightStorage;
    }

    @Override
    public String toString() {
        return "EmptyLightStorage{}";
    }
}