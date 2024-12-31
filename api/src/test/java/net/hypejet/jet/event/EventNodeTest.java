package net.hypejet.jet.event;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.jet.event.annotation.Subscribe;
import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Represents a test of {@linkplain EventNode an event node}.
 *
 * @since 1.0
 */
public final class EventNodeTest {
    @Test
    public void testChildren() {
        EventNode<Object> node = new EventNode<>(Object.class);
        try (CollectionAcquisition<EventNode<?>, ?> acquisition = node.children()) {
            Collection<EventNode<?>> collection = acquisition.collection();
            Assertions.assertTrue(collection.isEmpty());

            List<EventNode<Object>> children = List.of(new EventNode<>(Object.class), new EventNode<>(Object.class));

            children.forEach(node::addChild);
            Assertions.assertTrue(collection.containsAll(children));

            children.forEach(node::removeChild);
            Assertions.assertTrue(collection.isEmpty());
        }
    }

    @Test
    public void testListeners() {
        EventNode<Object> node = new EventNode<>(Object.class);
        try (CollectionAcquisition<EventListener<?>, ?> acquisition = node.listeners()) {
            Collection<EventListener<?>> collection = acquisition.collection();
            Assertions.assertTrue(collection.isEmpty());

            List<EventListener<Object>> listeners = new ArrayList<>();

            listeners.add(new EventListener<>(event -> {}, Object.class));
            listeners.add(new EventListener<>(event -> {}, String.class));
            listeners.forEach(node::addListener);

            listeners.add(node.addListener(event -> {}, Integer.class));
            Assertions.assertTrue(collection.containsAll(listeners));

            listeners.forEach(node::removeListener);
            Assertions.assertTrue(collection.isEmpty());
        }
    }

    @Test
    public void testCalling() {
        EventNode<Object> node = new EventNode<>(Object.class);

        CountDownLatch latch = new CountDownLatch(4);
        TestClassListener listener = new TestClassListener(latch);

        node.addListener(listener);

        node.addListener(event -> latch.countDown(), Object.class);
        node.addListener(event -> latch.countDown(), Object.class);

        node.addListener(event -> latch.countDown(), String.class);

        node.call(new Object());
        Assertions.assertEquals(0, latch.getCount());
    }

    @Test
    public void testNodeProperties() {
        EventPriority priority = EventPriority.LATE;
        Class<?> eventClass = Integer.class;

        EventNode<?> node = new EventNode<>(eventClass, priority);

        Assertions.assertEquals(priority, node.priority());
        Assertions.assertEquals(eventClass, node.eventClass());
    }

    private record TestClassListener(@NonNull CountDownLatch latch) {
        @Subscribe
        public void call(@NonNull Object object) {
            this.latch.countDown();
        }

        @Subscribe
        public void call2(@NonNull Object object) {
            this.latch.countDown();
        }

        @Subscribe
        public void notWorking(@NonNull Integer object) {
            this.latch.countDown();
        }
    }
}