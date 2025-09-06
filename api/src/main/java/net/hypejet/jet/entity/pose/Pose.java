package net.hypejet.jet.entity.pose;

import net.hypejet.jet.entity.Entity;
import org.jspecify.annotations.NonNull;

/**
 * A pose of an {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
public final class Pose {
    /**
     * The standing pose.
     *
     * @since 1.0
     */
    public static final Pose STANDING = new Pose("standing");

    /**
     * The gliding pose.
     *
     * @since 1.0
     */
    public static final Pose GLIDING = new Pose("gliding");

    /**
     * The sleeping pose.
     *
     * @since 1.0
     */
    public static final Pose SLEEPING = new Pose("sleeping");

    /**
     * The swimming pose.
     *
     * @since 1.0
     */
    public static final Pose SWIMMING = new Pose("swimming");

    /**
     * The pose used while charging from a trident with the riptide enchantment.
     *
     * @since 1.0
     */
    public static final Pose SPIN_ATTACK = new Pose("spin_attack");

    /**
     * The crouching pose.
     *
     * @since 1.0
     */
    public static final Pose CROUCHING = new Pose("crouching");

    /**
     * The long jumping pose.
     *
     * @since 1.0
     */
    public static final Pose LONG_JUMPING = new Pose("long_jumping");

    /**
     * The dying pose.
     *
     * @since 1.0
     */
    public static final Pose DYING = new Pose("dying");

    /**
     * The croaking pose.
     *
     * @since 1.0
     */
    public static final Pose CROAKING = new Pose("croaking");

    /**
     * The using-tongue pose.
     *
     * @since 1.0
     */
    public static final Pose USING_TONGUE = new Pose("using_tongue");

    /**
     * The sitting pose.
     *
     * @since 1.0
     */
    public static final Pose SITTING = new Pose("sitting");

    /**
     * The roaring pose.
     *
     * @since 1.0
     */
    public static final Pose ROARING = new Pose("roaring");

    /**
     * The sniffing pose.
     *
     * @since 1.0
     */
    public static final Pose SNIFFING = new Pose("sniffing");

    /**
     * The emerging pose.
     *
     * @since 1.0
     */
    public static final Pose EMERGING = new Pose("emerging");

    /**
     * The digging pose.
     *
     * @since 1.0
     */
    public static final Pose DIGGING = new Pose("digging");

    /**
     * The sliding pose.
     *
     * @since 1.0
     */
    public static final Pose SLIDING = new Pose("sliding");

    /**
     * The shooting pose.
     *
     * @since 1.0
     */
    public static final Pose SHOOTING = new Pose("shooting");

    /**
     * The inhaling pose.
     *
     * @since 1.0
     */
    public static final Pose INHALING = new Pose("inhaling");

    private final String name;

    private Pose(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pose{" +
                "name='" + this.name + '\'' +
                '}';
    }
}