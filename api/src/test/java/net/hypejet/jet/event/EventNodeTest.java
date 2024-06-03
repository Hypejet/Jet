package net.hypejet.jet.event;

import net.hypejet.jet.event.annotation.Subscribe;
import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Represents a test for {@linkplain EventNode event node}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class EventNodeTest {
    @Test
    public void testChildren() {
        EventNode<Object> node = EventNode.create(Object.class);
        Assertions.assertTrue(node.children().isEmpty());

        List<EventNode<Object>> children = List.of(EventNode.create(Object.class), EventNode.create(Object.class));

        children.forEach(node::addChild);
        Assertions.assertTrue(node.children().containsAll(children));

        children.forEach(node::removeChild);
        Assertions.assertTrue(node.children().isEmpty());
    }

    @Test
    public void testListeners() {
        EventNode<Object> node = EventNode.create(Object.class);
        Assertions.assertTrue(node.listeners().isEmpty());

        List<EventListener<Object>> listeners = new ArrayList<>();

        listeners.add(EventListener.listener(event -> {}, Object.class));
        listeners.add(EventListener.listener(event -> {}, String.class));
        listeners.forEach(node::addListener);

        listeners.add(node.addListener(event -> {}, Integer.class));

        Assertions.assertTrue(node.listeners().containsAll(listeners));

        listeners.forEach(node::removeListener);
        Assertions.assertTrue(node.listeners().isEmpty());
    }

    @Test
    public void testCalling() {
        EventNode<Object> node = EventNode.create(Object.class);

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

        EventNode<?> node = EventNode.create(eventClass, priority);

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