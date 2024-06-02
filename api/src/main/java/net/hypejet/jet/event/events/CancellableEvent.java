package net.hypejet.jet.event.events;

import net.hypejet.jet.event.annotation.Subscribe;

/**
 * Represents any event that can be cancelled, this can be listened to but should not be used
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 * @see Subscribe
 */
public interface CancellableEvent {
    /**
     * Sets whether the event should be cancelled.
     *
     * @param cancel true if the event should be cancelled, false otherwise
     * @since 1.0
     */
    void setCancelled(boolean cancel);

    /**
     * Gets whether the event has been cancelled.
     *
     * @return true if the event was cancelled, false otherwise
     * @since 1.0
     */
    boolean isCancelled();
}