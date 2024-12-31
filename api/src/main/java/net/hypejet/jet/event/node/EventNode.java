package net.hypejet.jet.event.node;

import net.hypejet.concurrency.collection.CollectionAcquirable;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.collection.set.HashSetAcquirable;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.annotation.Subscribe;
import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents something that manages events.
 *
 * @param <E> a type of event that this event node handles
 * @since 1.0
 */
public final class EventNode<E> implements Comparable<EventNode<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventNode.class);

    private final Class<E> eventClass;
    private final EventPriority priority;

    private final CollectionAcquirable<EventListener<? extends E>, ?> listeners = new HashSetAcquirable<>();
    private final CollectionAcquirable<EventNode<? extends E>, ?> children = new HashSetAcquirable<>();

    /**
     * Constructs the {@linkplain EventNode event node} with {@linkplain EventPriority#NORMAL a normal event priority}.
     *
     * @param eventClass a class of an event that the event node should handle
     * @since 1.0
     */
    public EventNode(@NonNull Class<E> eventClass) {
        this(eventClass, EventPriority.NORMAL);
    }

    /**
     * Constructs the {@linkplain EventNode event node}.
     *
     * @param eventClass a class of an event that the event node should handle
     * @param priority an event priority that the event node should have
     * @since 1.0
     */
    public EventNode(@NonNull Class<E> eventClass, @NonNull EventPriority priority) {
        this.eventClass = NullabilityUtil.requireNonNull(eventClass, "event class");
        this.priority = NullabilityUtil.requireNonNull(priority, "event priority");
    }

    /**
     * Adds {@linkplain EventNode an event node} as a child of this node. Does nothing if this event node already
     * contains the event node specified as a child.
     *
     * @param node the event node
     * @return this event node
     * @since 1.0
     */
    public @NonNull EventNode<E> addChild(@NonNull EventNode<? extends E> node) {
        NullabilityUtil.requireNonNull(node, "node");
        if (!this.eventClass.isAssignableFrom(node.eventClass())) {
            throw new IllegalArgumentException("You cannot add a child to an event node, whose event class is not" +
                    " assignable from an event class of the child");
        }

        if (this == node)
            throw new IllegalArgumentException("You cannot add an event node as a child of itself");

        try (CollectionAcquisition<EventNode<? extends E>, ?> acquisition = this.children.acquireWrite()) {
            acquisition.collection().add(node);
        }

        return this;
    }

    /**
     * Removes {@linkplain EventNode an event node} as a child from this node. Does nothing if the event node is not
     * a child of this node.
     *
     * @param node the event node
     * @return this event node
     * @since 1.0
     */
    public @NonNull EventNode<E> removeChild(@NonNull EventNode<? extends E> node) {
        NullabilityUtil.requireNonNull(node, "node");
        try (CollectionAcquisition<EventNode<? extends E>, ?> acquisition = this.children.acquireWrite()) {
            acquisition.collection().remove(node);
        }
        return this;
    }

    /**
     * Adds {@linkplain EventListener an event listener} to this event node. Does nothing if this event node already
     * contains the listener specified as a listener.
     *
     * @param listener the listener
     * @return this event node
     * @since 1.0
     * @see EventListener
     */
    public @NonNull EventNode<E> addListener(@NonNull EventListener<? extends E> listener) {
        NullabilityUtil.requireNonNull(listener, "listener");

        if (!this.eventClass.isAssignableFrom(listener.eventClass())) {
            throw new IllegalArgumentException("You cannot add a listener in an event node, of which an event class" +
                    " is not assignable from an event class of the listener");
        }

        try (CollectionAcquisition<EventListener<? extends E>, ?> acquisition = this.listeners.acquireWrite()) {
            acquisition.collection().add(listener);
        }

        return this;
    }


    /**
     * Creates and adds {@linkplain EventListener an event listener} to this {@linkplain EventNode event node}.
     *
     * @param eventConsumer a function that should consume events
     * @param eventClass a class of the event that the listener should listen to
     * @param <T> a type of event that the listener should handle
     * @return the listener created
     * @since 1.0
     * @see EventListener
     */
    public <T extends E> @NonNull EventListener<T> addListener(@NonNull Consumer<T> eventConsumer,
                                                               @NonNull Class<? extends T> eventClass) {
        NullabilityUtil.requireNonNull(eventConsumer, "event consumer");
        NullabilityUtil.requireNonNull(eventClass, "event class");

        EventListener<T> listener = new EventListener<>(eventConsumer, eventClass);
        this.addListener(listener);

        return listener;
    }

    /**
     * Removes {@linkplain EventListener an event listener} from this {@linkplain EventNode event node}.
     *
     * @param listener the listener
     * @return this event node
     * @since 1.0
     */
    public @NonNull EventNode<E> removeListener(@NonNull EventListener<? extends E> listener) {
        try (CollectionAcquisition<EventListener<? extends E>, ?> acquisition = this.listeners.acquireWrite()) {
            acquisition.collection().remove(NullabilityUtil.requireNonNull(listener, "listener"));
            return this;
        }
    }

    /**
     * Adds {@linkplain Subscribe a subscribe annotation-based} listener.
     *
     * @param listener the listener
     * @return this event node
     * @since 1.0
     * @see net.hypejet.jet.event.annotation.Subscribe
     */
    public @NonNull EventNode<E> addListener(@NonNull Object listener) {
        Class<?> listenerClass = listener.getClass();
        String listenerClassName = listenerClass.getSimpleName();

        for (Method method : listenerClass.getDeclaredMethods()) {
            Subscribe subscription = method.getAnnotation(Subscribe.class);
            if (subscription == null) continue;

            String methodName = method.getName();
            Class<?>[] parameters = method.getParameterTypes();

            if (parameters.length != 1) {
                throw new IllegalArgumentException(String.format(
                        "Could not register an event with method of \"%s#%s\", because it has more or less arguments" +
                                " than expected",
                        listenerClass, methodName
                ));
            }

            Class<?> eventType = parameters[0];
            if (!eventType.isAssignableFrom(this.eventClass) && !this.eventClass.isAssignableFrom(eventType)) {
                throw new IllegalArgumentException(String.format(
                        "Could not register an event listener with method of \"%s#%s\", because none of event " +
                                "classes - \"%s\" and \"%s\" - is assignable from each other",
                        listenerClassName, methodName, eventType.getSimpleName(), this.eventClass.getSimpleName()
                ));
            }

            this.addListener(new EventListener<>(event -> {
                try {
                    if (!method.canAccess(listener))
                        method.setAccessible(true);
                    if (eventType.isAssignableFrom(event.getClass()))
                        method.invoke(listener, event);
                } catch (Throwable throwable) {
                    LOGGER.error("An error occurred while calling an annotation-based listener", throwable);
                }
            }, this.eventClass, subscription.priority()));
        }

        return this;
    }

    /**
     * Calls all {@linkplain EventListener event listeners} including those from
     * {@linkplain EventNode children event nodes} of this event node with an event specified.
     *
     * @param event the event to pass as an argument of the calls
     * @return this event node
     * @since 1.0
     */
    public @NonNull EventNode<E> call(@NonNull E event) {
        if (!this.eventClass.isAssignableFrom(event.getClass())) {
            throw new IllegalArgumentException("You cannot call an event in an event node, of which event class is" +
                    " not assignable from an event class of the event");
        }

        try (CollectionAcquisition<EventListener<? extends E>, ?> listenersAcquisition = this.listeners.acquireRead();
             CollectionAcquisition<EventNode<? extends E>, ?> childrenAcquisition = this.children.acquireRead()) {
            List<EventListener<? extends E>> copiedListeners = new ArrayList<>(listenersAcquisition.collection());
            Collections.sort(copiedListeners);
            copiedListeners.forEach(listener -> this.callListener(listener, event));

            List<EventNode<? extends E>> copiedChildren = new ArrayList<>(childrenAcquisition.collection());
            Collections.sort(copiedChildren);
            copiedChildren.forEach(node -> this.callChild(node, event));
        }

        return this;
    }

    /**
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of listeners that were registered to this
     * event node.
     *
     * @return the listeners
     * @since 1.0
     */
    public @NonNull CollectionAcquisition<EventListener<? extends E>, ?> listeners() {
        return this.listeners.acquireRead();
    }

    /**
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of child event nodes that were registered
     * in this event node.
     *
     * @return the collection acquisition
     * @since 1.0
     */
    public @NonNull CollectionAcquisition<EventNode<? extends E>, ?> children() {
        return this.children.acquireRead();
    }

    /**
     * Gets a priority of this event node.
     *
     * @return the event priority
     * @since 1.0
     */
    public @NonNull EventPriority priority() {
        return this.priority;
    }

    /**
     * Gets a class of an event that this event node handles.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<E> eventClass() {
        return this.eventClass;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EventNode<?> eventNode)) return false;
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
    public int compareTo(@NonNull EventNode<?> node) {
        return Integer.compare(this.priority().ordinal(), node.priority().ordinal());
    }

    private <T extends E> void callListener(@NonNull EventListener<T> listener, @NonNull E event) {
        try {
            Class<? extends T> listenerEventClass = listener.eventClass();
            if (!listenerEventClass.isAssignableFrom(event.getClass())) return;

            T castEvent = listenerEventClass.cast(event);
            if (!listener.isEligible(castEvent)) return;

            listener.call(castEvent);
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while calling an event", throwable);
        }
    }

    private <T extends E> void callChild(@NonNull EventNode<T> node, @NonNull E event) {
        Class<T> nodeEventClass = node.eventClass();
        if (!nodeEventClass.isAssignableFrom(event.getClass())) return;
        node.call(nodeEventClass.cast(event));
    }
}