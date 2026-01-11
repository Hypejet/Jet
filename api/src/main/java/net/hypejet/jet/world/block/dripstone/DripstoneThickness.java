package net.hypejet.jet.world.block.dripstone;

import org.jspecify.annotations.NullMarked;

/**
 * Thickness of a pointed dripstone block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class DripstoneThickness {
    /**
     * The {@linkplain DripstoneThickness dripstone thickness} used when the pointed dripstone block
     * is the tip of a stalactite or stalagmite which is connecting to another stalactite or stalagmite.
     *
     * @since 1.0
     */
    public static final DripstoneThickness TIP_MERGE = new DripstoneThickness("tip_merge");

    /**
     * The {@linkplain DripstoneThickness dripstone thickness} used when
     * the pointed dripstone block is the tip of a stalactite or stalagmite.
     *
     * @since 1.0
     */
    public static final DripstoneThickness TIP = new DripstoneThickness("tip");

    /**
     * The {@linkplain DripstoneThickness dripstone thickness} used when the pointed
     * dripstone block is the block just before the tip of a stalactite or stalagmite.
     *
     * @since 1.0
     */
    public static final DripstoneThickness FRUSTUM = new DripstoneThickness("frustum");

    /**
     * The {@linkplain DripstoneThickness dripstone thickness} used when
     * the pointed dripstone block is the middle of a stalactite or stalagmite.
     *
     * @since 1.0
     */
    public static final DripstoneThickness MIDDLE = new DripstoneThickness("middle");

    /**
     * The {@linkplain DripstoneThickness dripstone thickness} used when
     * the pointed dripstone block is the base of a stalactite or stalagmite.
     *
     * @since 1.0
     */
    public static final DripstoneThickness BASE = new DripstoneThickness("base");

    private final String name;

    private DripstoneThickness(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DripstoneThickness{" +
                "name='" + this.name + '\'' +
                '}';
    }
}