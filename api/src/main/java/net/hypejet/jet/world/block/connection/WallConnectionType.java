package net.hypejet.jet.world.block.connection;

import org.jspecify.annotations.NullMarked;

/**
 * A type of wall block connection with a side block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class WallConnectionType {
    /**
     * A {@linkplain WallConnectionType wall connection type}
     * indicating that there is no connection with a side block.
     *
     * @since 1.0
     */
    public static final WallConnectionType NONE = new WallConnectionType("none");

    /**
     * A {@linkplain WallConnectionType wall connection type}
     * indicating that there is a low connection with a side block.
     *
     * @since 1.0
     */
    public static final WallConnectionType LOW = new WallConnectionType("low");

    /**
     * A {@linkplain WallConnectionType wall connection type}
     * indicating that there is a tall connection with a side block.
     *
     * @since 1.0
     */
    public static final WallConnectionType TALL = new WallConnectionType("tall");

    private final String name;

    private WallConnectionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WallConnectionType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}