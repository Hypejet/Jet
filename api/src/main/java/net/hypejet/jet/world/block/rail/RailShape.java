package net.hypejet.jet.world.block.rail;

import org.jspecify.annotations.NullMarked;

/**
 * A shape of the rail block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class RailShape {
    /**
     * A {@linkplain RailShape rail shape} connecting to northern and southern rails.
     *
     * @since 1.0
     */
    public static final RailShape NORTH_SOUTH = new RailShape("north_south");

    /**
     * A {@linkplain RailShape rail shape} connecting to northern and western rails.
     *
     * @since 1.0
     */
    public static final RailShape NORTH_WEST = new RailShape("north_west");

    /**
     * A {@linkplain RailShape rail shape} connecting to northern and eastern rails.
     *
     * @since 1.0
     */
    public static final RailShape NORTH_EAST = new RailShape("north_east");

    /**
     * A {@linkplain RailShape rail shape} connecting to southern and eastern rails.
     *
     * @since 1.0
     */
    public static final RailShape SOUTH_EAST = new RailShape("south_east");

    /**
     * A {@linkplain RailShape rail shape} connecting to southern and western rails.
     *
     * @since 1.0
     */
    public static final RailShape SOUTH_WEST = new RailShape("south_west");

    /**
     * A {@linkplain RailShape rail shape} ascending to the northern side.
     *
     * @since 1.0
     */
    public static final RailShape ASCENDING_NORTH = new RailShape("ascending_north");

    /**
     * A {@linkplain RailShape rail shape} ascending to the southern side.
     *
     * @since 1.0
     */
    public static final RailShape ASCENDING_SOUTH = new RailShape("ascending_south");

    /**
     * A {@linkplain RailShape rail shape} ascending to the eastern side.
     *
     * @since 1.0
     */
    public static final RailShape ASCENDING_EAST = new RailShape("ascending_east");

    /**
     * A {@linkplain RailShape rail shape} ascending to the western side.
     *
     * @since 1.0
     */
    public static final RailShape ASCENDING_WEST = new RailShape("ascending_west");

    private final String name;

    private RailShape(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RailShape{" +
                "name='" + this.name + '\'' +
                '}';
    }
}