package net.hypejet.jet.world.block.attachment;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A type of how bell block is attached to another block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class BellAttachmentType {
    /**
     * The bell attachment type indicating that the bell is attached to a block below it.
     *
     * @since 1.0
     */
    public static final BellAttachmentType FLOOR = new BellAttachmentType("floor");

    /**
     * The bell attachment type indicating that the bell is attached to a block above it.
     *
     * @since 1.0
     */
    public static final BellAttachmentType CEILING = new BellAttachmentType("ceiling");

    /**
     * The bell attachment type indicating that the bell is attached to a single block on side.
     *
     * @since 1.0
     */
    public static final BellAttachmentType SINGLE_WALL = new BellAttachmentType("single_wall");

    /**
     * The bell attachment type indicating that the bell is attached to two side blocks.
     *
     * @since 1.0
     */
    public static final BellAttachmentType DOUBLE_WALL = new BellAttachmentType("double_wall");

    private final String name;

    private BellAttachmentType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "BellAttachmentType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}