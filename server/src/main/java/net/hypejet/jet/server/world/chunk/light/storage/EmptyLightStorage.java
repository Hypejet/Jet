package net.hypejet.jet.server.world.chunk.light.storage;

import it.unimi.dsi.fastutil.bytes.Byte2ShortMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortMaps;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain AbstractLightStorage a light storage}, which does not store light values directly and returns
 * {@code 0} in all cases a light value is being got.
 *
 * @since 1.0
 * @see AbstractLightStorage
 */
public final class EmptyLightStorage extends AbstractLightStorage {

    private static final byte EMPTY_VALUE = 0;
    private static final UnmodifiableByteArray EMPTY_ARRAY = new UnmodifiableByteArray(new byte[DATA_ARRAY_LENGTH]);

    /**
     * An instance of the {@linkplain EmptyLightStorage empty light storage}.
     *
     * @since 1.0
     */
    public static final EmptyLightStorage INSTANCE = new EmptyLightStorage();

    private EmptyLightStorage() {}

    @Override
    public byte getValue(@NonNull ChunkPaletteRelativePosition position) {
        return EMPTY_VALUE;
    }

    @Override
    public @NonNull UnmodifiableByteArray unmodifiableData() {
        return EMPTY_ARRAY;
    }

    @Override
    protected @NonNull Byte2ShortMap valueCountMap() {
        return Byte2ShortMaps.singleton(EMPTY_VALUE, DATA_VALUE_COUNT);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
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