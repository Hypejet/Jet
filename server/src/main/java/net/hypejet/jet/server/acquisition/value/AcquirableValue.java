package net.hypejet.jet.server.acquisition.value;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.acquisition.AbstractAcquirable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents {@linkplain AbstractAcquirable an abstract acquirable}, which holds a value, which can be modified.
 *
 * @param <V> a type of the value
 * @since 1.0
 * @author Codestech
 */
public class AcquirableValue<V> extends AbstractAcquirable<V> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private @NonNull V value;

    /**
     * Constructs the {@linkplain AcquirableValue acquirable value}.
     *
     * @param initialValue an initial value
     * @since 1.0
     */
    public AcquirableValue(@NonNull V initialValue) {
        this.value = NullabilityUtil.requireNonNull(initialValue, "initial value");
    }

    /**
     * Creates {@linkplain MutableAcquisition a mutable acquisition} of a value held by
     * this {@linkplain AcquirableValue acquirable value}.
     *
     * @return the immutable acquisition
     * @since 1.0
     * @throws IllegalStateException if the caller thread is forbidden to create a mutable acquisition
     */
    public final @NonNull MutableAcquisition<V> acquireMutable() {
        return new MutableAcquisitionImpl<>(this);
    }

    @Override
    protected final @NonNull Acquisition<V> createAcquisition() {
        return new ImmutableAcquisitionImpl<>(this);
    }

    /**
     * Invoked when a value of this acquirable is set.
     *
     * @param value the new value
     * @since 1.0
     */
    protected void onSet(@NonNull V value) {}

    /**
     * Represents a function that initializes a value of {@linkplain AcquirableValue an acquirable value} giving access
     * to the initializer to the acquirable value.
     *
     * @param <V> a type of the value
     * @since 1.0
     * @see AcquirableValue
     */
    @FunctionalInterface
    public interface ValueInitializer<V> {
        /**
         * Initializes the value.
         *
         * @param acquirableValue the acquirable value
         * @return the value
         * @since 1.0
         */
        @NonNull V initialize(@NonNull AcquirableValue<V> acquirableValue);
    }

    /**
     * Represents an immutable implementation of {@linkplain AbstractValueAcquisition an abstract value acquisition}.
     *
     * @param <V> a type of the value
     * @since 1.0
     */
    private static final class ImmutableAcquisitionImpl<V> extends AbstractValueAcquisition<V> {
        /**
         * Constructs the {@linkplain ImmutableAcquisitionImpl immutable acquisition}.
         *
         * @param acquirableValue an acquirable value whose value is guarded by a lock
         * @since 1.0
         */
        private ImmutableAcquisitionImpl(@NonNull AcquirableValue<V> acquirableValue) {
            super(acquirableValue, acquirableValue.lock.readLock());
        }
    }

    /**
     * Represents an implementation of {@linkplain MutableAcquisition a mutable acquisition}
     * and {@linkplain AbstractValueAcquisition an abstract value acquisition}.
     *
     * @param <V> a type of the value
     * @since 1.0
     */
    private static final class MutableAcquisitionImpl<V> extends AbstractValueAcquisition<V>
            implements MutableAcquisition<V> {

        private final AcquirableValue<V> acquirableValue;

        /**
         * Constructs the {@linkplain ImmutableAcquisitionImpl immutable acquisition}.
         *
         * @param acquirableValue an acquirable value whose value is guarded by a lock
         * @since 1.0
         */
        private MutableAcquisitionImpl(@NonNull AcquirableValue<V> acquirableValue) {
            super(acquirableValue, acquirableValue.lock.writeLock());
            this.acquirableValue = acquirableValue;
        }

        @Override
        public void set(@NonNull V value) {
            this.runChecks();
            this.acquirableValue.value = value;
            this.acquirableValue.onSet(value);
        }
    }

    /**
     * Represents {@linkplain AbstractAcquirable.AbstractAcquisition an abstract acquisition}, which is handled
     * for a {@linkplain AcquirableValue acquirable value}.
     *
     * @param <V> a type of value that the acquirable holds
     * @since 1.0
     */
    private static class AbstractValueAcquisition<V> extends AbstractAcquirable.AbstractAcquisition<V> {

        private final AcquirableValue<V> acquirableValue;

        /**
         * Constructs the {@linkplain AbstractValueAcquisition abstract value acquisition}.
         *
         * @param acquirableValue an acquirable value that the acquisition should be handled for
         * @param lock a lock that should be locked
         * @since 1.0
         */
        private AbstractValueAcquisition(@NonNull AcquirableValue<V> acquirableValue, @NonNull Lock lock) {
            super(acquirableValue, lock);
            this.acquirableValue = NullabilityUtil.requireNonNull(acquirableValue, "acquirable");
        }

        @Override
        public final @NonNull V get() {
            this.runChecks();
            return this.acquirableValue.value;
        }
    }
}