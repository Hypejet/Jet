package net.hypejet.jet.acquisition.collection;

import net.hypejet.jet.acquisition.Acquisition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Represents an acquisition, which allows making changes to {@linkplain Collection a collection}.
 *
 * @param <V> a type of value of the collection
 * @param <C> a type of the collection
 * @since 1.0
 * @author Codestech
 * @see Collection
 */
public interface MutableCollectionAcquisition<V, C extends Collection<V>> extends Acquisition<C> {
    /**
     * Adds a value to the collection.
     *
     * @param value the value
     * @return {@code true} if the change was made, {@code false} otherwise
     * @since 1.0
     * @see Collection#add(Object)
     */
    boolean add(@NonNull V value);

    /**
     * Removes a value from the collection.
     *
     * @param value the value
     * @return {@code true} if the change was made, {@code false} otherwise
     * @since 1.0
     * @see Collection#remove(Object)
     */
    boolean remove(@NonNull V value);

    /**
     * Adds all values to the collection from another collection.
     *
     * @param collection the other collection
     * @return {@code true} if the change was made, {@code false} otherwise
     * @since 1.0
     * @see Collection#addAll(Collection)
     */
    boolean addAll(@NonNull Collection<? extends V> collection);

    /**
     * Removes all values from the collection, which are present in another collection.
     *
     * @param collection the other collection
     * @return {@code true} if the change was made, {@code false} otherwise
     * @since 1.0
     * @see Collection#removeAll(Collection)
     */
    boolean removeAll(@NonNull Collection<? extends V> collection);

    /**
     * Removes all values from the collection, which satisfy a predicate.
     *
     * @param predicate the predicate
     * @return {@code true} if the change was made, {@code false} otherwise
     * @since 1.0
     * @see Collection#removeIf(Predicate)
     */
    boolean removeIf(@NonNull Predicate<V> predicate);

    /**
     * Removes all values from the collection.
     *
     * @since 1.0
     * @see Collection#clear()
     */
    void clear();
}