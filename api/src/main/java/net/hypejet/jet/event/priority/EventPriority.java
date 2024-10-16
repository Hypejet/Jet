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
    /**
     * Represents an {@linkplain EventPriority event priority}.
     *
     * <p>Listeners of this type are called first.</p>
     *
     * @since 1.0
     */
    FIRST,
    /**
     * Represents an {@linkplain EventPriority event priority}.
     *
     * <p>Listeners of this type are called second.</p>
     *
     * @since 1.0
     */
    EARLY,
    /**
     * Represents an {@linkplain EventPriority event priority}.
     *
     * <p>Listeners of this type are called third.</p>
     *
     * @since 1.0
     */
    NORMAL,
    /**
     * Represents an {@linkplain EventPriority event priority}.
     *
     * <p>Listeners of this type are called fourth.</p>
     *
     * @since 1.0
     */
    SERVER,
    /**
     * Represents an {@linkplain EventPriority event priority}.
     *
     * <p>Listeners of this type are called fifth.</p>
     *
     * @since 1.0
     */
    LATE,
    /**
     * Represents an {@linkplain EventPriority event priority}.
     *
     * <p>Listeners of this type are called last.</p>
     *
     * @since 1.0
     */
    LAST
}