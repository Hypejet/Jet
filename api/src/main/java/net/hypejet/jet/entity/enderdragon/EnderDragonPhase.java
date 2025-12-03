package net.hypejet.jet.entity.enderdragon;

import org.jspecify.annotations.NonNull;

import net.hypejet.jet.entity.Entity;

/**
 * A phase of an ender dragon {@linkplain Entity entity}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Entity
 */
public final class EnderDragonPhase {
    /**
     * A circling phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase CIRCLING = new EnderDragonPhase("circling");

    /**
     * A strafing phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase STRAFING = new EnderDragonPhase("strafing");

    /**
     * A flying to portal to land phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase FLYING_TO_PORTAL_TO_LAND = new EnderDragonPhase("flying_to_portal_to_land");

    /**
     * A landing on portal phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase LANDING_ON_PORTAL = new EnderDragonPhase("landing_on_portal");

    /**
     * A taking off from portal phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase TAKING_OFF_FROM_PORTAL = new EnderDragonPhase("taking_off_from_portal");

    /**
     * A breath attack phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase BREATH_ATTACK = new EnderDragonPhase("breath_attack");

    /**
     * A searching for player phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase SEARCHING_FOR_PLAYER = new EnderDragonPhase("searching_for_player");

    /**
     * A roaring phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase ROARING = new EnderDragonPhase("roaring");

    /**
     * A charging player phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase CHARGING_PLAYER = new EnderDragonPhase("charging_player");

    /**
     * A flying to portal to die phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase FLYING_TO_PORTAL_TO_DIE = new EnderDragonPhase("flying_to_portal_to_die");

    /**
     * A hovering phase.
     *
     * @since 1.0
     */
    public static final EnderDragonPhase HOVERING = new EnderDragonPhase("hovering");

    private final String name;

    private EnderDragonPhase(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EnderDragonPhase{" +
                "name='" + this.name + '\'' +
                '}';
    }
}