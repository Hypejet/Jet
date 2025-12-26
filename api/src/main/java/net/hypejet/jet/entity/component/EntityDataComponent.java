package net.hypejet.jet.entity.component;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.enderdragon.EnderDragonPhase;
import net.hypejet.jet.entity.horse.HorseMarkings;
import net.hypejet.jet.entity.illager.IllagerSpellType;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.entity.variant.chicken.ChickenVariant;
import net.hypejet.jet.entity.variant.cow.CowVariant;
import net.hypejet.jet.entity.variant.cow.MushroomCowVariant;
import net.hypejet.jet.entity.variant.horse.HorseVariant;
import net.hypejet.jet.entity.variant.pig.PigVariant;
import net.hypejet.jet.entity.variant.rabbit.RabbitVariant;
import net.hypejet.jet.entity.villager.VillagerProfession;
import net.hypejet.jet.entity.villager.VillagerType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.game.color.DyeColor;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.rotation.Rotations;
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

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether an armor stand is small.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SMALL = new EntityDataComponent<>("small", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether arms of an armor stand are visible.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SHOW_ARMS = new EntityDataComponent<>("show_arms", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * defining whether the plate underneath an armor stand is hidden.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> HIDE_BASE_PLATE = new EntityDataComponent<>("hide_base_plate", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether an armor
     * stand is a marker, meaning it is invulnerable to all kind of attacks and physics.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> MARKER = new EntityDataComponent<>("marker", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * head {@linkplain Rotations rotations} of an armor stand.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Rotations> HEAD_POSE = new EntityDataComponent<>("head_pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * body {@linkplain Rotations rotations} of an armor stand.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Rotations> BODY_POSE = new EntityDataComponent<>("body_pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * left arm {@linkplain Rotations rotations} of an armor stand.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Rotations> LEFT_ARM_POSE = new EntityDataComponent<>("left_arm_pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * right arm {@linkplain Rotations rotations} of an armor stand.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Rotations> RIGHT_ARM_POSE = new EntityDataComponent<>("right_arm_pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * left leg {@linkplain Rotations rotations} of an armor stand.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Rotations> LEFT_LEG_POSE = new EntityDataComponent<>("left_leg_pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * right leg {@linkplain Rotations rotations} of an armor stand.
     *
     * <p>This component can be used on armor stand entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Rotations> RIGHT_LEG_POSE = new EntityDataComponent<>("right_leg_pose", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing a phase of an ender dragon entity.
     *
     * <p>This component can be used on ender dragon entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<EnderDragonPhase> ENDER_DRAGON_PHASE = new EntityDataComponent<>("ender_dragon_phase", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether an allay entity is dancing.
     *
     * <p>This component can be used on allay entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> DANCING = new EntityDataComponent<>("dancing", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether an allay entity can be duplicated.
     *
     * <p>This component can be used on allay entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CAN_DUPLICATE = new EntityDataComponent<>("can_duplicate", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} defining whether a bat entity is resting.
     *
     * <p>This component can be used on bat entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> RESTING = new EntityDataComponent<>("resting", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a creaking entity can move.
     *
     * <p>This component can be used on creaking entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CAN_MOVE = new EntityDataComponent<>("can_move", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether
     * a creaking entity is active, meaning that it is able to attack players.
     *
     * <p>This component can be used on creaking entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> ACTIVE = new EntityDataComponent<>("active", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a creaking entity is tearing down.
     *
     * <p>This component can be used on creaking entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> TEARING_DOWN = new EntityDataComponent<>("tearing_down", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a {@linkplain BlockPosition block position} of the creaking heart block
     * linked with a creaking entity.
     *
     * <p>This component can be used on creaking entities only.</p>
     *
     * <p>This component is nullable and {@code null} values mean
     * that an entity is not linked with any creaking heart.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<BlockPosition> CREAKING_HEART_POSITION = new EntityDataComponent<>("creaking_heart_position", true);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * whether an entity has charged its attack and is ready to use it.
     *
     * <p>This component can be used on blaze and vex entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CHARGED = new EntityDataComponent<>("charged", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a spider entity is climbing.
     *
     * <p>This component can be used on spider entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CLIMBING = new EntityDataComponent<>("climbing", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * an anger level that a warden entity currently has on its current target.
     *
     * <p>This component can be used on warden entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> ANGER_LEVEL = new EntityDataComponent<>("anger_level", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * a {@linkplain BlockState block state} carried by an enderman entity.
     *
     * <p>This component can be used on enderman entities only.</p>
     *
     * <p>This component is nullable and {@code null} values mean
     * that an enderman entity does not carry any block state.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<BlockState> CARRIED_BLOCK = new EntityDataComponent<>("carried_block", true);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether an enderman entity is screaming.
     *
     * <p>This component can be used on enderman entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SCREAMING = new EntityDataComponent<>("screaming", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether an enderman
     * entity has been stared at. This causes nearby players to hear an ambient sound.
     *
     * <p>This component can be used on enderman entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> STARED_AT = new EntityDataComponent<>("stared_at", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a piglin entity is immune
     * to zombification, meaning that it does not convert to a zombified piglin in dimensions where such kind
     * of behaviour is enabled.
     *
     * <p>This component can be used on piglin and piglin brute entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> IMMUNE_TO_ZOMBIFICATION = new EntityDataComponent<>("immune_to_zombification", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether an entity uses a baby model.
     *
     * <p>This component can be used on zoglin, zombie-like and ageable-mob entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> BABY = new EntityDataComponent<>("baby", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a zombie entity is converting to a drowned variant.
     *
     * <p>This component can be used on zombie-like entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CONVERTING_TO_DROWNED = new EntityDataComponent<>("converting_to_drowned", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * whether a zombie villager entity is converting to a normal villager entity.
     *
     * <p>This component can be used on zombie villager entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> RECOVERING = new EntityDataComponent<>("recovering", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing type of villager entity.
     *
     * <p>This component can be used on zombie villager entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Holder<VillagerType>> VILLAGER_TYPE = new EntityDataComponent<>("villager_type", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing profession of a villager entity.
     *
     * <p>This component can be used on zombie villager entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Holder<VillagerProfession>> VILLAGER_PROFESSION = new EntityDataComponent<>("villager_profession", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing level of a villager entity.
     *
     * <p>This component can be used on zombie villager entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> VILLAGER_LEVEL = new EntityDataComponent<>("villager_level", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a skeleton entity is converting to a stray.
     *
     * <p>This component can be used on skeleton entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CONVERTING_TO_STRAY = new EntityDataComponent<>("converting_to_stray", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a bogged entity is sheared from mushrooms.
     *
     * <p>This component can be used on bogged entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> MUSHROOM_SHEARED = new EntityDataComponent<>("mushroom_sheared", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a creeper entity is swelling.
     *
     * <p>This component can be used on creeper entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SWELLING = new EntityDataComponent<>("swelling", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * whether a creeper entity is powered, meaning that it has an "aura" effect.
     *
     * <p>This component can be used on creeper entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> POWERED = new EntityDataComponent<>("powered", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a creeper entity was manually ignited.
     *
     * <p>This component can be used on creeper entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> IGNITED = new EntityDataComponent<>("ignited", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * whether a celebrating animation is playing on an entity.
     *
     * <p>This component can be used on raider entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CELEBRATING = new EntityDataComponent<>("celebrating", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a witch entity is drinking a potion.
     *
     * <p>This component can be used on witch entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> DRINKING_POTION = new EntityDataComponent<>("drinking_potion", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether an entity is charging their crossbow.
     *
     * <p>This component can be used on pillager entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> CHARGING_CROSSBOW = new EntityDataComponent<>("charging_crossbow", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * the type of spell performed by a spellcaster illager.
     *
     * <p>This component can be used on spellcaster illager entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<IllagerSpellType> PERFORMED_SPELL = new EntityDataComponent<>("performed_spell", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * number of remaining ticks until a glow squid entity starts glowing.
     *
     * <p>This component can be used on glow squid entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> REMAINING_DARK_TICKS = new EntityDataComponent<>("remaining_dark_ticks", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a dolphin entity got a fish from a player.
     *
     * <p>This component can be used on dolphin entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> GOT_FISH = new EntityDataComponent<>("got_fish", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing how moist a dolphin entity is.
     *
     * <p>This component can be used on dolphin entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> MOISTNESS_LEVEL = new EntityDataComponent<>("moistness_level", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing for how
     * many ticks steering an entity with an item should be boosted with additional speed.
     *
     * <p>This component can be used on pig and strider entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> ITEM_STEERING_BOOST_TICKS = new EntityDataComponent<>("item_steering_boost_ticks", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing variant of a pig entity.
     *
     * <p>This component can be used on pig entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Holder<PigVariant>> PIG_VARIANT = new EntityDataComponent<>("pig_variant", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether an entity is suffocating.
     *
     * <p>This component can be used on strider entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SUFFOCATING = new EntityDataComponent<>("suffocating", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether an entity trusts players.
     *
     * <p>This component can be used on ocelot entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> TRUSTING = new EntityDataComponent<>("trusting", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing variant of a cow entity.
     *
     * <p>This component can be used on cow entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Holder<CowVariant>> COW_VARIANT = new EntityDataComponent<>("cow_variant", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing variant of a mushroom cow entity.
     *
     * <p>This component can be used on mushroom cow entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<MushroomCowVariant> MUSHROOM_COW_VARIANT = new EntityDataComponent<>("mushroom_cow_variant", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a goat entity is of the screaming variant.
     *
     * <p>This component can be used on goat entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> SCREAMING_GOAT = new EntityDataComponent<>("screaming_goat", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a goat entity has the left horn.
     *
     * <p>This component can be used on goat entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> HAS_LEFT_HORN = new EntityDataComponent<>("has_left_horn", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a goat entity has the right horn.
     *
     * <p>This component can be used on goat entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> HAS_RIGHT_HORN = new EntityDataComponent<>("has_right_horn", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing wool color of a sheep entity.
     *
     * <p>This component can be used on sheep entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<DyeColor> WOOL_COLOR = new EntityDataComponent<>("wool_color", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether wool of a sheep entity is sheared.
     *
     * <p>This component can be used on sheep entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> WOOL_SHEARED = new EntityDataComponent<>("wool_sheared", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing variant of a rabbit entity.
     *
     * <p>This component can be used on rabbit entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<RabbitVariant> RABBIT_VARIANT = new EntityDataComponent<>("rabbit_variant", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing variant of a chicken entity.
     *
     * <p>This component can be used on chicken entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Holder<ChickenVariant>> CHICKEN_VARIANT = new EntityDataComponent<>("chicken_variant", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing
     * whether a bee entity plays the rolling/stinging animation.
     *
     * <p>This component can be used on bee entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> STINGING = new EntityDataComponent<>("stinging", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a bee
     * entity has stung another entity, meaning that it does not have the stinger anymore.
     *
     * <p>This component can be used on bee entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> HAS_STUNG = new EntityDataComponent<>("has_stung", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a bee entity is carrying nectar.
     *
     * <p>This component can be used on bee entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> HAS_NECTAR = new EntityDataComponent<>("has_nectar", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing remaining time (in ticks) of an entity being angry.
     *
     * <p>This component can be used on bee entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Integer> REMAINING_ANGER_TIME = new EntityDataComponent<>("remaining_anger_time", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a horse-like entity has been tamed.
     *
     * <p>This component can be used on horse-like entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> TAMED = new EntityDataComponent<>("tamed", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} with unknown functionality.
     * It has no visual effect, and it never gets updated on vanilla servers. However, if it is manually
     * updated, it makes a horse-like entity follow their parent if the entity is of baby variant.
     *
     * <p>This component can be used on horse-like entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> BRED = new EntityDataComponent<>("bred", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a horse-like is playing the eating animation.
     *
     * <p>This component can be used on horse-like entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> EATING = new EntityDataComponent<>("eating", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a horse-like entity is standing.
     *
     * <p>This component can be used on horse-like entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> STANDING = new EntityDataComponent<>("standing", false);

    /**
     * An {@linkplain EntityDataComponent entity data component}
     * representing whether a horse-like entity has its mouth open.
     *
     * <p>This component can be used on horse-like entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> MOUTH_OPEN = new EntityDataComponent<>("mouth_open", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing variant of a horse entity.
     *
     * <p>This component can be used on horse entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<HorseVariant> HORSE_VARIANT = new EntityDataComponent<>("horse_variant", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing type of markings of a horse entity.
     *
     * <p>This component can be used on horse entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<HorseMarkings> HORSE_MARKINGS = new EntityDataComponent<>("horse_markings", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing whether a camel entity is dashing.
     *
     * <p>This component can be used on camel entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Boolean> DASHING = new EntityDataComponent<>("dashing", false);

    /**
     * An {@linkplain EntityDataComponent entity data component} representing number of the last game tick
     * during which pose of a camel entity has been changed. The component takes positive values when the camel
     * is standing and negative values for when the camel is sitting.
     *
     * <p>This component can be used on camel entities only.</p>
     *
     * @since 1.0
     */
    public static final EntityDataComponent<Long> LAST_POSE_CHANGE_TICK = new EntityDataComponent<>("last_pose_change_ticks", false);

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