package net.hypejet.jet.world.block.tilt;

import org.jspecify.annotations.NullMarked;

/**
 * Tilt of a block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class Tilt {
    /**
     * A {@linkplain Tilt tilt} indicating that the block is not tilted at all.
     *
     * @since 1.0
     */
    public static final Tilt NONE = new Tilt("none");

    /**
     * A {@linkplain Tilt tilt} indicating that the block is not tilted, but starts becoming unstable.
     *
     * @since 1.0
     */
    public static final Tilt UNSTABLE = new Tilt("unstable");

    /**
     * A {@linkplain Tilt tilt} indicating that the block is partially tilted.
     *
     * @since 1.0
     */
    public static final Tilt PARTIAL = new Tilt("partial");

    /**
     * A {@linkplain Tilt tilt} indicating that the block is fully tilted.
     *
     * @since 1.0
     */
    public static final Tilt FULL = new Tilt("full");

    private final String name;

    private Tilt(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tilt{" +
                "name='" + this.name + '\'' +
                '}';
    }
}