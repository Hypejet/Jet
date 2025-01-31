package net.hypejet.jet.server.util.function;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a functional interface that returns a byte for a value specified.
 *
 * @param <V> a type of the value
 * @since 1.0
 */
@FunctionalInterface
public interface ToByteFunction<V> {
    /**
     * Creates a byte result for a value specified.
     *
     * @param value the value
     * @return the byte result
     * @since 1.0
     */
    byte apply(@NonNull V value);
}