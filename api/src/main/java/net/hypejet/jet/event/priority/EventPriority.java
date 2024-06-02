package net.hypejet.jet.event.priority;

import net.hypejet.jet.event.annotation.Subscribe;

/**
 * Used to specify which order event listeners should be called, used on {@link Subscribe}
 * <p>
 * LOWEST will be called first, HIGHEST will be called last.
 * <p>
 * SERVER should only be used internally by Jet.
 * <p>
 * Note that events on the same priority may not be called in a reliable order
 *
 * @since 1.0
 * @author Window5
 * @see Subscribe
 */
public enum EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    SERVER,
    HIGH,
    HIGHEST
}
