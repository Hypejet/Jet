package net.hypejet.jet.event.annotation;

import net.hypejet.jet.event.priority.EventPriority;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an annotation, which indicates that a method annotated should listen to events with a type specified.
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    /**
     * Gets {@linkplain EventPriority an event priority} that the method should listen to events with.
     *
     * @return the event priority
     * @since 1.0
     */
    @NonNull EventPriority priority() default EventPriority.NORMAL;
}