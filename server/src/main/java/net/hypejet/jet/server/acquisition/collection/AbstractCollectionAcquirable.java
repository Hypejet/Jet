package net.hypejet.jet.server.acquisition.collection;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.collection.MutableCollectionAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.acquisition.AbstractAcquirable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * Represents {@linkplain AbstractAcquirable an abstract acquirable}, which holds {@linkplain Collection a collection}.
 *
 * @param <V> a type of value of the collection
 * @param <C> a type of the collection
 * @since 1.0
 * @author Codestech
 * @see AbstractAcquirable
 * @since 1.0
 */
abstract class AbstractCollectionAcquirable<V, C extends Collection<V>> extends AbstractAcquirable<C> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final C collection;
    private final C immutableView;

    /**
     * Constructs the {@linkplain AbstractCollectionAcquirable abstract collection acquirable}.
     *
     * @param defaultValuesCollection a collection that contains default values, which should be added to
     *                                the collection, {@code null} if none
     * @since 1.0
     */
    protected AbstractCollectionAcquirable(@Nullable Collection<V> defaultValuesCollection) {
        this.collection = NullabilityUtil.requireNonNull(this.createMutableCollection(), "collection");
        if (defaultValuesCollection != null)
            this.collection.addAll(defaultValuesCollection);

        this.immutableView = NullabilityUtil.requireNonNull(
                this.createImmutableView(this.collection), "immutable view"
        );
    }

    /**
     * Creates a mutable collection of the type specified in this acquirable.
     *
     * @return the mutable collection
     * @since 1.0
     */
    protected abstract @NonNull C createMutableCollection();

    /**
     * Creates an immutable view of a collection of the type specified in this acquirable.
     *
     * @param mutableCollection a mutable collection to create the immutable view with
     * @return the immutable view
     * @since 1.0
     */
    protected abstract @NonNull C createImmutableView(@NonNull C mutableCollection);

    @Override
    public final @NonNull Acquisition<C> acquire() {
        return new ImmutableCollectionAcquisitionImpl<>(this);
    }

    /**
     * Creates {@linkplain MutableCollectionAcquisition a mutable collection acquisition} of the collection.
     *
     * @return the mutable collection acquisition
     * @since 1.0
     */
    public final @NonNull MutableCollectionAcquisition<V, C> acquireMutable() {
        return new MutableCollectionAcquisitionImpl<>(this);
    }

    /**
     * Represents an implementation of {@linkplain AbstractCollectionAcquisition an abstract collection acquisition},
     * which grants immutable access to a collection.
     *
     * @param <V> a type of the value of the collection
     * @param <C> a type of the collection
     * @since 1.0
     * @see AbstractCollectionAcquisition
     */
    private static final class ImmutableCollectionAcquisitionImpl<V, C extends Collection<V>>
            extends AbstractCollectionAcquisition<V, C> {
        /**
         * Constructs the {@linkplain ImmutableCollectionAcquisitionImpl immutable collection acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @since 1.0
         */
        private ImmutableCollectionAcquisitionImpl(@NonNull AbstractCollectionAcquirable<V, C> acquirable) {
            super(acquirable, acquirable.lock.readLock());
        }
    }

    /**
     * Represents an implementation of {@linkplain AbstractCollectionAcquisition an abstract collection acquisition},
     * which grants mutable access to a collection.
     *
     * @param <V> a type of the value of the collection
     * @param <C> a type of the collection
     * @since 1.0
     * @see MutableCollectionAcquisition
     * @see AbstractCollectionAcquisition
     */
    private static final class MutableCollectionAcquisitionImpl<V, C extends Collection<V>>
            extends AbstractCollectionAcquisition<V, C> implements MutableCollectionAcquisition<V, C> {
        /**
         * Constructs the {@linkplain MutableCollectionAcquisitionImpl a mutable collection acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @since 1.0
         */
        private MutableCollectionAcquisitionImpl(@NonNull AbstractCollectionAcquirable<V, C> acquirable) {
            super(acquirable, acquirable.lock.writeLock());
        }

        @Override
        public boolean add(@NonNull V value) {
            this.runChecks();
            NullabilityUtil.requireNonNull(value, "value");
            return this.acquirable.collection.add(value);
        }

        @Override
        public boolean remove(@NonNull V value) {
            this.runChecks();
            NullabilityUtil.requireNonNull(value, "value");
            return this.acquirable.collection.remove(value);
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends V> collection) {
            this.runChecks();
            NullabilityUtil.requireNonNull(collection, "collection");
            return this.acquirable.collection.addAll(collection);
        }

        @Override
        public boolean removeAll(@NonNull Collection<? extends V> collection) {
            this.runChecks();
            NullabilityUtil.requireNonNull(collection, "collection");
            return this.acquirable.collection.removeAll(collection);
        }

        @Override
        public boolean removeIf(@NonNull Predicate<V> predicate) {
            this.runChecks();
            NullabilityUtil.requireNonNull(predicate, "predicate");
            return this.acquirable.collection.removeIf(predicate);
        }

        @Override
        public void clear() {
            this.runChecks();
            this.acquirable.collection.clear();
        }
    }

    /**
     * Represents {@linkplain AbstractAcquirable.AbstractAcquisition an abstract acquisition}, which
     * holds a collection.
     *
     * @param <V> a type of value of the collection
     * @param <C> a type of the collection
     * @since 1.0
     * @see AbstractAcquirable.AbstractAcquisition
     */
    private static abstract class AbstractCollectionAcquisition<V, C extends Collection<V>>
            extends AbstractAcquirable.AbstractAcquisition<C> {

        protected final AbstractCollectionAcquirable<V, C> acquirable;

        /**
         * Constructs the {@linkplain AbstractAcquisition abstract acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @param lock       the lock to acquire
         * @since 1.0
         */
        private AbstractCollectionAcquisition(@NonNull AbstractCollectionAcquirable<V, C> acquirable,
                                              @NonNull Lock lock) {
            super(acquirable, lock);
            this.acquirable = acquirable;
        }

        @Override
        public @NonNull C get() {
            this.runChecks();
            return this.acquirable.immutableView;
        }
    }
}