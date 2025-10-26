package net.hypejet.jet.world.block.connection;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A type of redstone block connection with another redstone block on side.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class RedstoneConnectionType {
    /**
     * A {@linkplain RedstoneConnectionType redstone connection type} indicating that a redstone block
     * connected with another redstone block, placed higher than the first block.
     *
     * @since 1.0
     */
    public static final RedstoneConnectionType UP = new RedstoneConnectionType("up");

    /**
     * A {@linkplain RedstoneConnectionType redstone connection type} indicating that a redstone block
     * connected with another redstone block, placed on the same height as the first block.
     *
     * @since 1.0
     */
    public static final RedstoneConnectionType SIDE = new RedstoneConnectionType("side");

    /**
     * A {@linkplain RedstoneConnectionType redstone connection type}
     * indicating that there is no connection with a side block.
     *
     * @since 1.0
     */
    public static final RedstoneConnectionType NONE = new RedstoneConnectionType("none");

    private final String name;

    private RedstoneConnectionType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "RedstoneConnectionType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}