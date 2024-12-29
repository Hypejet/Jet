package net.hypejet.jet.server.util.codec;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a function, which converts a value to another value.
 *
 * @param <R> a type of values to convert
 * @param <W> a type of converted values
 * @since 1.0
 */
@FunctionalInterface
public interface Writer<R, W> {
    /**
     * Converts a value.
     *
     * @param value a value to convert
     * @return the converted value
     * @since 1.0
     */
    @NonNull W write(@NonNull R value);
}