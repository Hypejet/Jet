package net.hypejet.jet.util.game.random;

import net.hypejet.jet.util.range.RangeUtil;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Holder of a value with a weight chance for selecting it.
 *
 * @param value the value
 * @param weight the weight chance
 * @param <V> a type of the value
 * @since 1.0
 */
public record Weighted<V>(@NonNull V value, int weight) {
    /**
     * Constructs the {@linkplain Weighted weighted}.
     *
     * @param value the value
     * @param weight the weight chance
     * @since 1.0
     */
    public Weighted {
        Objects.requireNonNull(value, "value");
        RangeUtil.ensureNotNegative(weight);
    }
}