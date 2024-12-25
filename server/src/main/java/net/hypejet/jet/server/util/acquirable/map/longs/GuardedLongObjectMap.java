package net.hypejet.jet.server.util.acquirable.map.longs;

import io.netty.util.collection.LongObjectMap;
import net.hypejet.concurrency.Acquisition;
import net.hypejet.concurrency.util.guard.iterable.GuardedIterable;
import net.hypejet.concurrency.util.guard.map.GuardedMap;
import net.hypejet.concurrency.util.wrapping.iterable.ElementWrappingIterable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain LongObjectMap a long-object map} wrapper, which ensures that
 * {@linkplain Acquisition an acquisition} is locked and a caller thread has a permission to it during doing any
 * operation.
 *
 * @param <V> a type of value of the guarded map
 * @param <M> a type of the guarded map
 * @since 1.0
 * @see Acquisition
 * @see LongObjectMap
 */
public class GuardedLongObjectMap<V, M extends LongObjectMap<V>> extends GuardedMap<Long, V, M>
        implements LongObjectMap<V> {
    /**
     * Constructs the {@linkplain GuardedLongObjectMap guarded long-object map}.
     *
     * @param delegate the map that should be wrapped
     * @param acquisition an acquisition that should guard the map
     * @since 1.0
     */
    public GuardedLongObjectMap(@NotNull M delegate, @NotNull Acquisition acquisition) {
        super(delegate, acquisition);
    }

    @Override
    public final V get(long key) {
        this.acquisition.ensurePermittedAndLocked();
        return this.delegate.get(key);
    }

    @Override
    public final V put(long key, V value) {
        this.acquisition.ensurePermittedAndLocked();
        return this.delegate.put(key, value);
    }

    @Override
    public final V remove(long key) {
        this.acquisition.ensurePermittedAndLocked();
        return this.delegate.remove(key);
    }

    @Override
    public final Iterable<PrimitiveEntry<V>> entries() {
        this.acquisition.ensurePermittedAndLocked();
        return new GuardedIterable<>(
                new ElementWrappingIterable<>(
                        this.delegate.entries(),
                        element -> new GuardedLongObjectMapPrimitiveEntry<>(element, this.acquisition)
                ),
                this.acquisition
        );
    }

    @Override
    public final boolean containsKey(long key) {
        this.acquisition.ensurePermittedAndLocked();
        return this.delegate.containsKey(key);
    }
}