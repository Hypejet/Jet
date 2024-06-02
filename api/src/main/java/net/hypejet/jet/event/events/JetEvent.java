package net.hypejet.jet.event.events;

import net.hypejet.jet.event.annotation.Subscribe;

/**
 * Represents any event, this can be listened to but will be called for every event so should not be used
 *
 * @since 1.0
 * @author Window5
 * @see Subscribe
 * @see CancellableEvent
 */
public class JetEvent {
    protected boolean cancelled = false;

    /**
     * Forces an event to be cancelled, meaning that it won't be handled by any listeners after this (including Jet). VERY UNSAFE, should NOT be used. This WILL lead to problems.
     * <p>
     * Only events extending {@link CancellableEvent} should be cancelled.
     *
     * @see CancellableEvent
     * @author Window5
     * @since 1.0
     * */
    public void forceCancel() {
        cancelled = true;
    }
}
