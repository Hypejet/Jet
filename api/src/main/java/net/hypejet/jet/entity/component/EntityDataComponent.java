package net.hypejet.jet.entity.component;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.particle.Particle;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;
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
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether custom name of an {@linkplain Entity entity} is visible.
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

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is using an item.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> USING_ITEM = new EntityDataComponent<>("using_item", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing an {@linkplain Entity.InteractionHand interaction hand}
     * that an {@linkplain Entity entity} is using an item with.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Entity.InteractionHand> USED_ITEM_HAND = new EntityDataComponent<>("used_item_hand", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether an {@linkplain Entity entity}
     * is currently in an auto-spin attack, meaning that it is attacking using a trident with riptide enchantment.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> AUTO_SPIN_ATTACK = new EntityDataComponent<>("auto_spin_attack", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing health of an {@linkplain Entity entity}.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Float> HEALTH = new EntityDataComponent<>("health", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing a {@linkplain List list}
     * of potion {@linkplain Particle particles} that are displayed around an {@linkplain Entity entity}.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<List<Particle>> POTION_PARTICLES = new EntityDataComponent<>("potion_particles", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether amount of potion
     * {@linkplain Particle particles} displayed around an {@linkplain Entity entity} is reduced.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> REDUCE_POTION_PARTICLES = new EntityDataComponent<>("reduce_potion_particles", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a number of arrows attached to an {@linkplain Entity entity}.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> ARROW_COUNT = new EntityDataComponent<>("arrow_count", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a number of bee stingers attached to an {@linkplain Entity entity}.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> STINGER_COUNT = new EntityDataComponent<>("stinger_count", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing a {@linkplain BlockPosition block position} of the bed
     * that an {@linkplain Entity entity} is currently sleeping in.
     *
     * <p>This component can be used on living entities only.</p>
     *
     * <p>This component is nullable and {@code null} values mean
     * that an entity does not have a sleeping position set.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<BlockPosition> SLEEPING_POSITION = new EntityDataComponent<>("sleeping_position", true);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether AI of an {@linkplain Entity entity} is enabled.
     *
     * <p>This component can be used on mob entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> NO_AI = new EntityDataComponent<>("no_ai", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether an {@linkplain Entity entity} is left-handed.
     *
     * <p>This component can be used on mob entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> LEFT_HANDED = new EntityDataComponent<>("left_handed", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} is currently aggressive.
     *
     * <p>This component can be used on mob entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> AGGRESSIVE = new EntityDataComponent<>("aggressive", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether an {@linkplain Entity entity} is preparing to shoot a fireball.
     *
     * <p>This component can be used on ghast entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> FIREBALL_CHARGING = new EntityDataComponent<>("fireball_charging", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing size of an {@linkplain Entity entity}.
     *
     * <p>This component can be used on phantom, slime and magma cube entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> SIZE = new EntityDataComponent<>("size", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a {@linkplain BlockPosition block position} that beam of an {@linkplain Entity entity} targets to.
     *
     * <p>This component can be used on end crystal entities only.</p>
     * <p>This component is nullable and {@code null} values mean that an entity has the beam disabled.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<BlockPosition> BEAM_TARGET = new EntityDataComponent<>("beam_target", true);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining
     * whether a bedrock plate is shown underneath an {@linkplain Entity entity}.
     *
     * <p>This component can be used on end crystal entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SHOW_BOTTOM = new EntityDataComponent<>("show_bottom", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing radius of an area effect cloud.
     *
     * <p>This component can be used on area effect cloud entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Float> EFFECT_RADIUS = new EntityDataComponent<>("effect_radius", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether
     * an area effect cloud ignores the radius and displays the effect as a single point.
     *
     * <p>This component can be used on area effect cloud entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> EFFECT_WAITING = new EntityDataComponent<>("effect_waiting", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a {@linkplain Particle particle} that an area effect cloud entity plays.
     *
     * <p>This component can be used on area effect cloud entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Particle> EFFECT_PARTICLE = new EntityDataComponent<>("effect_particle", false);

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