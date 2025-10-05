package net.hypejet.jet.world.block.orientation;

import net.hypejet.jet.world.direction.Direction;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A combination of block front and top face {@linkplain Direction directions}.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 * @see Direction
 */
@NullMarked
public final class FrontAndTop {
    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing downwards and whose front is facing east.
     *
     * @since 1.0
     */
    public static final FrontAndTop DOWN_EAST = new FrontAndTop(Direction.DOWN, Direction.EAST);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing downwards and whose front is facing north.
     *
     * @since 1.0
     */
    public static final FrontAndTop DOWN_NORTH = new FrontAndTop(Direction.DOWN, Direction.NORTH);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing downwards and whose front is facing south.
     *
     * @since 1.0
     */
    public static final FrontAndTop DOWN_SOUTH = new FrontAndTop(Direction.DOWN, Direction.SOUTH);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing downwards and whose front is facing west.
     *
     * @since 1.0
     */
    public static final FrontAndTop DOWN_WEST = new FrontAndTop(Direction.DOWN, Direction.WEST);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing upwards and whose front is facing east.
     *
     * @since 1.0
     */
    public static final FrontAndTop UP_EAST = new FrontAndTop(Direction.UP, Direction.EAST);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing upwards and whose front is facing north.
     *
     * @since 1.0
     */
    public static final FrontAndTop UP_NORTH = new FrontAndTop(Direction.UP, Direction.NORTH);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing upwards and whose front is facing south.
     *
     * @since 1.0
     */
    public static final FrontAndTop UP_SOUTH = new FrontAndTop(Direction.UP, Direction.SOUTH);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing upwards and whose front is facing west.
     *
     * @since 1.0
     */
    public static final FrontAndTop UP_WEST = new FrontAndTop(Direction.UP, Direction.WEST);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing west and whose front is facing up.
     *
     * @since 1.0
     */
    public static final FrontAndTop WEST_UP = new FrontAndTop(Direction.WEST, Direction.UP);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing east and whose front is facing up.
     *
     * @since 1.0
     */
    public static final FrontAndTop EAST_UP = new FrontAndTop(Direction.EAST, Direction.UP);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing north and whose front is facing up.
     *
     * @since 1.0
     */
    public static final FrontAndTop NORTH_UP = new FrontAndTop(Direction.NORTH, Direction.UP);

    /**
     * A {@linkplain FrontAndTop front-and-top} whose top is facing south and whose front is facing up.
     *
     * @since 1.0
     */
    public static final FrontAndTop SOUTH_UP = new FrontAndTop(Direction.SOUTH, Direction.UP);

    private final Direction top;
    private final Direction front;

    private FrontAndTop(Direction top, Direction front) {
        this.top = Objects.requireNonNull(top, "top");
        this.front = Objects.requireNonNull(front, "front");
    }

    @Override
    public String toString() {
        return "FrontAndTop{" +
                "top=" + this.top +
                ", front=" + this.front +
                '}';
    }
}