package net.hypejet.jet.data.json.model;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a set of {@linkplain JsonHolder holders}.
 *
 * @param <V> a value type of holders that this holder set store
 * @since 1.0
 */
public sealed interface JsonHolderSet<V> {
    /**
     * Represents a {@linkplain JsonHolderSet holder set} directly storing the holders in a {@linkplain List list}.
     *
     * @param contents the list storing the holders
     * @param <V> a value type of holders that this holder set store
     * @since 1.0
     */
    record Direct<V>(@NonNull List<JsonHolder<V>> contents) implements JsonHolderSet<V> {
        /**
         * Constructs the {@linkplain Direct direct holder set}.
         *
         * @param contents the list storing the holders
         * @since 1.0
         */
        public Direct {
            contents = List.copyOf(Objects.requireNonNull(contents, "contents"));
        }
    }

    /**
     * Represents a {@linkplain JsonHolderSet holder set} referencing to all holders bound to the specified tag.
     *
     * @param tag the key of the tag
     * @param <V> a value type of holders that this holder set store
     * @since 1.0
     */
    record Named<V>(@NonNull Key tag) implements JsonHolderSet<V> {
        /**
         * Constructs the {@linkplain Named named holder set}.
         *
         * @param tag the key of the tag
         * @since 1.0
         */
        public Named {
            Objects.requireNonNull(tag, "tag");
        }
    }
}