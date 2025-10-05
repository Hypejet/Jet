package net.hypejet.jet.world.block.connection;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * The type of side chest connection of the chest block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class ChestConnectionType {
    /**
     * A {@linkplain ChestConnectionType chest connection type} indicating
     * that the chest block has no connection with side chest blocks.
     *
     * @since 1.0
     */
    public static final ChestConnectionType SINGLE = new ChestConnectionType("single");

    /**
     * A {@linkplain ChestConnectionType chest connection type} indicating that the chest
     * block has a connection with the chest on the left side when looking at its face.
     *
     * @since 1.0
     */
    public static final ChestConnectionType LEFT = new ChestConnectionType("left");

    /**
     * A {@linkplain ChestConnectionType chest connection type} indicating that the chest
     * block has a connection with the chest on the right side when looking at its face.
     *
     * @since 1.0
     */
    public static final ChestConnectionType RIGHT = new ChestConnectionType("right");

    private final String name;

    private ChestConnectionType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "ChestType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}