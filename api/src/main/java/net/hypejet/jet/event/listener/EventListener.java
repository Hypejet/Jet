package net.hypejet.jet.event.listener;

import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents something that consumes a ran {@linkplain E event}.
 *
 * @param <E> a type of the event
 * @since 1.0
 * @author Codestech
 * @author Window5
 */
public interface EventListener<E> extends Comparable<EventListener<?>>, Consumer<E>, Predicate<E> {
    /**
     * Calls an {@linkplain E event}.
     *
     * @param event the event
     * @since 1.0
     */
    void call(@NonNull E event);

    /**
     * Gets a type of event that this listener listens to.
     *
     * @return the type
     * @since 1.0
     */
    @NonNull Class<? extends E> eventClass();

    /**
     * Gets an {@linkplain EventPriority event priority} of the listener.
     *
     * @return the priority
     * @since 1.0
     */
    default @NonNull EventPriority priority() {
        return EventPriority.NORMAL;
    }

    /**
     * Gets whether an event is eligible to call this listener.
     *
     * @param event the event
     * @return true if the event is eligible to call this listener, false otherwise
     * @since 1.0
     */
    default boolean isEligible(@NonNull E event) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(@NonNull EventListener<?> listener) {
        return Integer.compare(this.priority().ordinal(), listener.priority().ordinal());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void accept(@NonNull E event) {
        this.call(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(@NonNull E event) {
        return this.isEligible(event);
    }

    /**
     * Creates a new {@linkplain EventListener event listener} with a predicate, which always returns {@code true}
     * a priority of {@link EventPriority#NORMAL}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass) {
        return listener(eventConsumer, eventClass, (@Nullable Predicate<E>) null);
    }

    /**
     * Creates a new {@linkplain EventListener event listener} with a priority of {@link EventPriority#NORMAL}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     * @param eventPredicate a predicate that checks whether an event called is eligible to be called by the event
     *                       listener
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass,
                                         @Nullable Predicate<E> eventPredicate) {
        return listener(eventConsumer, eventClass, eventPredicate, EventPriority.NORMAL);
    }

    /**
     * Creates a new {@linkplain EventListener event listener} with a predicate, which always returns {@code true}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     * @param priority an {@linkplain EventPriority event priority} of the event listener
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass,
                                         @NonNull EventPriority priority) {
        return listener(eventConsumer, eventClass, null, priority);
    }

    /**
     * Creates a new {@linkplain EventListener event listener}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     * @param eventPredicate a predicate that checks whether an event called is eligible to be called by the event
     *                       listener
     * @param priority an {@linkplain EventPriority event priority} of the event listener
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass,
                                         @Nullable Predicate<E> eventPredicate, @NonNull EventPriority priority) {
        return new EventListenerImpl<>(eventConsumer, eventClass, eventPredicate, priority);
    }
}