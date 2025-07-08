package net.hypejet.jet.data.json.model;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents holder of a value with a weight chance for selecting it.
 *
 * @param value the value
 * @param weight the weight chance
 * @param <V> a type of the value
 * @since 1.0
 */
public record JsonWeighted<V>(@NonNull V value, int weight) {
    /**
     * Constructs the {@linkplain JsonWeighted weighted}.
     *
     * @param value the value
     * @param weight the weight chance
     */
    public JsonWeighted {
        Objects.requireNonNull(value, "value");
    }
}