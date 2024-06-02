package net.hypejet.jet.event.node;

import net.hypejet.jet.event.listener.EventListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Represents something that manages events.
 *
 * @since 1.0
 * @author Codestech
 */
public sealed interface EventNode extends Comparable<EventNode> permits EventNodeImpl {
    /**
     * Adds a {@linkplain EventNode event node} as a child of this node.
     *
     * @param node the event node
     * @return this node
     * @since 1.0
     * @see EventNode
     */
    @This
    @NonNull EventNode addChild(@NonNull EventNode node);

    /**
     * Adds an {@linkplain EventListener event listener} to this {@linkplain EventNode event node}.
     *
     * @param listener the listener
     * @return this node
     * @since 1.0
     * @see EventListener
     */
    @This
    @NonNull EventNode addListener(@NonNull EventListener<?> listener);

    /**
     * Creates and adds an {@linkplain EventListener event listener} to this {@linkplain EventNode event node}.
     *
     * @param eventConsumer a consumer of the event
     * @param eventClass a class of the event that the listener should listen to
     * @param <E> a type of the event
     * @return this node
     * @since 1.0
     * @see EventListener
     */
    @This
    <E> @NonNull EventNode addListener(@NonNull Consumer<E> eventConsumer, @NonNull Class<? extends E> eventClass);

    /**
     * Adds an annotation-based listener.
     *
     * @param listener the listener
     * @return this node
     * @since 1.0
     */
    @This
    @NonNull EventNode addListener(@NonNull Object listener);

    /**
     * Calls an event.
     *
     * @param event the event
     * @return this node
     * @since 1.0
     */
    @This
    @NonNull EventNode call(@NonNull Object event);

    /**
     * Gets listeners that were registered to this {@linkplain EventNode event node}.
     *
     * @return the listeners
     * @since 1.0
     */
    @NonNull Collection<EventListener<?>> listeners();

    /**
     * Gets children {@linkplain EventNode event nodes} of this node.
     *
     * @return the children event nodes
     * @since 1.0
     */
    @NonNull Collection<EventNode> children();

    /**
     * Gets a priority of this {@linkplain EventNode event node}.
     *
     * @return the event priority
     * @since 1.0
     */
    int priority();

    /**
     * Creates an event node with a priority of {@code 0}.
     *
     * @return the event node
     * @since 1.0
     */
    static @NonNull EventNode create() {
        return create(0);
    }

    /**
     * Creates an event node.
     *
     * @param priority a priority of the event node
     * @return the event node
     * @since 1.0
     */
    static @NonNull EventNode create(int priority) {
        return new EventNodeImpl(priority);
    }
}