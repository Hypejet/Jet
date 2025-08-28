package net.hypejet.jet.entity;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A Minecraft entity.
 *
 * @since 1.0
 */
public interface Entity extends Identified, Pointered, HoverEventSource<HoverEvent.ShowEntity>, Keyed {
    /**
     * Gets the {@linkplain Key} of type of this {@linkplain Entity entity}.
     *
     * @return the entity type key
     * @since 1.0
     */
    @NonNull Key entityType();

    /**
     * Gets a {@linkplain UUID unique identifier} of this {@linkplain Entity entity}.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Gets current {@linkplain Position position} of this {@linkplain Entity entity}.
     *
     * @return the position where this entity currently is
     * @since 1.0
     */
    @NonNull Position position();

    /**
     * Gets a {@linkplain Vector} of current velocity of this {@linkplain Entity entity}.
     *
     * @return the velocity vector
     * @since 1.0
     */
    @NonNull Vector velocity();

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
    void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                        @NonNull RelativeFlag @NonNull ... flags);

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
    void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                        @NonNull Collection<RelativeFlag> flags);

    /**
     * Gets a {@linkplain World world} where this {@linkplain Entity entity} is.
     *
     * @return the world of the entity
     * @since 1.0
     */
    @NonNull World world();

    /**
     * Teleports this {@linkplain Entity entity} to the specified {@linkplain World world}.
     *
     * <p>The initial position of the entity is going
     * to be the {@linkplain World#defaultSpawnPosition() default spawn position}
     * of the world that the entity is being teleported to.</p>
     *
     * <p>Attributes and metadata of the entity are kept after the world change.</p>
     *
     * @param world the world that this entity should be teleported to
     * @since 1.0
     */
    void teleport(@NonNull World world);

    /**
     * Teleports this {@linkplain Entity entity} to the specified {@linkplain World world}.
     *
     * <p>Attributes and metadata of the entity are kept after the world change.</p>
     *
     * @param world the world that this entity should be teleported to
     * @param position an initial position where the entity should spawn after the world change
     * @since 1.0
     */
    void teleport(@NonNull World world, @NonNull Position position);

    /**
     * Teleports this {@linkplain Entity entity} to the specified {@linkplain World world}.
     *
     * @param world the world that this entity should be teleported to
     * @param position an initial position where the entity should spawn after the world change
     * @param keepAttributes whether attributes of the entity should be kept after the world change
     * @param keepMetadata whether metadata of the entity should be kept after the world change
     * @since 1.0
     */
    void teleport(@NonNull World world, @NonNull Position position, boolean keepAttributes, boolean keepMetadata);

    /**
     * Gets a name that this {@linkplain Entity entity} uses in {@linkplain Score score} management
     * of {@linkplain Scoreboard scoreboards}.
     *
     * @return the name
     * @since 1.0
     */
    @NonNull String scoreboardName();

    /**
     * Gets a {@linkplain MinecraftServer server} that this {@linkplain Entity entity} is part of.
     *
     * @return the server
     * @since 1.0
     */
    @NonNull MinecraftServer server();

    /**
     * Represents a hand of an entity.
     *
     * <p>Contents of this enum depend on Minecraft, however it is safe to keep it an enum, since it is very unlikely
     * to change.</p>
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

        private Animation(@NonNull String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        @Override
        public String toString() {
            return "Animation{name=" + this.name + "}";
        }
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
    final class Status {    
        /**
         * A {@linkplain Status status} that spawns tipped arrow particle effects.
         *
         * @since 1.0
         */
        public static final Status ARROW_TIP = new Status("arrow_tip");
    
        /**
         * A {@linkplain Status status} that makes a rabbit play its jump animation.
         *
         * @since 1.0
         */
        public static final Status RABBIT_JUMP = new Status("rabbit_jump");
    
        /**
         * A {@linkplain Status status} that displays particles on eggs or living entities when a projectile hits.
         *
         * @since 1.0
         */
        public static final Status PROJECTILE_HIT = new Status("projectile_hit");
    
        /**
         * A {@linkplain Status status} that plays the attack animation for golems, hoglins, ravagers, wardens and zoglins.
         *
         * @since 1.0
         */
        public static final Status ATTACK = new Status("attack");
    
        /**
         * A {@linkplain Status status} that spawns smoke particles when taming fails.
         *
         * @since 1.0
         */
        public static final Status TAMING_FAIL = new Status("taming_fail");
    
        /**
         * A {@linkplain Status status} that spawns heart particles when taming succeeds.
         *
         * @since 1.0
         */
        public static final Status TAMING_SUCCESS = new Status("taming_success");
    
        /**
         * A {@linkplain Status status} that plays the wolf shaking water animation.
         *
         * @since 1.0
         */
        public static final Status WOLF_SHAKE = new Status("wolf_shake");
    
        /**
         * A {@linkplain Status status} that marks item use as finished for players.
         *
         * @since 1.0
         */
        public static final Status PLAYER_ITEM_USE_FINISH = new Status("player_item_use_finish");
    
        /**
         * A {@linkplain Status status} that makes sheep play the eating-grass animation.
         *
         * @since 1.0
         */
        public static final Status SHEEP_EAT = new Status("sheep_eat");
    
        /**
         * A {@linkplain Status status} that makes an Iron Golem hold out a poppy.
         *
         * @since 1.0
         */
        public static final Status GOLEM_HOLD_POPPY = new Status("golem_hold_poppy");
    
        /**
         * A {@linkplain Status status} that spawns villager mating heart particles.
         *
         * @since 1.0
         */
        public static final Status VILLAGER_MATING = new Status("villager_mating");
    
        /**
         * A {@linkplain Status status} that spawns villager angry particles.
         *
         * @since 1.0
         */
        public static final Status VILLAGER_ANGRY = new Status("villager_angry");
    
        /**
         * A {@linkplain Status status} that spawns villager happy particles.
         *
         * @since 1.0
         */
        public static final Status VILLAGER_HAPPY = new Status("villager_happy");
    
        /**
         * A {@linkplain Status status} that spawns witch magic particles.
         *
         * @since 1.0
         */
        public static final Status WITCH_MAGIC = new Status("witch_magic");
    
        /**
         * A {@linkplain Status status} that plays the Zombie Villager cure sound.
         *
         * @since 1.0
         */
        public static final Status ZOMBIE_VILLAGER_CURE = new Status("zombie_villager_cure");
    
        /**
         * A {@linkplain Status status} that triggers a firework explosion effect.
         *
         * @since 1.0
         */
        public static final Status FIREWORK_EXPLODE = new Status("firework_explode");
    
        /**
         * A {@linkplain Status status} that spawns love-mode heart particles for animals or Allay.
         *
         * @since 1.0
         */
        public static final Status ANIMAL_LOVE = new Status("animal_love");
    
        /**
         * A {@linkplain Status status} that resets a squid's rotation to 0 radians.
         *
         * @since 1.0
         */
        public static final Status SQUID_ROTATION_RESET = new Status("squid_rotation_reset");
    
        /**
         * A {@linkplain Status status} that spawns an explosion particle for mobs.
         *
         * @since 1.0
         */
        public static final Status MOB_EXPLOSION = new Status("mob_explosion");
    
        /**
         * A {@linkplain Status status} that plays a guardian attack sound.
         *
         * @since 1.0
         */
        public static final Status GUARDIAN_ATTACK = new Status("guardian_attack");
    
        /**
         * A {@linkplain Status status} that enables reduced debug screen information for players.
         *
         * @since 1.0
         */
        public static final Status PLAYER_DEBUG_REDUCED = new Status("player_debug_reduced");
    
        /**
         * A {@linkplain Status status} that disables reduced debug screen information for players.
         *
         * @since 1.0
         */
        public static final Status PLAYER_DEBUG_NORMAL = new Status("player_debug_normal");
    
        /**
         * A {@linkplain Status status} that sets player op level to 0.
         *
         * @since 1.0
         */
        public static final Status PLAYER_OP_0 = new Status("player_op_0");
    
        /**
         * A {@linkplain Status status} that sets player op level to 1.
         *
         * @since 1.0
         */
        public static final Status PLAYER_OP_1 = new Status("player_op_1");
    
        /**
         * A {@linkplain Status status} that sets player op level to 2.
         *
         * @since 1.0
         */
        public static final Status PLAYER_OP_2 = new Status("player_op_2");
    
        /**
         * A {@linkplain Status status} that sets player op level to 3.
         *
         * @since 1.0
         */
        public static final Status PLAYER_OP_3 = new Status("player_op_3");
    
        /**
         * A {@linkplain Status status} that sets player op level to 4.
         *
         * @since 1.0
         */
        public static final Status PLAYER_OP_4 = new Status("player_op_4");
    
        /**
         * A {@linkplain Status status} that plays the death animation for living entities.
         *
         * @since 1.0
         */
        public static final Status DEATH_ANIMATION = new Status("death_animation");
    
        /**
         * A {@linkplain Status status} that plays the shield block animation/sound for living entities.
         *
         * @since 1.0
         */
        public static final Status SHIELD_BLOCK = new Status("shield_block");
    
        /**
         * A {@linkplain Status status} that plays a shield break sound for living entities.
         *
         * @since 1.0
         */
        public static final Status SHIELD_BREAK = new Status("shield_break");
    
        /**
         * A {@linkplain Status status} that pulls a player caught by a fishing hook.
         *
         * @since 1.0
         */
        public static final Status FISHING_PULL_PLAYER = new Status("fishing_pull_player");
    
        /**
         * A {@linkplain Status status} that plays a hit sound and resets cooldown for an Armor Stand.
         *
         * @since 1.0
         */
        public static final Status ARMOR_STAND_HIT = new Status("armor_stand_hit");
    
        /**
         * A {@linkplain Status status} that puts away an Iron Golem's poppy.
         *
         * @since 1.0
         */
        public static final Status GOLEM_PUTAWAY_POPPY = new Status("golem_putaway_poppy");
    
        /**
         * A {@linkplain Status status} that plays the totem of undying animation.
         *
         * @since 1.0
         */
        public static final Status TOTEM_OF_UNDYING = new Status("totem_of_undying");
    
        /**
         * A {@linkplain Status status} that causes dolphins to display happy villager particles.
         *
         * @since 1.0
         */
        public static final Status DOLPHIN_HAPPY = new Status("dolphin_happy");
    
        /**
         * A {@linkplain Status status} that marks a ravager as stunned.
         *
         * @since 1.0
         */
        public static final Status RAVAGER_STUNNED = new Status("ravager_stunned");
    
        /**
         * A {@linkplain Status status} that spawns smoke particles when ocelot taming fails.
         *
         * @since 1.0
         */
        public static final Status OCELOT_TAMING_FAIL = new Status("ocelot_taming_fail");
    
        /**
         * A {@linkplain Status status} that spawns heart particles when ocelot taming succeeds.
         *
         * @since 1.0
         */
        public static final Status OCELOT_TAMING_SUCCESS = new Status("ocelot_taming_success");
    
        /**
         * A {@linkplain Status status} that spawns villager splash particles during a raid.
         *
         * @since 1.0
         */
        public static final Status VILLAGER_SPLASH = new Status("villager_splash");
    
        /**
         * A {@linkplain Status status} that spawns cloud particles when a player's Bad Omen effect is removed.
         *
         * @since 1.0
         */
        public static final Status PLAYER_BAD_OMEN_CLOUD = new Status("player_bad_omen_cloud");
    
        /**
         * A {@linkplain Status status} that spawns particles based on the fox's held item.
         *
         * @since 1.0
         */
        public static final Status FOX_CHEW = new Status("fox_chew");
    
        /**
         * A {@linkplain Status status} that spawns portal particles for teleporting entities.
         *
         * @since 1.0
         */
        public static final Status PORTAL_PARTICLES = new Status("portal_particles");
    
        /**
         * A {@linkplain Status status} that plays equipment break sound and spawns break particles (main hand).
         *
         * @since 1.0
         */
        public static final Status MAIN_HAND_BREAK = new Status("main_hand_break");
    
        /**
         * A {@linkplain Status status} that plays equipment break sound and spawns break particles (off hand).
         *
         * @since 1.0
         */
        public static final Status OFF_HAND_BREAK = new Status("off_hand_break");
    
        /**
         * A {@linkplain Status status} that plays equipment break sound and spawns break particles (head slot).
         *
         * @since 1.0
         */
        public static final Status HEAD_BREAK = new Status("head_break");
    
        /**
         * A {@linkplain Status status} that plays equipment break sound and spawns break particles (chest slot).
         *
         * @since 1.0
         */
        public static final Status CHEST_BREAK = new Status("chest_break");
    
        /**
         * A {@linkplain Status status} that plays equipment break sound and spawns break particles (legs slot).
         *
         * @since 1.0
         */
        public static final Status LEGS_BREAK = new Status("legs_break");
    
        /**
         * A {@linkplain Status status} that plays equipment break sound and spawns break particles (feet slot).
         *
         * @since 1.0
         */
        public static final Status FEET_BREAK = new Status("feet_break");
    
        /**
         * A {@linkplain Status status} that spawns honey block slide particles.
         *
         * @since 1.0
         */
        public static final Status HONEY_BLOCK_SLIDE = new Status("honey_block_slide");
    
        /**
         * A {@linkplain Status status} that spawns honey block fall particles.
         *
         * @since 1.0
         */
        public static final Status HONEY_BLOCK_FALL = new Status("honey_block_fall");
    
        /**
         * A {@linkplain Status status} that swaps the entity's hand items.
         *
         * @since 1.0
         */
        public static final Status SWAP_HANDS = new Status("swap_hands");
    
        /**
         * A {@linkplain Status status} that stops the wolf shaking-water animation.
         *
         * @since 1.0
         */
        public static final Status WOLF_SHAKE_STOP = new Status("wolf_shake_stop");
    
        /**
         * A {@linkplain Status status} that lowers a goat's head for ramming.
         *
         * @since 1.0
         */
        public static final Status GOAT_RAM = new Status("goat_ram");
    
        /**
         * A {@linkplain Status status} that stops the goat lowering its head.
         *
         * @since 1.0
         */
        public static final Status GOAT_STOP_RAM = new Status("goat_stop_ram");
    
        /**
         * A {@linkplain Status status} that spawns death smoke particles.
         *
         * @since 1.0
         */
        public static final Status DEATH_SMOKE = new Status("death_smoke");
    
        /**
         * A {@linkplain Status status} that performs tendril shaking animation for the Warden.
         *
         * @since 1.0
         */
        public static final Status WARDEN_TENDRIL_SHAKE = new Status("warden_tendril_shake");
    
        /**
         * A {@linkplain Status status} that performs the sonic boom attack animation for the Warden.
         *
         * @since 1.0
         */
        public static final Status WARDEN_SONIC_BOOM = new Status("warden_sonic_boom");
    
        /**
         * A {@linkplain Status status} that plays a digging animation sound for the Sniffer.
         *
         * @since 1.0
         */
        public static final Status SNIFFER_DIG = new Status("sniffer_dig");
    
        private final String name;
    
        private Status(@NonNull String name) {
            this.name = Objects.requireNonNull(name, "name");
        }
    
        @Override
        public String toString() {
            return "Status{name=" + this.name + "}";
        }
    }
}
