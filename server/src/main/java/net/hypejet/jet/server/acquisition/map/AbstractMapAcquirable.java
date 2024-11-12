package net.hypejet.jet.server.acquisition.map;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.map.MutableMapAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.acquisition.AbstractAcquirable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents {@linkplain AbstractAcquirable an abstract acquirable}, which holds {@linkplain Map a map}.
 *
 * @param <K> a type of key of the map
 * @param <V> a type of value of the map
 * @param <M> a type of the map
 * @since 1.0
 * @author Codestech
 * @see Map
 * @see AbstractAcquirable
 */
abstract class AbstractMapAcquirable<K, V, M extends Map<K, V>> extends AbstractAcquirable<M> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final M map;
    private final M immutableView;

    /**
     * Constructs the {@linkplain AbstractMapAcquirable abstract map acquirable}.
     *
     * @param defaultEntries a map of default entries that should be added to the map of the acquirable, {@code null}
     *                       if none
     * @since 1.0
     */
    protected AbstractMapAcquirable(@Nullable Map<? extends K, ? extends V> defaultEntries) {
        this.map = this.createMutableMap();
        if (defaultEntries != null)
            this.map.putAll(defaultEntries);
        this.immutableView = this.createImmutableView(this.map);
    }

    @Override
    public @NonNull Acquisition<M> acquire() {
        return new ImmutableMapAcquisition<>(this);
    }

    /**
     * Creates {@linkplain MutableMapAcquisition a mutable map acquisition} of the map.
     *
     * @return the acquisition
     * @since 1.0
     */
    public final @NonNull MutableMapAcquisition<K, V, M> acquireMutable() {
        return new MutableMapAcquisitionImpl<>(this);
    }

    /**
     * Creates a mutable map of the type specified in this acquirable.
     *
     * @return the map
     * @since 1.0
     */
    protected abstract @NonNull M createMutableMap();

    /**
     * Creates an immutable view of a mutable map of the type specified in this acquirable.
     *
     * @param mutableMap the mutable map
     * @return the immutable view
     * @since 1.0
     */
    protected abstract @NonNull M createImmutableView(@NonNull M mutableMap);

    /**
     * Represents an implementation of {@linkplain AbstractMapAcquisition an abstract map acquisition}, which does
     * not allow to do write operations.
     *
     * @param <K> a type of key of the map
     * @param <V> a type of value of the map
     * @param <M> a type of the map
     * @since 1.0
     * @see AbstractMapAcquisition
     */
    private static final class ImmutableMapAcquisition<K, V, M extends Map<K, V>>
            extends AbstractMapAcquisition<K, V, M> {
        /**
         * Constructs the {@linkplain ImmutableMapAcquisition immutable map acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @since 1.0
         */
        private ImmutableMapAcquisition(@NonNull AbstractMapAcquirable<K, V, M> acquirable) {
            super(acquirable, acquirable.lock.readLock());
        }
    }

    /**
     * Represents an implementation of {@linkplain AbstractMapAcquisition an abstract map acquisition}
     * and {@linkplain MutableMapAcquisition mutable map acquisition}.
     *
     * @param <K> a type of key of the map
     * @param <V> a type of value of the map
     * @param <M> a type of the map
     * @since 1.0
     * @see AbstractMapAcquisition
     */
    private static final class MutableMapAcquisitionImpl<K, V, M extends Map<K, V>>
            extends AbstractMapAcquisition<K, V, M> implements MutableMapAcquisition<K, V, M> {
        /**
         * Constructs the {@linkplain MutableMapAcquisitionImpl mutable map acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @since 1.0
         */
        private MutableMapAcquisitionImpl(@NonNull AbstractMapAcquirable<K, V, M> acquirable) {
            super(acquirable, acquirable.lock.writeLock());
        }

        @Override
        public @Nullable V put(@NonNull K key, @NonNull V value) {
            this.runChecks();
            return this.acquirable.map.put(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(value, "value"));
        }

        @Override
        public @Nullable V remove(@NonNull K key) {
            this.runChecks();
            return this.acquirable.map.remove(NullabilityUtil.requireNonNull(key, "key"));
        }

        @Override
        public boolean remove(@NonNull K key, @NonNull V value) {
            this.runChecks();
            return this.acquirable.map.remove(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(value, "value"));
        }

        @Override
        public boolean replace(@NonNull K key, @Nullable V oldValue, @NonNull V newValue) {
            this.runChecks();
            return this.acquirable.map.replace(NullabilityUtil.requireNonNull(key, "key"), oldValue,
                    NullabilityUtil.requireNonNull(newValue, "new value"));
        }

        @Override
        public @Nullable V replace(@NonNull K key, @NonNull V value) {
            this.runChecks();
            return this.acquirable.map.replace(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(value, "value"));
        }

        @Override
        public void putAll(@NonNull Map<? extends K, ? extends V> map) {
            this.runChecks();
            this.acquirable.map.putAll(NullabilityUtil.requireNonNull(map, "map"));
        }

        @Override
        public @Nullable V putIfAbsent(@NonNull K key, @NonNull V value) {
            this.runChecks();
            return this.acquirable.map.putIfAbsent(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(value, "value"));
        }

        @Override
        public void clear() {
            this.runChecks();
            this.acquirable.map.clear();
        }

        @Override
        public void merge(@NonNull K key, @NonNull V value, @NonNull BiFunction<V, V, ? extends V> remappingFunction) {
            this.runChecks();
            this.acquirable.map.merge(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(value, "value"),
                    NullabilityUtil.requireNonNull(remappingFunction, "remapping function"));
        }

        @Override
        public void replaceAll(@NonNull BiFunction<K, V, ? extends V> function) {
            this.runChecks();
            this.acquirable.map.replaceAll(NullabilityUtil.requireNonNull(function, "function"));
        }

        @Override
        public @Nullable V computeIfAbsent(@NonNull K key, @NonNull Function<K, ? extends V> mappingFunction) {
            this.runChecks();
            return this.acquirable.map.computeIfAbsent(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(mappingFunction, "mapping function"));
        }

        @Override
        public @Nullable V computeIfPresent(@NonNull K key, @NonNull BiFunction<K, V, ? extends V> remappingFunction) {
            this.runChecks();
            return this.acquirable.map.computeIfPresent(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(remappingFunction, "remapping function"));
        }

        @Override
        public @Nullable V compute(@NonNull K key, @NonNull BiFunction<K, V, ? extends V> remappingFunction) {
            this.runChecks();
            return this.acquirable.map.compute(NullabilityUtil.requireNonNull(key, "key"),
                    NullabilityUtil.requireNonNull(remappingFunction, "remapping function"));
        }
    }

    /**
     * Represents {@linkplain AbstractAcquirable.AbstractAcquisition an abstract acquisition}, which
     * holds {@linkplain Map a map}.
     *
     * @param <K> a type of key of the map
     * @param <V> a type of value of the map
     * @param <M> a type of the map
     * @since 1.0
     * @see AbstractAcquirable.AbstractAcquisition
     */
    private static abstract class AbstractMapAcquisition<K, V, M extends Map<K, V>> extends AbstractAcquisition<M> {

        protected final AbstractMapAcquirable<K, V, M> acquirable;

        /**
         * Constructs the {@linkplain AbstractAcquisition abstract acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @param lock       the lock to acquire
         * @since 1.0
         */
        protected AbstractMapAcquisition(@NonNull AbstractMapAcquirable<K, V, M> acquirable, @NonNull Lock lock) {
            super(acquirable, lock);
            this.acquirable = acquirable;
        }

        @Override
        public @NonNull M get() {
            return this.acquirable.immutableView;
        }
    }
}