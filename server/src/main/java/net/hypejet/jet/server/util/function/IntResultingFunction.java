package net.hypejet.jet.server.util.function;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a functional interface that returns an integer for a value specified.
 *
 * @param <V> a type of the value
 * @since 1.0
 */
@FunctionalInterface
public interface IntResultingFunction<V> {
    /**
     * Creates an integer result for a value specified.
     *
     * @param value the value
     * @return the integer result
     * @since 1.0
     */
    int apply(@NonNull V value);
}