package net.hypejet.jet.acquisition;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Acquisition an acquisition} whose value can be set.
 *
 * @param <V> a type of the value
 * @since 1.0
 * @author Codestech
 */
public interface MutableAcquisition<V> extends Acquisition<V> {
    /**
     * Sets the value.
     *
     * @param value the value
     * @since 1.0
     * @throws IllegalStateException if the acquisition has been already unlocked
     * @throws IllegalArgumentException if the caller thread does not own the acquisition
     */
    void set(@NonNull V value);
}