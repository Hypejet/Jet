package net.hypejet.jet.entity.component;

import net.hypejet.jet.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A field of mutable data of an {@linkplain Entity entity}.
 *
 * @param <V> the type of values that the field accepts
 * @since 1.0
 * @see EntityDataComponentMap
 * @see Entity
 */
@NullMarked
public final class EntityDataComponent<V> {

    private final String name;

    private EntityDataComponent(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "EntityDataComponent{" +
                "name='" + this.name + '\'' +
                '}';
    }
}