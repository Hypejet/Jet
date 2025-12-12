package net.hypejet.jet.entity.illager;

/**
 * The type of spell currently performed by a spellcaster illager.
 *
 * @since 1.0
 */
public enum IllagerSpellType {
    /**
     * A spell type indicating that the spellcaster illager is not currently performing any spell.
     *
     * @since 1.0
     */
    NONE,
    /**
     * A spell type summoning vexes.
     *
     * @since 1.0
     */
    SUMMON_VEX,
    /**
     * A spell type summoning fangs.
     *
     * @since 1.0
     */
    SUMMON_FANGS,
    /**
     * A spell type changing color of a blue sheep to red.
     *
     * @since 1.0
     */
    SHEEP_COLOR_SWAP,
    /**
     * A spell type making the spellcaster illager invisible.
     *
     * @since 1.0
     */
    DISAPPEAR,
    /**
     * A spell type adding the blindness effect to the target of the spellcaster illager.
     *
     * @since 1.0
     */
    BLINDNESS
}