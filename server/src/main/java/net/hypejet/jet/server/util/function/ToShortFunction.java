package net.hypejet.jet.server.util.function;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a functional interface that returns a short for a value specified.
 *
 * @param <V> a type of the value
 * @since 1.0
 */
@FunctionalInterface
public interface ToShortFunction<V> {
    /**
     * Creates a short result for a value specified.
     *
     * @param value the value
     * @return the short result
     * @since 1.0
     */
    short apply(@NonNull V value);
}