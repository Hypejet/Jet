package net.hypejet.jet.event.node;

import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Represents something that manages events.
 *
 * @param <E> a type of event that this event node handles
 * @since 1.0
 * @author Codestech
 */
public sealed interface EventNode<E> extends Comparable<EventNode<?>> permits EventNodeImpl {
    /**
     * Adds a {@linkplain EventNode event node} as a child of this node.
     *
     * @param node the event node
     * @return this node
     * @since 1.0
     * @see EventNode
     */
    @This
    @NonNull EventNode<E> addChild(@NonNull EventNode<? extends E> node);

    /**
     * Adds an {@linkplain EventListener event listener} to this {@linkplain EventNode event node}.
     *
     * @param listener the listener
     * @return this node
     * @since 1.0
     * @see EventListener
     */
    @This
    @NonNull EventNode<E> addListener(@NonNull EventListener<? extends E> listener);

    /**
     * Creates and adds an {@linkplain EventListener event listener} to this {@linkplain EventNode event node}.
     *
     * @param eventConsumer a consumer of the event
     * @param eventClass a class of the event that the listener should listen to
     * @param <T> a type of event that the listener should handle
     * @return this node
     * @since 1.0
     * @see EventListener
     */
    @This
    <T extends E> @NonNull EventNode<E> addListener(@NonNull Consumer<T> eventConsumer,
                                                    @NonNull Class<? extends T> eventClass);

    /**
     * Adds an annotation-based listener.
     *
     * @param listener the listener
     * @return this node
     * @since 1.0
     * @see net.hypejet.jet.event.annotation.Subscribe
     */
    @This
    @NonNull EventNode<E> addListener(@NonNull Object listener);

    /**
     * Calls an event.
     *
     * @param event the event
     * @return this node
     * @since 1.0
     */
    @This
    @NonNull EventNode<E> call(@NonNull E event);

    /**
     * Gets listeners that were registered to this {@linkplain EventNode event node}.
     *
     * @return the listeners
     * @since 1.0
     */
    @NonNull Collection<EventListener<? extends E>> listeners();

    /**
     * Gets children {@linkplain EventNode event nodes} of this node.
     *
     * @return the children event nodes
     * @since 1.0
     */
    @NonNull Collection<EventNode<? extends E>> children();

    /**
     * Gets a priority of this {@linkplain EventNode event node}.
     *
     * @return the event priority
     * @since 1.0
     */
    @NonNull EventPriority priority();

    /**
     * Gets a class of an event that this {@linkplain EventNode event node} handles/
     *
     * @return the class
     * @since 1.0
     */
    @NonNull Class<E> eventClass();

    /**
     * Creates an event node with a type of {@link Object} and a priority of {@linkplain EventPriority#NORMAL}.
     *
     * @return the event node
     * @since 1.0
     */
    static @NonNull EventNode<Object> create() {
        return create(Object.class, EventPriority.NORMAL);
    }

    /**
     * Creates an event node with a priority of {@linkplain EventPriority#NORMAL}.
     *
     * @param eventClass a class of an event that the event node should handle
     * @param <E> a type of event that the event node should handle
     * @return the event node
     * @since 1.0
     */
    static <E> @NonNull EventNode<E> create(@NonNull Class<E> eventClass) {
        return create(eventClass, EventPriority.NORMAL);
    }

    /**
     * Creates an event node.
     *
     * @param eventClass a class of an event that the event node should handle
     * @param priority an {@linkplain EventPriority event priority} of the event node
     * @param <E> a type of event that the event node should handle
     * @return the event node
     * @since 1.0
     */
    static <E> @NonNull EventNode<E> create(@NonNull Class<E> eventClass, @NonNull EventPriority priority) {
        return new EventNodeImpl<>(eventClass, priority);
    }
}