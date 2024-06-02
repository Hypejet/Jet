package net.hypejet.jet.event.node;

import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Represents an implementation of an {@linkplain EventNode event node}.
 *
 * @since 1.0
 * @author Codestech
 * @see EventNode
 */
final class EventNodeImpl implements EventNode {

    private final int priority;

    private final Collection<EventListener<?>> listeners = new CopyOnWriteArraySet<>();
    private final Collection<EventNode> children = new CopyOnWriteArraySet<>();

    /**
     * Creates an {@linkplain EventNodeImpl implementation of the event node}.
     *
     * @param priority a priority of the event node
     * @since 1.0
     */
    EventNodeImpl(int priority) {
        this.priority = priority;
    }

    @Override
    public @NotNull EventNode addChild(@NotNull EventNode node) {
        this.children.add(node);
        return this;
    }

    @Override
    public @NotNull EventNode addListener(@NotNull EventListener<?> listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public @This <E> @NotNull EventNode addListener(@NotNull Consumer<E> eventConsumer,
                                                    @NotNull Class<? extends E> eventClass) {
        return this.addListener(EventListener.listener(eventConsumer, eventClass));
    }

    @Override
    public @NotNull EventNode addListener(@NotNull Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Subscribe subscription = method.getAnnotation(Subscribe.class);
            if (subscription == null) continue;

            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) continue;

            Class<?> eventParameter = parameters[0];

            this.addListener(EventListener.listener(event -> {
                if (!method.canAccess(listener)) {
                    method.setAccessible(true);
                }

                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }, eventParameter, subscription.priority()));
        }

        return this;
    }

    @Override
    public @NotNull EventNode call(@NotNull Object event) {
        List<EventListener<?>> copiedListeners = new ArrayList<>(this.listeners);
        Collections.sort(copiedListeners);
        copiedListeners.forEach(listener -> this.call(listener, event));

        List<EventNode> copiedChildren = new ArrayList<>(this.children);
        Collections.sort(copiedChildren);
        copiedChildren.forEach(node -> node.call(event));

        return this;
    }

    @Override
    public @NonNull Collection<EventListener<?>> listeners() {
        return Set.copyOf(this.listeners);
    }

    @Override
    public @NonNull Collection<EventNode> children() {
        return Set.copyOf(this.children);
    }

    @Override
    public int priority() {
        return this.priority;
    }

    @Override
    public int compareTo(EventNode node) {
        return Integer.compare(node.priority(), this.priority());
    }

    private <E> void call(EventListener<E> listener, Object event) {
        Class<? extends E> listenerEventClass = listener.eventClass();

        if (!listenerEventClass.isAssignableFrom(event.getClass())) return;

        E castEvent = listenerEventClass.cast(event);
        if (!listener.isEligible(castEvent)) return;

        listener.call(castEvent);
    }
}
