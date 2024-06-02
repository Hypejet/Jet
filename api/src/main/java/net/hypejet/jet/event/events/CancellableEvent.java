package net.hypejet.jet.event.events;

import net.hypejet.jet.event.annotation.Subscribe;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents any event that can be cancelled, this can be listened to but should not be used
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 * @see Subscribe
 */
public abstract class CancellableEvent {

    private final AtomicBoolean cancel = new AtomicBoolean();

    /**
     * Sets whether the event should be cancelled.
     *
     * @param cancel true if the event should be cancelled, false otherwise
     * @since 1.0
     */
    public final void setCancelled(boolean cancel) {
        this.cancel.set(cancel);
    }

    /**
     * Gets whether the event has been cancelled.
     *
     * @return true if the event was cancelled, false otherwise
     * @since 1.0
     */
    public final boolean isCancelled() {
        return this.cancel.get();
    }
}