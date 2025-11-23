package net.hypejet.jet.entity.component;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.pose.Pose;
import net.kyori.adventure.text.Component;
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
    public static final EntityDataComponent<Boolean> ON_FIRE = new EntityDataComponent<>("on_fire", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether
     * name tag of an {@linkplain Entity entity} is hidden due to sneaking.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SNEAKING = new EntityDataComponent<>("sneaking", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} plays sprinting particles.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SPRINTING = new EntityDataComponent<>("sprinting", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is swimming.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SWIMMING = new EntityDataComponent<>("swimming", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is invisible.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> INVISIBLE = new EntityDataComponent<>("invisible", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} has a glowing effect.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> GLOWING = new EntityDataComponent<>("glowing", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is gliding.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> GLIDING = new EntityDataComponent<>("gliding", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * remaining air supply of an {@linkplain Entity entity}.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> AIR_SUPPLY = new EntityDataComponent<>("air_supply", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing a custom name of an {@linkplain Entity entity}.
     *
     * <p>This component is nullable and {@code null} values mean that an entity does not have a custom name.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Component> CUSTOM_NAME = new EntityDataComponent<>("custom_name", true);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether
     * custom name of an {@linkplain Entity entity} should be visible.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CUSTOM_NAME_VISIBLE = new EntityDataComponent<>("custom_name_visible", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether
     * an {@linkplain Entity entity} is silent, meaning that it does not play any sounds.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SILENT = new EntityDataComponent<>("silent", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether
     * gravity is <strong>not</strong> applied to an {@linkplain Entity entity}.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> NO_GRAVITY = new EntityDataComponent<>("no_gravity", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a {@linkplain Pose pose} of an {@linkplain Entity entity}.
     *
     * @since 1.0
     * @see Pose
     */
    public static final EntityDataComponent<Pose> POSE = new EntityDataComponent<>("pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a number of ticks for which an {@linkplain Entity entity} is frozen.
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> TICKS_FROZEN = new EntityDataComponent<>("ticks_frozen", false);

    private final String name;
    private final boolean nullable;

    private EntityDataComponent(String name, boolean nullable) {
        this.name = Objects.requireNonNull(name, "name");
        this.nullable = nullable;
    }

    /**
     * Gets a display name of this {@linkplain EntityDataComponent entity data component}.
     *
     * @return the component display name
     * @since 1.0
     */
    public String name() {
        return this.name;
    }

    /**
     * Gets whether this {@linkplain EntityDataComponent entity data component} allows {@code null} values.
     *
     * @return {@code true} if this component allows {@code null} values, {@code false} otherwise
     * @since 1.0
     */
    public boolean nullable() {
        return this.nullable;
    }

    @Override
    public String toString() {
        return "EntityDataComponent{" +
                "name='" + this.name + '\'' +
                '}';
    }
}