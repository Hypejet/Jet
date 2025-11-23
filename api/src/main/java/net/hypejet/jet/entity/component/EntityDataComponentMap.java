package net.hypejet.jet.entity.component;

import net.hypejet.jet.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A map associating {@linkplain EntityDataComponent entity data components}
 * with their values for an {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see EntityDataComponent
 * @see Entity
 */
@NullMarked
public interface EntityDataComponentMap {
    /**
     * Gets a value associated with the specified {@linkplain EntityDataComponent entity data component}.
     *
     * @param component the entity data component whose value association should be returned
     * @return the value associated with the specified entity data component,
     *         which may be {@code null} if the entity data component is nullable
     * @param <V> the type of value that is returned for the specified entity data component
     * @throws IllegalArgumentException if type of entity associated with this entity data component map
     *                                  does not support the specified entity data component
     * @since 1.0
     */
    <V> @Nullable V value(EntityDataComponent<V> component);

    /**
     * Replaces a value associated with the specified {@linkplain EntityDataComponent entity data component}.
     *
     * @param component the entity data component whose value association should be replaced
     * @param value the replacement value, which can be {@code null} if the specified entity data component is nullable
     * @param <V> the type of values that can be associated with the specified entity data component
     * @throws IllegalArgumentException
     *     <ul>
     *         <li>if type of entity associated with this entity data component map
     *         does not support the specified entity data component</li>
     *         <li>if the specified value is null and the specified component is not nullable</li>
     *     </ul>
     *
     *
     * @since 1.0
     */
    <V> void value(EntityDataComponent<V> component, @Nullable V value);
}