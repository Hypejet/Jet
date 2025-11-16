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

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} plays a burning effect.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> ON_FIRE = new EntityDataComponent<>("on_fire");

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether
     * name tag of an {@linkplain Entity entity} is hidden due to sneaking.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SNEAKING = new EntityDataComponent<>("sneaking");

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} plays sprinting particles.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SPRINTING = new EntityDataComponent<>("sprinting");

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is swimming.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SWIMMING = new EntityDataComponent<>("swimming");

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is invisible.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> INVISIBLE = new EntityDataComponent<>("invisible");

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} has a glowing effect.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> GLOWING = new EntityDataComponent<>("glowing");

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is gliding.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> GLIDING = new EntityDataComponent<>("gliding");

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