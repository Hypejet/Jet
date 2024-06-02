package net.hypejet.jet.event.listener;

import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents an implementation of {@linkplain EventListener event listener}.
 * 
 * @param <E> the type of event that this listener listens to
 * @since 1.0
 * @author Codestech, Window5
 * @see EventListener
 */
final class EventListenerImpl<E> implements EventListener<E> {
    
    private final Consumer<E> consumer;
    private final Predicate<E> predicate;
    
    private final Class<? extends E> eventClass;
    private final EventPriority priority;

    /**
     * Constructs an {@linkplain EventListenerImpl event listener implementation}.
     * 
     * @param consumer a consumer that consumes called events
     * @param eventClass a class of an event that this listener consumes
     * @param predicate a predicate that checks if this listener is eligible to call 
     * @param priority a priority of this listener
     * @since 1.0
     */
    EventListenerImpl(@NonNull Consumer<E> consumer, @NonNull Class<? extends E> eventClass,
                      @Nullable Predicate<E> predicate, EventPriority priority) {
        this.consumer = consumer;
        this.eventClass = eventClass;
        this.predicate = predicate;
        this.priority = priority;
    }
    
    @Override
    public void call(E event) {
        this.consumer.accept(event);
    }

    @Override
    public Class<? extends E> eventClass() {
        return this.eventClass;
    }

    @Override
    public EventPriority priority() {
        return this.priority;
    }

    @Override
    public boolean isEligible(E event) {
        if (this.predicate == null) return true;
        return this.predicate.test(event);
    }
    
    @Override
    public boolean test(E event) {
        return this.isEligible(event);
    }
    
    @Override
    public void accept(E event) {
        this.call(event);
    }
    
    @Override
    public int compareTo(@NonNull EventListener<?> listener) {
        return Integer.compare(listener.priority().ordinal(), this.priority().ordinal());
    }
}