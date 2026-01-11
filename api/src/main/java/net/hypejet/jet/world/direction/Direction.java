package net.hypejet.jet.world.direction;

import org.jspecify.annotations.NonNull;

/**
 * A Minecraft direction.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 */
public final class Direction {
    /**
     * The downward direction.
     *
     * @since 1.0
     */
    public static final Direction DOWN = new Direction("down");

    /**
     * The upward direction.
     *
     * @since 1.0
     */
    public static final Direction UP = new Direction("up");

    /**
     * The northern direction.
     *
     * @since 1.0
     */
    public static final Direction NORTH = new Direction("north");

    /**
     * The southern direction.
     *
     * @since 1.0
     */
    public static final Direction SOUTH = new Direction("south");

    /**
     * The western direction.
     *
     * @since 1.0
     */
    public static final Direction WEST = new Direction("west");

    /**
     * The eastern direction.
     *
     * @since 1.0
     */
    public static final Direction EAST = new Direction("east");

    private final String name;

    private Direction(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "name='" + this.name + '\'' +
                '}';
    }
}