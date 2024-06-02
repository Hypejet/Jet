package net.hypejet.jet.event.events;

import net.hypejet.jet.event.annotation.Subscribe;

/**
 * Represents any event that can be cancelled, this can be listened to but should not be used
 *
 * @since 1.0
 * @author Window5
 * @see Subscribe
 */
public class CancellableEvent extends JetEvent {
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
