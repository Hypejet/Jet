package net.hypejet.jet.event.annotation;

import net.hypejet.jet.event.listener.EventListener;
import net.hypejet.jet.event.priority.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an annotation, which defines a method as a {@linkplain EventListener event
 * listener} and contains properties for it.
 *
 * @since 1.0
 * @author Codestech, Window5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    /**
     * Gets a priority of a listener.
     *
     * @return the event priority
     * @since 1.0
     */
    EventPriority priority() default EventPriority.NORMAL;
}