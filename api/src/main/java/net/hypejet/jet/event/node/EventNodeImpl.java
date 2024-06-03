package net.hypejet.jet.event.node;

import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.annotation.Subscribe;
import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents an implementation of an {@linkplain EventNode event node}.
 *
 * @param <E> a type of event that this event node handles
 * @since 1.0
 * @author Codestech
 * @see EventNode
 */
final class EventNodeImpl<E> implements EventNode<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventNode.class);

    private final Class<E> eventClass;
    private final EventPriority priority;

    private final Collection<EventListener<? extends E>> listeners = new CopyOnWriteArraySet<>();
    private final Collection<EventNode<? extends E>> children = new CopyOnWriteArraySet<>();

    /**
     * Creates an {@linkplain EventNodeImpl implementation of the event node}.
     *
     * @param eventClass a class of an event that this event node handles
     * @param priority a {@linkplain EventPriority event priority} of the event node
     * @since 1.0
     */
    EventNodeImpl(@NonNull Class<E> eventClass, @NonNull EventPriority priority) {
        this.eventClass = eventClass;
        this.priority = priority;
    }

    @Override
    public @NotNull EventNode<E> addChild(@NotNull EventNode<? extends E> node) {
        if (!this.eventClass.isAssignableFrom(node.eventClass())) {
            LOGGER.error("You cannot add a child to an event node, of which an event class is not assignable " +
                    "from an event class of the child", new IllegalArgumentException(node.toString()));
            return this;
        }

        if (this.children().contains(node)) {
            LOGGER.error("An event node is already added as a child of this node",
                    new IllegalArgumentException(node.toString()));
            return this;
        }

        if (this == node) {
            LOGGER.error("You cannot add an event node as a child of itself",
                    new IllegalArgumentException(node.toString()));
            return this;
        }

        this.children.add(node);
        return this;
    }

    @Override
    public @This @NonNull EventNode<E> removeChild(@NonNull EventNode<? extends E> node) {
        this.children.remove(node);
        return this;
    }

    @Override
    public @NotNull EventNode<E> addListener(@NotNull EventListener<? extends E> listener) {
        if (!this.eventClass.isAssignableFrom(listener.eventClass())) {
            LOGGER.error("You cannot add a listener in an event node, of which an event class is not assignable " +
                    "from an event class of the listener", new IllegalArgumentException(listener.toString()));
            return this;
        }
        this.listeners.add(listener);
        return this;
    }

    @Override
    public @This <T extends E> @NonNull EventListener<T> addListener(@NonNull Consumer<T> eventConsumer,
                                                                     @NonNull Class<? extends T> eventClass) {
        EventListener<T> listener = EventListener.listener(eventConsumer, eventClass);
        this.addListener(listener);
        return listener;
    }

    @Override
    public @This @NonNull EventNode<E> removeListener(@NonNull EventListener<? extends E> listener) {
        this.listeners.remove(listener);
        return this;
    }

    @Override
    public @NotNull EventNode<E> addListener(@NotNull Object listener) {
        Class<?> listenerClass = listener.getClass();
        String listenerClassName = listenerClass.getSimpleName();

        for (Method method : listenerClass.getDeclaredMethods()) {
            Subscribe subscription = method.getAnnotation(Subscribe.class);
            if (subscription == null) continue;

            String methodName = method.getName();
            Class<?>[] parameters = method.getParameterTypes();

            if (parameters.length != 1) {
                LOGGER.error("Could not register an event with method of \"{}#{}\", because length of its parameters" +
                        " is not 1", listenerClass, methodName, new IllegalArgumentException());
                continue;
            }

            Class<?> eventType = parameters[0];

            if (!eventType.isAssignableFrom(this.eventClass) && !this.eventClass.isAssignableFrom(eventType)) {
                LOGGER.error("Could not register an event listener with method of \"{}#{}\", because none of event " +
                                "classes - \"{}\" and \"{}\" - is assignable from each other", listenerClassName,
                        methodName, eventType.getSimpleName(), this.eventClass.getSimpleName(),
                        new IllegalArgumentException());
                continue;
            }

            this.addListener(EventListener.listener(event -> {
                if (!method.canAccess(listener)) {
                    method.setAccessible(true);
                }

                try {
                    if (eventType.isAssignableFrom(event.getClass())) {
                        method.invoke(listener, event);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }, this.eventClass, subscription.priority()));
        }

        return this;
    }

    @Override
    public @NotNull EventNode<E> call(@NotNull E event) {
        if (!this.eventClass.isAssignableFrom(event.getClass())) {
            LOGGER.error("You cannot call an event in an event node, of which event class is not assignable from an " +
                    "event class of the event", new IllegalArgumentException(event.toString()));
            return this;
        }

        List<EventListener<? extends E>> copiedListeners = new ArrayList<>(this.listeners);
        Collections.sort(copiedListeners);
        copiedListeners.forEach(listener -> this.callListener(listener, event));

        List<EventNode<? extends E>> copiedChildren = new ArrayList<>(this.children);
        Collections.sort(copiedChildren);
        copiedChildren.forEach(node -> this.callChild(node, event));

        return this;
    }

    @Override
    public @NonNull Collection<EventListener<? extends E>> listeners() {
        return Set.copyOf(this.listeners);
    }

    @Override
    public @NonNull Collection<EventNode<? extends E>> children() {
        return Set.copyOf(this.children);
    }

    @Override
    public @NotNull EventPriority priority() {
        return this.priority;
    }

    @Override
    public @NonNull Class<E> eventClass() {
        return this.eventClass;
    }

    @Override
    public int compareTo(@NonNull EventNode<?> node) {
        return Integer.compare(this.priority().ordinal(), node.priority().ordinal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventNodeImpl<?> eventNode)) return false;

        return Objects.equals(this.eventClass, eventNode.eventClass)
                && this.priority == eventNode.priority
                && Objects.equals(this.listeners, eventNode.listeners)
                && Objects.equals(this.children, eventNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.eventClass, this.priority, this.listeners, this.children);
    }

    @Override
    public String toString() {
        return "EventNodeImpl{" +
                "eventClass=" + this.eventClass.getName() +
                ", priority=" + this.priority +
                ", listeners=" + this.listeners +
                ", children=" + this.children +
                '}';
    }

    private <T extends E> void callListener(@NonNull EventListener<T> listener, @NonNull E event) {
        Class<? extends T> listenerEventClass = listener.eventClass();

        if (!listenerEventClass.isAssignableFrom(event.getClass())) return;

        T castEvent = listenerEventClass.cast(event);

        Predicate<T> predicate = listener.predicate();
        if (predicate != null && !predicate.test(castEvent)) return;

        try {
            listener.consumer().accept(castEvent);
        } catch (Throwable throwable) {
            // Plugins may throw an uncaught exception
            LOGGER.error("An error occurred while calling an event", throwable);
        }
    }

    private <T extends E> void callChild(@NonNull EventNode<T> node, @NonNull E event) {
        Class<T> nodeEventClass = node.eventClass();
        if (!nodeEventClass.isAssignableFrom(event.getClass())) return;
        node.call(nodeEventClass.cast(event));
    }
}