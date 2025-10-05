package net.hypejet.jet.world.block.attachment;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A type of how block is attached to another block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class BlockAttachmentType {
    /**
     * The attachment type indicating that a block was attached to a block below it.
     *
     * @since 1.0
     */
    public static final BlockAttachmentType FLOOR = new BlockAttachmentType("floor");

    /**
     * The attachment type indicating that a block was attached to a block on the side.
     *
     * @since 1.0
     */
    public static final BlockAttachmentType WALL = new BlockAttachmentType("wall");

    /**
     * The attachment type indicating that a block was attached to a block above it.
     *
     * @since 1.0
     */
    public static final BlockAttachmentType CEILING = new BlockAttachmentType("ceiling");

    private String name;

    private BlockAttachmentType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "AttachmentFace{" +
                "name='" + this.name + '\'' +
                '}';
    }
}