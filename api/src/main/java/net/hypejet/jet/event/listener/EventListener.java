package net.hypejet.jet.event.listener;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents something that consumes {@linkplain E an event}.
 *
 * @param <E> the type of event that the event listener consumes
 * @since 1.0
 */
public final class EventListener<E> implements Comparable<EventListener<?>> {

    private final Consumer<E> consumer;
    private final Class<? extends E> eventClass;
    private final EventPriority priority;

    private final Predicate<E> predicate;

    /**
     * Constructs the {@linkplain EventListener event listener} with no predicate and
     * {@linkplain EventPriority#NORMAL a normal event priority}.
     *
     * @param consumer a consumer that should consume events that the event listener is called with
     * @param eventClass a class of an event that the event listener should consume
     * @since 1.0
     */
    public EventListener(@NonNull Consumer<E> consumer, @NonNull Class<? extends E> eventClass) {
        this(consumer, eventClass, (Predicate<E>) null);
    }

    /**
     * Constructs the {@linkplain EventListener event listener} with {@linkplain EventPriority#NORMAL a normal event
     * priority}.
     *
     * @param consumer a consumer that should consume events that the event listener is called with
     * @param eventClass a class of an event that the event listener should consume
     * @param predicate a predicate, which should check whether an event is eligible to call the event listener,
     *                  {@code null} if all events with the class specified should be eligible to call the event
     *                  listener
     * @since 1.0
     */
    public EventListener(@NonNull Consumer<E> consumer, @NonNull Class<? extends E> eventClass,
                         @Nullable Predicate<E> predicate) {
        this(consumer, eventClass, EventPriority.NORMAL, predicate);
    }

    /**
     * Constructs the {@linkplain EventListener event listener} with no predicate.
     *
     * @param consumer a consumer that should consume events that the event listener is called with
     * @param eventClass a class of an event that the event listener should consume
     * @param priority an event priority that the event listener should have
     * @since 1.0
     */
    public EventListener(@NonNull Consumer<E> consumer, @NonNull Class<? extends E> eventClass,
                         @NonNull EventPriority priority) {
        this(consumer, eventClass, priority, null);
    }

    /**
     * Constructs the {@linkplain EventListener event listener}.
     *
     * @param consumer a consumer that should consume events that the event listener is called with
     * @param eventClass a class of an event that the event listener should consume
     * @param priority an event priority that the event listener should have
     * @param predicate a predicate, which should check whether an event is eligible to call the event listener,
     *                  {@code null} if all events with the class specified should be eligible to call the event
     *                  listener
     * @since 1.0
     */
    public EventListener(@NonNull Consumer<E> consumer, @NonNull Class<? extends E> eventClass,
                         @NonNull EventPriority priority, @Nullable Predicate<E> predicate) {
        this.consumer = NullabilityUtil.requireNonNull(consumer, "consumer");
        this.eventClass = NullabilityUtil.requireNonNull(eventClass, "event class");
        this.priority = NullabilityUtil.requireNonNull(priority, "event priority");
        this.predicate = predicate;
    }

    /**
     * Calls the event listener.
     *
     * @param event the event that caused the event listener to be called
     * @since 1.0
     */
    public void call(@NonNull E event) {
        this.consumer.accept(event);
    }

    /**
     * Gets a class of an event that this listener consumes.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<? extends E> eventClass() {
        return eventClass;
    }

    /**
     * Gets an event priority of this event listener.
     *
     * @return the event priority
     * @since 1.0
     */
    public @NonNull EventPriority priority() {
        return this.priority;
    }

    /**
     * Gets whether an event specified is eligible to call this listener.
     *
     * @param event the event
     * @return {@code true} if the event is eligible to call this listener, {@code false} otherwise
     * @since 1.0
     */
    public boolean isEligible(@NonNull E event) {
        return this.predicate == null || this.predicate.test(event);
    }

    @Override
    public int compareTo(@NotNull EventListener<?> o) {
        return Integer.compare(this.priority.ordinal(), o.priority.ordinal());
    }
}