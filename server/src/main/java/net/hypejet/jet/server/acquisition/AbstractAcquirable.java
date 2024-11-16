package net.hypejet.jet.server.acquisition;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.acquisition.value.AcquirableValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents something that protects a value that is guarded by a {@linkplain Lock lock} that requires to be acquired
 * before getting or setting the value.
 *
 * @param <V> a type of the value
 * @since 1.0
 * @author Codestech
 */
public abstract class AbstractAcquirable<V> {

    private final Map<Thread, AbstractAcquisition<V>> acquisitions = new IdentityHashMap<>();
    private final ReentrantLock acquisitionsLock = new ReentrantLock();

    /**
     * Creates {@linkplain Acquisition an immutable acquisition} of a value held by
     * this {@linkplain AcquirableValue acquirable}.
     * </p>
     * If the caller thread already owns the acquisition a special implementation of an acquisition is used, which
     * reuses it and does nothing when {@link Acquisition#close()} is called. If the acquisition needs to be
     * unlocked the already existing acquisition needs to be used to do that.
     *
     * @return the immutable acquisition
     * @since 1.0
     */
    public final @NonNull Acquisition<V> acquire() {
        Acquisition<V> acquisition;
        try {
            this.acquisitionsLock.lock();
            acquisition = this.acquisitions.get(Thread.currentThread());
        } finally {
            this.acquisitionsLock.unlock();
        }

        /* We can safely call use the acquisition from the map outside the lock, because it is attached
           to the caller thread anyway. */
        if (acquisition == null) acquisition = this.createAcquisition();
        else acquisition = new ReusedAcquisition<>(acquisition);

        return acquisition;
    }

    /**
     * Creates {@linkplain Acquisition an immutable acquisition} specific to this acquirable implementation.
     *
     * @return the acquisition
     * @since 1.0
     */
    protected abstract @NonNull Acquisition<V> createAcquisition();

    /**
     * Registers an acquisition.
     *
     * @param acquisition the acquisition
     * @since 1.0
     * @throws IllegalArgumentException if an acquisition for current thread has been already registered
     */
    private void registerAcquisition(@NonNull AbstractAcquisition<V> acquisition) {
        try {
            this.acquisitionsLock.lock();

            Thread owner = acquisition.owner;
            if (this.acquisitions.containsKey(owner))
                throw new IllegalArgumentException("An acquisition for current thread has been already registered");

            this.acquisitions.put(owner, acquisition);
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
            this.acquisitions.remove(acquisition.owner, acquisition);
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
    protected static abstract class AbstractAcquisition<V> implements Acquisition<V> {

        protected final AbstractAcquirable<V> acquirable;

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
        protected AbstractAcquisition(@NonNull AbstractAcquirable<V> acquirable, @NonNull Lock lock) {
            this.acquirable = NullabilityUtil.requireNonNull(acquirable, "acquirable");
            this.owner = Thread.currentThread();
            this.lock = NullabilityUtil.requireNonNull(lock, "lock");

            acquirable.registerAcquisition(this);
            this.lock.lock();
        }

        @Override
        public final void close() {
            this.runChecks();

            if (this.unlocked) return;
            this.unlocked = true;

            this.acquirable.unregisterAcquisition(this);
            this.lock.unlock();
        }

        @Override
        public final boolean isUnlocked() {
            this.checkCallerThread();
            return this.unlocked;
        }

        /**
         * Checks whether the caller thread owns the acquisition and whether the acquisition has been not already
         * unlocked.
         *
         * @since 1.0
         */
        protected final void runChecks() {
            this.checkCallerThread();
            if (this.unlocked)
                throw new IllegalStateException("The acquisition has been already unlocked");
        }

        /**
         * Checks whether the caller thread owns the acquisition.
         *
         * @since 1.0
         */
        protected final void checkCallerThread() {
            if (Thread.currentThread() != this.owner)
                throw new IllegalArgumentException("The caller thread does not own the acquisition");
        }
    }

    /**
     * Represents {@linkplain Acquisition an acquisition}, which reuses another acquisition, which was already created
     * for a thread, which requested the acquisition to be created.
     *
     * @param acquisition the reused acquisition
     * @param <V> a type of value of the reused acquisition
     * @since 1.0
     * @see Acquisition
     */
    private record ReusedAcquisition<V>(@NonNull Acquisition<V> acquisition) implements Acquisition<V> {
        /**
         * Constructs the {@linkplain ReusedAcquisition reused acquisition}.
         *
         * @param acquisition the reused acquisition
         * @since 1.0
         */
        private ReusedAcquisition {
            NullabilityUtil.requireNonNull(acquisition, "acquisition");
        }

        @Override
        public @NonNull V get() {
            return this.acquisition.get();
        }

        @Override
        public void close() {
            // NOOP
        }

        @Override
        public boolean isUnlocked() {
            return this.acquisition.isUnlocked();
        }
    }
}