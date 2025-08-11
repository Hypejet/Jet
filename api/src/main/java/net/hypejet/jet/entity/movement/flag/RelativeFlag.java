package net.hypejet.jet.entity.movement.flag;

import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a flag indicating that a specific value related to movement
 * of {@linkplain net.hypejet.jet.entity.player.Player a player} should be relative to another value.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example.</p>
 *
 * @since 1.0
 */
public final class RelativeFlag {
    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Position#x() an X position value}
     * should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag X = new RelativeFlag("X");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Position#y() an Y position value}
     * should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag Y = new RelativeFlag("Y");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Position#z() an Z position value}
     * should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag Z = new RelativeFlag("Z");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Position#pitch() a pitch position value}
     * should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag PITCH = new RelativeFlag("pitch");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Position#yaw() a yaw position value}
     * should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag YAW = new RelativeFlag("yaw");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Vector#x() an X vector value} of a delta
     * movement should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag DELTA_X = new RelativeFlag("delta X");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Vector#x() an X vector value} of a delta
     * movement should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag DELTA_Y = new RelativeFlag("delta Y");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Vector#z() an Z vector value} of a delta
     * movement should be relative to another value.
     *
     * @since 1.0
     */
    public static final RelativeFlag DELTA_Z = new RelativeFlag("delta Z");

    /**
     * {@linkplain RelativeFlag A relative flag} indicating that {@linkplain Vector vector} of a delta movement should
     * be rotated by an angle of rotation changes of {@linkplain Position a position}.
     *
     * @since 1.0
     */
    public static final RelativeFlag ROTATE_DELTA = new RelativeFlag("rotate delta");

    /**
     * {@linkplain Set A set} of {@linkplain RelativeFlag relative flags} that make anything related to a view
     * relative, except of {@linkplain RelativeFlag#ROTATE_DELTA delta rotation}.
     *
     * @since 1.0
     */
    public static final Set<RelativeFlag> VIEW = Set.of(YAW, PITCH);

    /**
     * {@linkplain Set A set} of {@linkplain RelativeFlag relative flags} that make anything related
     * to {@linkplain Position a position} relative.
     *
     * @since 1.0
     */
    public static final Set<RelativeFlag> POSITION = Set.of(X, Y, Z, YAW, PITCH);

    /**
     * {@linkplain Set A set} of all {@linkplain RelativeFlag relative flags}.
     *
     * @since 1.0
     */
    public static final Set<RelativeFlag> ALL = Set.of(X, Y, Z, YAW, PITCH, DELTA_X, DELTA_Y, DELTA_Z, ROTATE_DELTA);

    private final String name;

    /**
     * Constructs the {@linkplain RelativeFlag relative flag}.
     *
     * @param name a name that the relative flag should have
     * @since 1.0
     */
    private RelativeFlag(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       since all instances are defined in constants of this class. */

    @Override
    public String toString() {
        return "RelativeFlag{" +
                "name='" + this.name + '\'' +
                '}';
    }
}