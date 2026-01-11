package net.hypejet.jet.server.util.collection;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jspecify.annotations.NullMarked;

/**
 * A builder of an {@linkplain Object2IntMap object-to-int map}.
 *
 * @param <K> the type of keys that the object-to-int map should have
 * @since 1.0
 * @see Object2IntMap
 */
@NullMarked
public final class Object2IntMapBuilder<K> {

    private final Object2IntMap<K> map = new Object2IntOpenHashMap<>();

    /**
     * Associates the specified key with the specified value.
     *
     * @param key the key that should be associated with the specified value
     * @param value the value that the specified key should be associated with
     * @return this builder
     * @since 1.0
     */
    public Object2IntMapBuilder<K> put(K key, int value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * Builds the {@linkplain Object2IntMap object-to-int map}.
     *
     * @return the created unmodifiable object-to-int map
     * @since 1.0
     */
    public Object2IntMap<K> build() {
        return Object2IntMaps.unmodifiable(new Object2IntOpenHashMap<>(this.map));
    }
}