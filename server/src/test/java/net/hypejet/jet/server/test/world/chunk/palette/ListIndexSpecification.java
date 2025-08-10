package net.hypejet.jet.server.test.world.chunk.palette;

import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * A {@linkplain AbstractChunkPalette.IndexSpecification chunk-palette index specification}
 * specifying indices using a {@linkplain List list}.
 *
 * @param <V> the type of values whose indices this index specification specifies
 * @since 1.0
 * @see List
 */
public final class ListIndexSpecification<V> implements AbstractChunkPalette.IndexSpecification<V> {

    private final List<V> list;

    /**
     * Constructs the {@linkplain ListIndexSpecification list index specification}.
     *
     * @param list a list that the index specification should specify indices with
     * @since 1.0
     */
    public ListIndexSpecification(@NonNull List<V> list) {
        this.list = List.copyOf(Objects.requireNonNull(list, "list"));
    }

    @Override
    public int indexFor(@NonNull V value) {
        int index = this.list.indexOf(value);
        if (index == -1)
            throw new IllegalArgumentException("This index specification has no index for the specified value");
        return index;
    }

    @Override
    public @NonNull V valueByIndex(int index) {
        V value = this.list.get(index);
        if (value == null)
            throw new IllegalArgumentException("This index specification has no value for the specified index");
        return value;
    }
}