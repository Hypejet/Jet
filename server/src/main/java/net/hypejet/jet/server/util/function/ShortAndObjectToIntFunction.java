package net.hypejet.jet.server.util.function;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a functional interface that returns an integer and requires a short and an object arguments
 * to be provided.
 *
 * @param <E> a type of the object argument
 * @since 1.0
 */
@FunctionalInterface
public interface ShortAndObjectToIntFunction<E> {
    /**
     * Applies the function.
     *
     * @param firstValue the short argument
     * @param secondValue the object argument
     * @return the integer
     * @since 1.0
     */
    int apply(short firstValue, @NonNull E secondValue);
}