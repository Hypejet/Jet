package net.hypejet.jet.entity;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.view.Viewable;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A Minecraft entity.
 *
 * @since 1.0
 */
@NullMarked
public interface Entity extends Identified, Pointered, Keyed, Viewable, HoverEventSource<HoverEvent.ShowEntity> {
    /**
     * Gets the {@linkplain Holder.Reference holder referencing} to the type of this {@linkplain Entity entity}.
     *
     * @return the entity type key
     * @since 1.0
     */
    Holder.Reference<EntityType> entityType();

    /**
     * Gets a {@linkplain UUID unique identifier} of this {@linkplain Entity entity}.
     *
     * @return the unique identifier
     * @since 1.0
     */
    UUID uniqueId();

    /**
     * Gets a {@linkplain MinecraftServer server} that this {@linkplain Entity entity} is part of.
     *
     * @return the server
     * @since 1.0
     */
    MinecraftServer server();

    /**
     * Gets a name that this {@linkplain Entity entity} uses in {@linkplain Score score} management
     * of {@linkplain Scoreboard scoreboards}.
     *
     * @return the name
     * @since 1.0
     */
    String scoreboardName();

    /**
     * Gets current {@linkplain Position position} of this {@linkplain Entity entity}.
     *
     * @return the position where this entity currently is
     * @since 1.0
     */
    Position position();

    /**
     * Gets a {@linkplain Vector} of current velocity of this {@linkplain Entity entity}.
     *
     * @return the velocity vector
     * @since 1.0
     */
    Vector velocity();

    /**
     * Updates {@linkplain Position position} and velocity {@linkplain Vector vector}
     * of this {@linkplain Entity entity}.
     *
     * <p>Specifying a {@linkplain RelativeFlag relative flag} makes a value
     * associated with that flag relative to the current one.</p>
     *
     * @param position the position that the entity should have
     * @param velocity the velocity that the entity should have
     * @param flags the relative flags specifying which values of the specified position
     *              and velocity should be recognised as relative to current ones
     * @since 1.0
     */
    void updatePosition(Position position, Vector velocity, RelativeFlag... flags);

    /**
     * Updates {@linkplain Position position} and velocity {@linkplain Vector vector}
     * of this {@linkplain Entity entity}.
     *
     * <p>Specifying a {@linkplain RelativeFlag relative flag} makes a value
     * associated with that flag relative to the current one.</p>
     *
     * @param position the position that the entity should have
     * @param velocity the velocity that the entity should have
     * @param flags the relative flags specifying which values of the specified position
     *              and velocity should be recognised as relative to current ones
     * @since 1.0
     */
    void updatePosition(Position position, Vector velocity, Collection<RelativeFlag> flags);

    /**
     * Gets the value of the specified {@linkplain EntityDataComponent entity data component} for this entity.
     *
     * @param component the entity data component whose value should be returned
     * @return the entity data component value, can be {@code null} only if the component is nullable
     * @param <V> the type of returned value
     * @throws IllegalArgumentException if this entity does not support the specified component
     * @since 1.0
     */
    <V> @Nullable V component(EntityDataComponent<V> component);

    /**
     * Sets the value of the specified {@linkplain EntityDataComponent entity data component} for this entity.
     *
     * @param component the entity data component whose value should be set
     * @param value the value that the entity data component should have for this entity,
     *              may be {@code null} only if the component is nullable
     * @param <V> the value type of the specified entity data component
     * @throws IllegalArgumentException if this entity does not support the specified component or the specified
     *                                  component is not nullable but the specified value is null
     * @since 1.0
     */
    <V> void component(EntityDataComponent<V> component, @Nullable V value);

    /**
     * Represents a hand of an entity.
     *
     * <p>Contents of this enum depend on Minecraft, however it is safe to keep
     * it an enum, since it is very unlikely to change.</p>
     *
     * @since 1.0
     */
    enum Hand {
        /**
         * A left hand of an entity.
         *
         * @since 1.0
         */
        LEFT,
        /**
         * A right hand of an entity.
         *
         * @since 1.0
         */
        RIGHT
    }

    /**
     * A hand an {@linkplain Entity entity} can interact with.
     *
     * <p>Contents of this enum depend on Minecraft, however it is safe to keep
     * it an enum, since it is very unlikely to change.</p>
     *
     * @since 1.0
     * @see Entity
     */
    enum InteractionHand {
        /**
         * The main hand.
         *
         * @since 1.0
         */
        MAIN_HAND,
        /**
         * The offhand.
         *
         * @since 1.0
         */
        OFFHAND
    }

    /**
     * An {@linkplain Entity entity} animation.
     *
     * <p>This is not an enum, since it depends on Minecraft.
     * Adding an enum entry could break enum switch cases for example.</p>
     *
     * @since 1.0
     * @see Entity
     */
    final class Animation {
        /**
         * An {@linkplain Animation animation} swinging the main hand.
         *
         * @since 1.0
         */
        public static final Animation SWING_MAIN_HAND = new Animation("swing_main_hand");

        /**
         * An {@linkplain Animation animation} showing the leave bed effect.
         *
         * @since 1.0
         */
        public static final Animation LEAVE_BED = new Animation("leave_bed");

        /**
         * An {@linkplain Animation animation} swinging the offhand.
         *
         * @since 1.0
         */
        public static final Animation SWING_OFFHAND = new Animation("swing_offhand");

        /**
         * An {@linkplain Animation animation} showing the critical hit effect.
         *
         * @since 1.0
         */
        public static final Animation CRITICAL_HIT = new Animation("critical_hit");

        /**
         * An {@linkplain Animation animation} showing the magic critical hit effect.
         *
         * @since 1.0
         */
        public static final Animation MAGIC_CRITICAL_HIT = new Animation("magic_critical_hit");

        private final String name;

        private Animation(String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        @Override
        public String toString() {
            return "Animation{name=" + this.name + "}";
        }
    }
    
    /**
     * An {@linkplain Entity entity} event.
     *
     * <p>This is not an enum, since it depends on Minecraft.
     * Adding an enum entry could break enum switch cases for example.</p>
     *
     * @since 1.0
     * @see Entity
     */
    final class Event {
        /**
         * An {@linkplain Event event} that spawns tipped arrow particle effects.
         *
         * @since 1.0
         */
        public static final Event ARROW_TIP = new Event("arrow_tip");

        /**
         * An {@linkplain Event event} that makes a rabbit play its jump animation.
         *
         * @since 1.0
         */
        public static final Event RABBIT_JUMP = new Event("rabbit_jump");

        /**
         * An {@linkplain Event event} that plays the death animation for living entities.
         *
         * @since 1.0
         */
        public static final Event DEATH_ANIMATION = new Event("death_animation");

        /**
         * An {@linkplain Event event} that plays the attack animation
         * for golems, hoglins, ravagers, wardens and zoglins.
         *
         * @since 1.0
         */
        public static final Event START_ATTACKING = new Event("start_attacking");

        /**
         * An {@linkplain Event event} that spawns smoke particles when taming fails.
         *
         * @since 1.0
         */
        public static final Event TAMING_FAILED = new Event("taming_failed");

        /**
         * An {@linkplain Event event} that spawns heart particles when taming succeeds.
         *
         * @since 1.0
         */
        public static final Event TAMING_SUCCEEDED = new Event("taming_succeeded");

        /**
         * An {@linkplain Event event} that starts playing the wolf shaking-water animation.
         *
         * @since 1.0
         */
        public static final Event WETNESS_SHAKING_START = new Event("wetness_shaking_start");

        /**
         * An {@linkplain Event event} that stops playing the wolf shaking-water animation.
         *
         * @since 1.0
         */
        public static final Event WETNESS_SHAKING_STOP = new Event("wetness_shaking_stop");

        /**
         * An {@linkplain Event event} that marks player item usage finished.
         *
         * @since 1.0
         */
        public static final Event ITEM_USAGE_FINISHED = new Event("item_usage_finished");

        /**
         * An {@linkplain Event event} that makes an entity play the eating-grass animation
         * or ignites TNT inside the minecart, depending on type of the entity.
         *
         * @since 1.0
         */
        public static final Event EAT_GRASS_OR_IGNITE = new Event("eat_grass_or_ignite");

        /**
         * An {@linkplain Event event} that makes an iron golem hold out a poppy.
         *
         * @since 1.0
         */
        public static final Event START_OFFERING_POPPY = new Event("start_offering_poppy");

        /**
         * An {@linkplain Event event} that puts away poppy held by an iron golem.
         *
         * @since 1.0
         */
        public static final Event STOP_OFFERING_POPPY = new Event("stop_offering_poppy");

        /**
         * An {@linkplain Event event} that spawns villager mating heart particles.
         *
         * @since 1.0
         */
        public static final Event MATING = new Event("mating");

        /**
         * An {@linkplain Event event} that spawns villager angry particles.
         *
         * @since 1.0
         */
        public static final Event VILLAGER_ANGRY = new Event("villager_angry");

        /**
         * An {@linkplain Event event} that spawns villager happy particles.
         *
         * @since 1.0
         */
        public static final Event VILLAGER_HAPPY = new Event("villager_happy");

        /**
         * An {@linkplain Event event} that spawns witch magic particles.
         *
         * @since 1.0
         */
        public static final Event WITCH_MAGIC = new Event("witch_magic");

        /**
         * An {@linkplain Event event} that plays the Zombie Villager cure sound.
         *
         * @since 1.0
         */
        public static final Event ZOMBIE_VILLAGER_CURE = new Event("zombie_villager_cure");

        /**
         * An {@linkplain Event event} that triggers a firework explosion effect.
         *
         * @since 1.0
         */
        public static final Event FIREWORK_EXPLODE = new Event("firework_explode");

        /**
         * An {@linkplain Event event} that spawns love-mode heart particles for animals or Allay.
         *
         * @since 1.0
         */
        public static final Event ANIMAL_LOVE = new Event("animal_love");

        /**
         * An {@linkplain Event event} that resets rotation of a squid to 0 radians.
         *
         * @since 1.0
         */
        public static final Event SQUID_ROTATION_RESET = new Event("squid_rotation_reset");

        /**
         * An {@linkplain Event event} that spawns an explosion particle for mobs.
         *
         * @since 1.0
         */
        public static final Event SPAWN_EXPLOSION_PARTICLES = new Event("spawn_explosion_particles");

        /**
         * An {@linkplain Event event} that plays a guardian attack sound.
         *
         * @since 1.0
         */
        public static final Event GUARDIAN_ATTACK = new Event("guardian_attack");

        /**
         * An {@linkplain Event event} that enables reduced debug screen information for players.
         *
         * @since 1.0
         */
        public static final Event REDUCED_DEBUG_INFO = new Event("reduced_debug_info");

        /**
         * An {@linkplain Event event} that disables reduced debug screen information for players.
         *
         * @since 1.0
         */
        public static final Event FULL_DEBUG_INFO = new Event("full_debug_info");

        /**
         * An {@linkplain Event event} that sets player op level to 0.
         *
         * @since 1.0
         */
        public static final Event PERMISSION_LEVEL_0 = new Event("permission_level_0");

        /**
         * An {@linkplain Event event} that sets player permission level to 1.
         *
         * @since 1.0
         */
        public static final Event PERMISSION_LEVEL_1 = new Event("permission_level_1");

        /**
         * An {@linkplain Event event} that sets player permission level to 2.
         *
         * @since 1.0
         */
        public static final Event PERMISSION_LEVEL_2 = new Event("permission_level_2");

        /**
         * An {@linkplain Event event} that sets player permission level to 3.
         *
         * @since 1.0
         */
        public static final Event PERMISSION_LEVEL_3 = new Event("permission_level_3");

        /**
         * An {@linkplain Event event} that sets player permission level to 4.
         *
         * @since 1.0
         */
        public static final Event PERMISSION_LEVEL_4 = new Event("permission_level_4");

        /**
         * An {@linkplain Event event} that pulls an entity caught by a fishing hook.
         *
         * @since 1.0
         */
        public static final Event FISHING_ROD_PULL_ENTITY = new Event("fishing_rod_pull_entity");

        /**
         * An {@linkplain Event event} that plays a hit sound and resets hit cooldown for an Armor Stand.
         *
         * @since 1.0
         */
        public static final Event ARMOR_STAND_HIT = new Event("armor_stand_hit");

        /**
         * An {@linkplain Event event} that plays the totem of undying animation and sound.
         *
         * @since 1.0
         */
        public static final Event TOTEM_OF_UNDYING_PROTECT = new Event("totem_of_undying_protect");

        /**
         * An {@linkplain Event event} that causes a dolphin to play a treasure locating effect.
         *
         * @since 1.0
         */
        public static final Event DOLPHIN_LOOK_FOR_TREASURE = new Event("dolphin_look_for_treasure");

        /**
         * An {@linkplain Event event} that marks a ravager as stunned.
         *
         * @since 1.0
         */
        public static final Event RAVAGER_STUNNED = new Event("ravager_stunned");

        /**
         * An {@linkplain Event event} that spawns smoke particles when ocelot taming fails.
         *
         * @since 1.0
         */
        public static final Event TRUSTING_FAILED = new Event("trusting_failed");

        /**
         * An {@linkplain Event event} that spawns heart particles when ocelot taming succeeds.
         *
         * @since 1.0
         */
        public static final Event TRUSTING_SUCCEEDED = new Event("trusting_succeeded");

        /**
         * An {@linkplain Event event} that spawns villager "sweat" particles during a raid.
         *
         * @since 1.0
         */
        public static final Event VILLAGER_SWEAT = new Event("villager_sweat");

        /**
         * An {@linkplain Event event} that spawns fox eating particles.
         *
         * @since 1.0
         */
        public static final Event FOX_CHEW = new Event("fox_chew");

        /**
         * An {@linkplain Event event} that spawns portal particles for teleporting entities.
         *
         * @since 1.0
         */
        public static final Event PORTAL_PARTICLES = new Event("portal_particles");

        /**
         * An {@linkplain Event event} that spawns honey block slide particles.
         *
         * @since 1.0
         */
        public static final Event HONEY_BLOCK_SLIDE = new Event("honey_block_slide");

        /**
         * An {@linkplain Event event} that spawns honey block fall particles.
         *
         * @since 1.0
         */
        public static final Event HONEY_BLOCK_FALL = new Event("honey_block_fall");

        /**
         * An {@linkplain Event event} that swaps hand items for an entity.
         *
         * @since 1.0
         */
        public static final Event SWAP_HANDS = new Event("swap_hands");

        /**
         * An {@linkplain Event event} that lowers head of a goat for ramming.
         *
         * @since 1.0
         */
        public static final Event START_RAM = new Event("start_ram");

        /**
         * An {@linkplain Event event} that stops the goat lowering its head while ramming.
         *
         * @since 1.0
         */
        public static final Event STOP_RAM = new Event("stop_ram");

        /**
         * An {@linkplain Event event} that spawns death smoke particles.
         *
         * @since 1.0
         */
        public static final Event DEATH_SMOKE = new Event("death_smoke");

        /**
         * An {@linkplain Event event} that performs tendril shaking animation for a warden entity.
         *
         * @since 1.0
         */
        public static final Event TENDRIL_SHAKE = new Event("tendril_shake");

        /**
         * An {@linkplain Event event} that performs the sonic boom attack animation for a warden entity.
         *
         * @since 1.0
         */
        public static final Event SONIC_BOOM = new Event("sonic_boom");

        /**
         * An {@linkplain Event event} that plays a digging animation for a sniffer entity.
         *
         * @since 1.0
         */
        public static final Event SNIFFER_DIG = new Event("sniffer_dig");

        /**
         * An {@linkplain Event event} that plays a peek animation for an armadillo entity.
         *
         * @since 1.0
         */
        public static final Event ARMADILLO_SCARED = new Event("armadillo_scared");
        
        /**
         * An {@linkplain Event event} that plays a twitch animation for a creaking entity.
         *
         * @since 1.0
         */
        public static final Event SHAKE = new Event("shake");

        /**
         * An {@linkplain Event event} that plays drowning particles on a living entity.
         *
         * @since 1.0
         */
        public static final Event DROWNING_PARTICLES = new Event("drowning_particles");

        /**
         * An {@linkplain Event event} playing a ravager roar sound.
         *
         * @since 1.0
         */
        public static final Event RAVAGER_ROAR = new Event("ravager_roar");
    
        private final String name;
    
        private Event(String name) {
            this.name = Objects.requireNonNull(name, "name");
        }
    
        @Override
        public String toString() {
            return "Event{name=" + this.name + "}";
        }
    }
}