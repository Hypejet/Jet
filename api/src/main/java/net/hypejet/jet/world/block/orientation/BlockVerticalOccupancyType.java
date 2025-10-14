package net.hypejet.jet.world.block.orientation;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An inner block height that should be occupied by the block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class BlockVerticalOccupancyType {
    /**
     * The occupancy of the top of the block.
     *
     * @since 1.0
     */
    public static final BlockVerticalOccupancyType TOP = new BlockVerticalOccupancyType("top");

    /**
     * The occupancy of the bottom of the block.
     *
     * @since 1.0
     */
    public static final BlockVerticalOccupancyType BOTTOM = new BlockVerticalOccupancyType("bottom");

    private final String name;

    public BlockVerticalOccupancyType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "BlockVerticalOccupancyType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}