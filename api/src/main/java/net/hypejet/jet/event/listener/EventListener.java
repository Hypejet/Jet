package net.hypejet.jet.event.listener;

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
 */
public sealed interface EventListener<E> extends Predicate<E>, Consumer<E>, Comparable<EventListener<?>>
        permits EventListenerImpl {
    /**
     * Calls the listener with an {@code event}.
     *
     * @param event the event
     * @since 1.0
     */
    void call(E event);

    /**
     * Gets a type of event that this listener listens to.
     *
     * @return the type
     * @since 1.0
     */
    Class<? extends E> eventClass();

    /**
     * Gets a priority of the listener.
     *
     * @return the priority
     * @since 1.0
     */
    int priority();

    /**
     * Gets whether an event is eligible to call this listener.
     *
     * @param event the event
     * @return true if the event is eligible to call this listener, false otherwise
     */
    boolean isEligible(E event);

    /**
     * {@inheritDoc}
     *
     * @see #isEligible(Object)
     */
    @Override
    boolean test(E event);

    /**
     * {@inheritDoc}
     *
     * @see #call(Object)
     */
    @Override
    void accept(E event);

    /**
     * {@inheritDoc}
     */
    @Override
    int compareTo(@NonNull EventListener<?> listener);

    /**
     * Creates a new {@linkplain EventListener event listener} with a predicate, which always returns {@code true}
     * a priority of {@code 0}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass) {
        return listener(eventConsumer, eventClass, null);
    }

    /**
     * Creates a new {@linkplain EventListener event listener} with a priority of {@code 0}.
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
        return listener(eventConsumer, eventClass, eventPredicate, 0);
    }

    /**
     * Creates a new {@linkplain EventListener event listener} with a predicate, which always returns {@code true}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     * @param priority a priority of the event listener
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass,
                                         int priority) {
        return listener(eventConsumer, eventClass, null, priority);
    }

    /**
     * Creates a new {@linkplain EventListener event listener}.
     *
     * @param eventClass a class of an event that the event listener should listen to
     * @param eventConsumer a consumer of that consumes called events
     * @param eventPredicate a predicate that checks whether an event called is eligible to be called by the event
     *                       listener
     * @param priority a priority of the event listener
     *
     * @return the event listener
     * @param <E> a type of event that the event listener listens to
     * @since 1.0
     */
    static <E> EventListener<E> listener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass,
                                         @Nullable Predicate<E> eventPredicate, int priority) {
        return new EventListenerImpl<>(eventConsumer, eventClass, eventPredicate, priority);
    }
}