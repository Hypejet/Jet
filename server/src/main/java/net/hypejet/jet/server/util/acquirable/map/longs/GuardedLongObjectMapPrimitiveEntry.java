package net.hypejet.jet.server.util.acquirable.map.longs;

import io.netty.util.collection.LongObjectMap;
import net.hypejet.concurrency.Acquisition;
import net.hypejet.concurrency.util.guard.GuardedObject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain LongObjectMap.PrimitiveEntry a long-object map primitive entry} wrapper, which ensures that
 * {@linkplain Acquisition an acquisition} is locked and a caller thread has a permission to it during doing any
 * operation.
 *
 * @param <V> a type of value of the guarded map
 * @since 1.0
 * @see Acquisition
 * @see LongObjectMap
 */
public class GuardedLongObjectMapPrimitiveEntry<V, E extends LongObjectMap.PrimitiveEntry<V>> extends GuardedObject<E>
        implements LongObjectMap.PrimitiveEntry<V> {
    /**
     * Constructs the {@linkplain GuardedLongObjectMapPrimitiveEntry guarded long-object map primitive entry}.
     *
     * @param delegate the long-object map primitive entry that should be wrapped
     * @param acquisition an acquisition that should guard the long-object map primitive entry
     * @since 1.0
     */
    public GuardedLongObjectMapPrimitiveEntry(@NotNull E delegate, @NotNull Acquisition acquisition) {
        super(delegate, acquisition);
    }

    @Override
    public final long key() {
        this.acquisition.ensurePermittedAndLocked();
        return this.delegate.key();
    }

    @Override
    public final V value() {
        this.acquisition.ensurePermittedAndLocked();
        return this.delegate.value();
    }

    @Override
    public final void setValue(V value) {
        this.acquisition.ensurePermittedAndLocked();
        this.delegate.setValue(value);
    }
}
