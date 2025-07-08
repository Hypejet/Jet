package net.hypejet.jet.data.json.model;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents something that holds a value.
 *
 * @param <V> a type of the held value
 * @since 1.0
 */
public sealed interface JsonHolder<V> {
    /**
     * Represents {@linkplain JsonHolder a holder} holding {@linkplain Key key} that the held value can be retrieved
     * with from a registry associated with it.
     *
     * @param key the key
     * @param <V> a type of the held value
     * @since 1.0
     */
    record Registry<V>(@NonNull Key key) implements JsonHolder<V> {
        /**
         * Constructs the {@linkplain Registry registry holder}.
         *
         * @param key the key
         * @since 1.0
         */
        public Registry {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * Represents {@linkplain JsonHolder a holder} directly holding the value.
     *
     * @param value the held value
     * @param <V> a type of the held value
     * @since 1.0
     */
    record Direct<V>(@NonNull V value) implements JsonHolder<V> {
        /**
         * Constructs the {@linkplain Direct direct holder}.
         *
         * @param value the held value
         * @since 1.0
         */
        public Direct {
            Objects.requireNonNull(value, "value");
        }
    }
}