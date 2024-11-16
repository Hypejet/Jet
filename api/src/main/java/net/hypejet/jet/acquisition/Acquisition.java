package net.hypejet.jet.acquisition;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an acquisition of a {@linkplain java.util.concurrent.locks.Lock lock}. Acquisitions allow getting
 * and values guarded by their locks safely in multithreaded environments.
 *
 * <p>{@linkplain #close()} A close method} should be called when the value was consumed and the work using it
 * has finished.</p>
 *
 * @param <V> a type of the value
 * @since 0
 * @author Codestech
 */
public interface Acquisition<V> extends AutoCloseable {
    /**
     * Gets the value.
     *
     * @return the value
     * @since 1.0
     * @throws IllegalStateException if the acquisition has been already unlocked
     * @throws IllegalArgumentException if the caller thread does not own the acquisition
     */
    @NonNull V get();

    /**
     * Gets whether the {@linkplain Acquisition acquisition} has been unlocked.
     *
     * @return {@code true} if the acquisition has been unlocked, {@code false} otherwise
     * @since 1.0
     * @throws IllegalArgumentException if the caller thread does not own the acquisition
     */
    boolean isUnlocked();

    /**
     * Unlocks the {@linkplain Acquisition acquisition}, does nothing if the acquisition has been already unlocked.
     *
     * @since 1.0
     * @throws IllegalArgumentException if the caller thread does not own the acquisition
     */
    @Override
    void close();
}