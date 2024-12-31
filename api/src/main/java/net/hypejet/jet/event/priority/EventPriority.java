package net.hypejet.jet.event.priority;

/**
 * Represents something that specifies in which order
 * {@linkplain net.hypejet.jet.event.listener.EventListener event listeners} should be called.
 *
 * <p>{@link EventPriority#FIRST} will be called first, {@link EventPriority#LAST} will be called last.</p>
 *
 * <p>Note that events on the same priority may not be called in a reliable order.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.event.listener.EventListener
 */
public enum EventPriority {
    /**
     * {@linkplain EventPriority An event priority}, whose listeners are called first.
     *
     * @since 1.0
     */
    FIRST,
    /**
     * {@linkplain EventPriority An event priority}, whose listeners are called second.
     *
     * @since 1.0
     */
    EARLY,
    /**
     * {@linkplain EventPriority An event priority}, whose listeners are called third.
     *
     * @since 1.0
     */
    NORMAL,
    /**
     * {@linkplain EventPriority An event priority}, whose listeners are called fourth.
     *
     * @since 1.0
     */
    LATE,
    /**
     * {@linkplain EventPriority An event priority}, whose listeners are called last.
     *
     * @since 1.0
     */
    LAST
}