package net.hypejet.jet.event.listener;

import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents an implementation of {@linkplain EventListener event listener}.
 *
 * @param consumer a consumer that consumes called events
 * @param eventClass a class of an event that this listener consumes
 * @param predicate a predicate that checks if this listener is eligible to call
 * @param priority a {@linkplain EventPriority event priority} of this listener
 * @param <E> the type of event that this listener listens to
 * @author Codestech
 * @author Window5
 * @see EventListener
 * @since 1.0
 */
record EventListenerImpl<E>(@NonNull Consumer<E> consumer, @NonNull Class<? extends E> eventClass,
                            @Nullable Predicate<E> predicate, @NonNull EventPriority priority)
        implements EventListener<E> {
    @Override
    public void call(@NonNull E event) {
        this.consumer.accept(event);
    }

    @Override
    public boolean isEligible(@NonNull E event) {
        if (this.predicate == null) return true;
        return this.predicate.test(event);
    }
}