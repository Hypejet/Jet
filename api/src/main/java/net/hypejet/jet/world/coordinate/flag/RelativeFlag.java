package net.hypejet.jet.world.coordinate.flag;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A flag indicating that a specific value of {@linkplain Position position} or velocity {@linkplain Vector vector}
 * of an {@linkplain Entity entity} should be relative to another one.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example.</p>
 *
 * @since 1.0
 * @see Position
 * @see Vector
 * @see Entity
 */
public final class RelativeFlag {
    /**
     * A {@linkplain RelativeFlag relative flag} indicating that an {@code X}
     * axis value of a {@linkplain Position position} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag X = new RelativeFlag("X");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that an {@code Y}
     * axis value of a {@linkplain Position position} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag Y = new RelativeFlag("Y");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that an {@code Z}
     * axis value of a {@linkplain Position position} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag Z = new RelativeFlag("Z");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that a yaw head
     * rotation value of a {@linkplain Position position} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag PITCH = new RelativeFlag("pitch");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that a pitch head
     * rotation value of a {@linkplain Position position} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag YAW = new RelativeFlag("yaw");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that an {@code X}
     * axis value of a velocity {@linkplain Vector vector} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag VELOCITY_X = new RelativeFlag("velocity-X");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that an {@code Y}
     * axis value of a velocity {@linkplain Vector vector} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag VELOCITY_Y = new RelativeFlag("velocity-Y");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that an {@code Z}
     * axis value of a velocity {@linkplain Vector vector} should be relative.
     *
     * @since 1.0
     */
    public static final RelativeFlag VELOCITY_Z = new RelativeFlag("velocity-Z");

    /**
     * A {@linkplain RelativeFlag relative flag} indicating that a velocity {@linkplain Vector vector}
     * should be rotated accordingly to the {@linkplain Position position} head rotation changes.
     *
     * @since 1.0
     */
    public static final RelativeFlag ROTATE_VELOCITY = new RelativeFlag("rotate-velocity");

    private final String name;
    
    private RelativeFlag(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "RelativeFlag{" +
                "name='" + this.name + '\'' +
                '}';
    }
}