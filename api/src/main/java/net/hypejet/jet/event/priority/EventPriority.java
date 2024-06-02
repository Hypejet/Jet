package net.hypejet.jet.event.priority;

import net.hypejet.jet.event.annotation.Subscribe;

/**
 * Used to specify which order event listeners should be called, used on {@link Subscribe}
 * <p>
 * {@link EventPriority#FIRST} will be called first, {@link EventPriority#LAST} will be called last.
 * <p>
 * {@link EventPriority#SERVER} should only be used internally by listeners of a Jet server.
 * <p>
 * Note that events on the same priority may not be called in a reliable order
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 * @see Subscribe
 * @see net.hypejet.jet.event.listener.EventListener
 */
public enum EventPriority {
    FIRST,
    EARLY,
    NORMAL,
    SERVER,
    LATE,
    LAST
}