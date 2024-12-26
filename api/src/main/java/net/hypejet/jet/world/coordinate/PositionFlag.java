package net.hypejet.jet.world.coordinate;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a flag of a position update of {@linkplain net.hypejet.jet.entity.player.Player a player}.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example.</p>
 *
 * @since 1.0
 */
public final class PositionFlag {
    /**
     * A flag indicating that a player is on ground.
     *
     * @since 1.0
     */
    public static final PositionFlag ON_GROUND = new PositionFlag("on ground");

    /**
     * A flag indicating that a player is colliding horizontally.
     *
     * @since 1.0
     */
    public static final PositionFlag HORIZONTAL_COLLISION = new PositionFlag("horizontal collision");

    private final String name;

    private PositionFlag(@NonNull String name) {
        this.name = NullabilityUtil.requireNonNull(name, "name");
    }

    /**
     * Gets a readable lower-case name of this position flag.
     *
     * @return the name
     * @since 1.0
     */
    public @NonNull String name() {
        return this.name;
    }

    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       since all instances are defined in constants of this class. */

    @Override
    public String toString() {
        return "PositionFlag{" +
                "name='" + this.name + '\'' +
                '}';
    }
}