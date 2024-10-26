package net.hypejet.jet.server.acquisition;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents something that protects a value that is guarded by a {@linkplain Lock lock} that requires to be acquired
 * before getting or settings the value.
 *
 * @param <V> a type of the value
 * @since 1.0
 * @author Codestech
 */
public class Acquirable<V> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private V value;

    private final Map<Thread, AbstractAcquisition<V>> acquisitions = new IdentityHashMap<>();
    private final ReentrantLock acquisitionsLock = new ReentrantLock();

    /**
     * Constructs the {@linkplain Acquirable acquirable}.
     *
     * @param initialValue an initial value of the acquirable
     * @since 1.0
     */
    public Acquirable(@NonNull V initialValue) {
        this.value = initialValue;
    }

    /**
     * Creates {@linkplain Acquisition an immutable acquisition} of a value held by
     * this {@linkplain Acquirable acquirable}.
     *
     * <p>If the thread acquiring has already acquired the value, the already-existing acquisition is returned,
     * even if it is mutable.</p>
     *
     * @return the immutable acquisition
     * @since 1.0
     */
    public final @NonNull Acquisition<V> acquire() {
        Acquisition<V> acquisition = this.findAcquisition();
        if (acquisition == null) return new ImmutableAcquisitionImpl<>(this);
        return acquisition;
    }

    /**
     * Creates {@linkplain MutableAcquisition a mutable acquisition} of a value held by
     * this {@linkplain Acquirable acquirable}.
     *
     * <p>If the thread acquiring has already acquired the value, the already-existing acquisition is returned.</p>
     *
     * @return the immutable acquisition
     * @since 1.0
     * @throws IllegalStateException if the thread already created an acquisition, but it is immutable
     */
    public final @NonNull MutableAcquisition<V> acquireMutable() {
        Acquisition<V> acquisition = this.findAcquisition();
        if (acquisition == null) return new MutableAcquisitionImpl<>(this);
        if (acquisition instanceof MutableAcquisition<V> mutableAcquisition) return mutableAcquisition;
        throw new IllegalStateException("The thread already acquired the value, but the acquisition is immutable");
    }

    /**
     * Gets the value.
     *
     * <p>This should be only called by {@linkplain Acquisition acquisitions} as it exists only for overriding.</p>
     *
     * @return the value
     * @since 1.0
     */
    protected @NonNull V get() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * <p>This should be only called by {@linkplain Acquisition acquisitions} as it exists only for overriding.</p>
     *
     * @param value the new value
     * @since 1.0
     */
    protected void set(@NonNull V value) {
        this.value = NullabilityUtil.requireNonNull(value, "value");
    }

    /**
     * Finds {@linkplain Acquisition an acquisition} - owned by this thread - of the value.
     *
     * @return the acquisition
     * @since 1.0
     */
    private @Nullable Acquisition<V> findAcquisition() {
        try {
            this.acquisitionsLock.lock();
            return this.acquisitions.get(Thread.currentThread());
        } finally {
            this.acquisitionsLock.unlock();
        }
    }

    /**
     * Registers an acquisition.
     *
     * @param acquisition the acquisition
     * @since 1.0
     */
    private void registerAcquisition(@NonNull AbstractAcquisition<V> acquisition) {
        try {
            this.acquisitionsLock.lock();
            this.acquisitions.put(acquisition.owner, acquisition);
        } finally {
            this.acquisitionsLock.unlock();
        }
    }

    /**
     * Unregisters an acquisition.
     *
     * @param acquisition the acquisition
     * @since 1.0
     */
    private void unregisterAcquisition(@NonNull AbstractAcquisition<V> acquisition) {
        try {
            this.acquisitionsLock.lock();
            NullabilityUtil.requireNonNull(acquisition, "acquisition");
        } finally {
            this.acquisitionsLock.unlock();
        }
    }

    /**
     * Represents an abstract implementation of {@linkplain Acquisition acquisition}.
     *
     * @param <V> a type of the value
     * @since 1.0
     * @see Acquisition
     */
    private static abstract class AbstractAcquisition<V> implements Acquisition<V> {

        protected final Acquirable<V> acquirable;

        private final Thread owner;
        private final Lock lock;

        private boolean unlocked;

        /**
         * Constructs the {@linkplain AbstractAcquisition abstract acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by the lock
         * @param lock the lock to acquire
         * @since 1.0
         */
        protected AbstractAcquisition(@NonNull Acquirable<V> acquirable, @NonNull Lock lock) {
            this.acquirable = NullabilityUtil.requireNonNull(acquirable, "acquirable");
            this.owner = Thread.currentThread();
            this.lock = NullabilityUtil.requireNonNull(lock, "lock");

            this.lock.lock();
            acquirable.registerAcquisition(this);
        }

        @Override
        public @NonNull V get() {
            this.runChecks();
            return this.acquirable.get();
        }

        @Override
        public void unlock() {
            this.runChecks();
            this.unlocked = true;
            this.acquirable.unregisterAcquisition(this);
            this.lock.unlock();
        }

        /**
         * Checks whether the calling thread owns the acquisition and whether the acquisition has been not already
         * unlocked.
         *
         * @since 1.0
         */
        protected void runChecks() {
            if (Thread.currentThread() != this.owner)
                throw new IllegalArgumentException("The calling thread does not own the acquisition");
            if (this.unlocked)
                throw new IllegalStateException("The acquisition has been already unlocked");
        }
    }

    /**
     * Represents an immutable implementation of {@linkplain AbstractAcquisition an abstract acquisition}.
     *
     * @param <V> a type of the value
     * @since 1.0
     */
    private static final class ImmutableAcquisitionImpl<V> extends AbstractAcquisition<V> {
        /**
         * Constructs the {@linkplain ImmutableAcquisitionImpl immutable acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by a lock
         * @since 1.0
         */
        private ImmutableAcquisitionImpl(@NonNull Acquirable<V> acquirable) {
            super(acquirable, acquirable.lock.readLock());
        }
    }

    /**
     * Represents an implementation of {@linkplain MutableAcquisition a mutable acquisition}
     * and {@linkplain AbstractAcquisition an abstract acquisition}.
     *
     * @param <V> a type of the value
     * @since 1.0
     */
    private static final class MutableAcquisitionImpl<V> extends AbstractAcquisition<V>
            implements MutableAcquisition<V> {
        /**
         * Constructs the {@linkplain ImmutableAcquisitionImpl immutable acquisition}.
         *
         * @param acquirable an acquirable whose value is guarded by a lock
         * @since 1.0
         */
        private MutableAcquisitionImpl(@NonNull Acquirable<V> acquirable) {
            super(acquirable, acquirable.lock.writeLock());
        }

        @Override
        public void set(@NonNull V value) {
            this.runChecks();
            this.acquirable.set(value);
        }
    }
}