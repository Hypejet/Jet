package net.hypejet.jet.server.util.collection;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a utility for {@linkplain Set a set} management.
 *
 * @since 1.0
 * @see Set
 */
public final class SetUtil {

    private SetUtil() {}

    /**
     * Creates an immutable {@linkplain Set set} by copying a minuend {@linkplain Set set} and removing all elements
     * from it that are in a subtrahend {@linkplain Set set}.
     *
     * @param minuend the minuend set
     * @param subtrahend the subtrahend set
     * @return the subtracted set
     * @param <E> a type of elements that the set should have
     * @since 1.0
     */
    public static <E> @NonNull Set<E> subtract(@NonNull Set<E> minuend, @NonNull Set<E> subtrahend) {
        HashSet<E> subtractedSet = new HashSet<>(minuend);
        subtractedSet.removeAll(subtrahend);
        return subtractedSet;
    }
}