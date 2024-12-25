package net.hypejet.jet.server.util.acquirable.map.longs;

import io.netty.util.collection.LongCollections;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.MapAcquisition;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents an implementation of {@linkplain MapAcquirable an map acquirable}, which guards
 * {@linkplain LongObjectHashMap a long-object hash map}.
 *
 * @param <V> a type of value of the map
 * @since 1.0
 * @see MapAcquirable
 */
public final class LongObjectHashMapAcquirable<V> extends MapAcquirable<Long, V, LongObjectMap<V>> {
    /**
     * Constructs the {@linkplain LongObjectHashMapAcquirable long-object hash map acquirable} with no initial
     * elements.
     * 
     * @since 1.0
     */
    public LongObjectHashMapAcquirable() {}

    /**
     * Constructs the {@linkplain LongObjectHashMapAcquirable long-object hash map acquirable}
     *
     * @param initialEntries a map of entries that should be added to the map during initialization, {@code null} if
     *                       none
     * @since 1.0
     */
    public LongObjectHashMapAcquirable(@Nullable Map<Long, V> initialEntries) {
        super(initialEntries);
    }

    @Override
    protected @NotNull LongObjectMap<V> createMap(@Nullable Map<Long, V> initialEntries) {
        LongObjectMap<V> map = new LongObjectHashMap<>();
        if (initialEntries != null)
            map.putAll(initialEntries);
        return map;
    }

    @Override
    protected @NotNull LongObjectMap<V> createReadOnlyView(@NotNull LongObjectMap<V> map) {
        return LongCollections.unmodifiableMap(map);
    }

    @Override
    protected @NotNull LongObjectMap<V> createGuardedView(@NotNull LongObjectMap<V> map,
                                                          @NotNull MapAcquisition<Long, V, LongObjectMap<V>> acquisition) {
        return new GuardedLongObjectMap<>(map, acquisition);
    }
}