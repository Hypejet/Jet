package net.hypejet.jet.entity.damage.type;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A message type displayed when players or pets die.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 */
public final class DeathMessageType {
    /**
     * A message type using the standard death message logic.
     *
     * @since 1.0
     */
    public static final DeathMessageType DEFAULT = new DeathMessageType("default");

    /**
     * A message type displaying fall damage death messages.
     *
     * @since 1.0
     */
    public static final DeathMessageType FALL_VARIANTS = new DeathMessageType("fall_variants");

    /**
     * A message type displaying {@code intentional game design} message.
     *
     * @since 1.0
     */
    public static final DeathMessageType INTENTIONAL_GAME_DESIGN = new DeathMessageType("intentional_game_design");

    private final String name;

    private DeathMessageType(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "DeathMessageType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}