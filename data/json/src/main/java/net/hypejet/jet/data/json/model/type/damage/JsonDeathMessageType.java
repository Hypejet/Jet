package net.hypejet.jet.data.json.model.type.damage;

/**
 * A message type displayed when players or pets die.
 *
 * @since 1.0
 */
public enum JsonDeathMessageType {
    /**
     * A message type using the standard death message logic.
     *
     * @since 1.0
     */
    DEFAULT,
    /**
     * A message type displaying fall damage death messages.
     *
     * @since 1.0
     */
    FALL_VARIANTS,
    /**
     * A message type displaying {@code intentional game design} message.
     *
     * @since 1.0
     */
    INTENTIONAL_GAME_DESIGN
}