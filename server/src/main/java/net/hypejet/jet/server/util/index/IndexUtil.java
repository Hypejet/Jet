package net.hypejet.jet.server.util.index;

import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Utilities for {@linkplain Index index} instance creation.
 *
 * @since 1.0
 * @see Index
 */
public final class IndexUtil {

    private IndexUtil() {}

    /**
     * Creates an {@linkplain Index index} from the specified map.
     *
     * @param map the map associating values with keys that the index should have
     * @return the created index
     * @param <K> the key type of the index
     * @param <V> the value type of the index
     * @since 1.0
     */
    public static <K, V> @NonNull Index<K, V> fromMap(@NonNull Map<V, K> map) {
        return Index.create(map::get, List.copyOf(map.keySet()));
    }
}