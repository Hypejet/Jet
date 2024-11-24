package net.hypejet.jet.server.acquisition.collection;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain AbstractCollectionAcquirable an abstract collection acquirable},
 * which holds {@linkplain Set a set} using {@linkplain HashSet a hash set} implementation.
 *
 * @param <V> a type of value of the set
 * @since 1.0
 * @author Codestech
 * @see AbstractCollectionAcquirable
 */
public final class HashSetAcquirable<V> extends AbstractCollectionAcquirable<V, Set<V>> {
    /**
     * Constructs the {@linkplain HashSetAcquirable set acquirable} without default values.
     *
     * @since 1.0
     */
    public HashSetAcquirable() {
        this(null);
    }

    /**
     * Constructs the {@linkplain HashSetAcquirable set acquirable}.
     *
     * @param defaultValuesCollection a collection that contains default values, which should be added to
     *                                the collection, {@code null} if none
     * @since 1.0
     */
    public HashSetAcquirable(@Nullable Collection<V> defaultValuesCollection) {
        super(defaultValuesCollection);
    }

    @Override
    protected @NonNull Set<V> createMutableCollection() {
        return new HashSet<>();
    }

    @Override
    protected @NonNull Set<V> createImmutableView(@NonNull Set<V> mutableCollection) {
        return Collections.unmodifiableSet(mutableCollection);
    }
}