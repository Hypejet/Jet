package net.hypejet.jet.server.acquisition.map;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an implementation of {@linkplain AbstractMapAcquirable an abstract map acquirable}, which
 * holds {@linkplain Map a map} using {@linkplain HashMap a hash map} implementation.
 *
 * @param <K> a type of key of the map
 * @param <V> a type of value of the map
 * @since 1.0
 * @author Codestech
 * @see AbstractMapAcquirable
 */
public final class HashMapAcquirable<K, V> extends AbstractMapAcquirable<K, V, Map<K, V>> {
    /**
     * Constructs the {@linkplain HashMapAcquirable hash map acquirable} with no default entries.
     *
     * @since 1.0
     */
    public HashMapAcquirable() {
        super(null);
    }

    /**
     * Constructs the {@linkplain HashMapAcquirable hash map acquirable}.
     *
     * @param defaultEntries a map of default entries that should be added to the map of the acquirable, {@code null}
     *                       if none
     * @since 1.0
     */
    public HashMapAcquirable(@Nullable Map<? extends K, ? extends V> defaultEntries) {
        super(defaultEntries);
    }

    @Override
    protected @NonNull Map<K, V> createMutableMap() {
        return new HashMap<>();
    }

    @Override
    protected @NonNull Map<K, V> createImmutableView(@NonNull Map<K, V> mutableMap) {
        return Collections.unmodifiableMap(mutableMap);
    }
}