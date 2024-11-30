package net.hypejet.jet.world.coordinate;

/**
 * Represents a flag of a position update of {@linkplain net.hypejet.jet.entity.player.Player a player}.
 *
 * @since 1.0
 * @author Codestech
 */
public enum PositionFlag {
    /**
     * A flag indicating that a player is on ground.
     *
     * @since 1.0
     */
    ON_GROUND,
    /**
     * A flag indicating that a player is colliding horizontally.
     *
     * @since 1.0
     */
    HORIZONTAL_COLLISION
}