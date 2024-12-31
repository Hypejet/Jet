package net.hypejet.jet.event.events;

/**
 * Represents an event that can be cancelled.
 *
 * <p>Cancelled event state informs caller to stop executing the task after when the event is called and handled.</p>
 *
 * @since 1.0
 */
public abstract class CancellableEvent {

    private boolean cancel;

    /**
     * Sets whether the event should be cancelled.
     *
     * @param cancel {@code} if the event should be cancelled, {@code} otherwise
     * @since 1.0
     */
    public final void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets whether the event has been cancelled.
     *
     * @return {@code} if the event was cancelled, {@code} otherwise
     * @since 1.0
     */
    public final boolean isCancelled() {
        return this.cancel;
    }
}