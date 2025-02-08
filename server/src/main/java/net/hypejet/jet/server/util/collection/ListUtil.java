package net.hypejet.jet.server.util.collection;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a utility for {@linkplain List list} management.
 *
 * @since 1.0
 * @see List
 */
public final class ListUtil {

    private ListUtil() {}

    /**
     * Creates {@linkplain ArrayList an array list}, which is filled with an element specified number of times
     * specified.
     *
     * @param element the element
     * @param size the number of times
     * @return the array list
     * @param <E> a type of the element
     * @since 1.0
     */
    public static <E> @NonNull List<E> filledArrayList(@NonNull E element, int size) {
        List<E> list = new ArrayList<>(size);
        for (int index = 0; index < size; index++)
            list.add(index, element);
        return list;
    }
}