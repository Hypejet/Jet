package net.hypejet.jet.data.json.model.type.damage;

/**
 * A difficulty scaling type of {@linkplain JsonDamageType damage type}.
 *
 * @since 1.0
 */
public enum JsonDamageScalingType {
    /**
     * A scaling type making the damage always the same, regardless of the difficulty.
     *
     * @since 1.0
     */
    NEVER,
    /**
     * A scaling type scaling the damage only if an attacker is not a player, but is a living entity.
     *
     * @since 1.0
     */
    WHEN_CAUSED_BY_LIVING_NON_PLAYER,
    /**
     * A scaling type always scaling damage with the difficulty.
     *
     * @since 1.0
     */
    ALWAYS
}