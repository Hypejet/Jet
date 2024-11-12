package net.hypejet.jet.server.acquisition;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.acquisition.value.AcquirableValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

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
     * Consumes a value from this acquirable.
     *
     * <p>If current thread already created an acquisition then it is being used, however this method will not
     * unlock it. It will be unlocked only if the acquisition was created specifically for the consumption.</p>
     *
     * @param consumer the consumer that should consume the value
     * @since 1.0
     */
    public final void consume(@NonNull Consumer<V> consumer) {
        NullabilityUtil.requireNonNull(consumer, "consumer");

        Acquisition<V> acquisition = this.findAcquisition();
        boolean shouldAcquireAndUnlock = acquisition == null;

        if (shouldAcquireAndUnlock)
            acquisition = this.acquire();

        try {
            consumer.accept(acquisition.get());
        } finally {
            if (shouldAcquireAndUnlock)
                acquisition.unlock();
        }
    }

    /**
     * Finds {@linkplain Acquisition an acquisition} (owned by the calling thread) of the value.
     *
     * @return the acquisition
     * @since 1.0
     */
    public final @Nullable Acquisition<V> findAcquisition() {
        try {
            this.acquisitionsLock.lock();
            return this.acquisitions.get(Thread.currentThread());
        } finally {
            this.acquisitionsLock.unlock();
        }
    }

    /**
     * Creates {@linkplain Acquisition an immutable acquisition} of a value held by
     * this {@linkplain AcquirableValue acquirable}.
     *
     * @return the immutable acquisition
     * @since 1.0
     * @throws IllegalStateException if the thread has already created an acquisition
     */
    public abstract @NonNull Acquisition<V> acquire();

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

            this.lock.lock();
            acquirable.registerAcquisition(this);
        }

        @Override
        public final void unlock() {
            this.runChecks();
            this.unlocked = true;
            this.acquirable.unregisterAcquisition(this);
            this.lock.unlock();
        }

        @Override
        public final void unlockIfNotUnlocked() {
            if (!this.isUnlocked())
                this.unlock();
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
}