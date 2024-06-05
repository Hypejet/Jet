package net.hypejet.jet.event;

import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.priority.EventPriority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a test for {@linkplain EventListener event listener}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class EventListenerTest {
    @Test
    public void testListenerProperties() {
        Class<?> eventClass = Integer.class;
        EventPriority priority = EventPriority.EARLY;

        EventListener<?> listener = EventListener.listener(event -> {}, eventClass, priority);

        Assertions.assertEquals(eventClass, listener.eventClass());
        Assertions.assertEquals(priority, listener.priority());
    }

    @Test
    public void testListenerCalling() {
        AtomicBoolean called = new AtomicBoolean();
        EventListener<String> listener = EventListener.listener(event -> called.set(true), String.class);

        Assertions.assertFalse(called.get());
        listener.call("Hello World, Hello Jet users!");
        Assertions.assertTrue(called.get());
    }

    @Test
    public void testListenerEligible() {
        EventListener<String> listener = EventListener.listener(event -> {}, String.class, event -> event.equals("A"));
        Assertions.assertFalse(listener.isEligible("a"));
        Assertions.assertTrue(listener.isEligible("A"));
        Assertions.assertFalse(listener.isEligible("B"));
    }
}