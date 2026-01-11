package net.hypejet.jet.world.block.stairs;

import org.jspecify.annotations.NullMarked;

/**
 * A shape of the stairs block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class StairsShape {
    /**
     * The straight shape of stairs.
     *
     * @since 1.0
     */
    public static final StairsShape STRAIGHT = new StairsShape("straight");

    /**
     * The shape of stairs where when looking at the block face the stairs make a connection with the stairs
     * on the left side (which are facing in the same direction) with the stairs behind (which are facing
     * to the right when looking at the connection block face) the block with this stairs shape.
     *
     * @since 1.0
     */
    public static final StairsShape INNER_LEFT = new StairsShape("inner_left");

    /**
     * The shape of stairs where when looking at the block face the stairs make a connection with the stairs
     * on the right side (which are facing in the same direction) with the stairs behind (which are facing
     * to the left when looking at the connection block face) the block with this stairs shape.
     *
     * @since 1.0
     */
    public static final StairsShape INNER_RIGHT = new StairsShape("inner_right");

    /**
     * The shape of stairs where when looking at the block face the stairs make a connection with the stairs
     * on the right side (which are facing in the same direction) with the stairs in front of the block
     * with this stairs shape (which are facing to the right when looking at the connection block face).
     *
     * @since 1.0
     */
    public static final StairsShape OUTER_LEFT = new StairsShape("outer_left");

    /**
     * The shape of stairs where when looking at the block face the stairs make a connection with the stairs
     * on the left side (which are facing in the same direction) with the stairs in front of the block
     * with this stairs shape (which are facing to the left when looking at the connection block face).
     *
     * @since 1.0
     */
    public static final StairsShape OUTER_RIGHT = new StairsShape("outer_right");

    private final String name;

    private StairsShape(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StairsShape{" +
                "name='" + this.name + '\'' +
                '}';
    }
}