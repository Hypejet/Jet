package net.hypejet.jet.server.util.collection;

import io.netty.util.collection.IntCollections;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import org.jspecify.annotations.NonNull;

/**
 * A builder of an {@linkplain IntObjectMap int-object map}.
 *
 * @param <V> the value type that the int-object map should have
 * @since 1.0
 * @see IntObjectMap
 */
public final class IntObjectMapBuilder<V> {

    private final IntObjectMap<V> map = new IntObjectHashMap<>();

    /**
     * Associates the specified key with the specified value.
     *
     * @param key the key which should be associated with the specified value
     * @param value the value that the specified key should be associated with
     * @return this builder
     * @since 1.0
     */
    public @NonNull IntObjectMapBuilder<V> put(int key, @NonNull V value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * Builds the {@linkplain IntObjectMap int-object map}.
     *
     * @return the created unmodifiable int-object map
     * @since 1.0
     */
    public @NonNull IntObjectMap<V> build() {
        IntObjectMap<V> mapCopy = new IntObjectHashMap<>(this.map.size());
        mapCopy.putAll(this.map);
        return IntCollections.unmodifiableMap(mapCopy);
    }
}