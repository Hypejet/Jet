package net.hypejet.jet.world.block.bamboo;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Type of leaves of a bamboo block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class BambooLeavesType {
    /**
     * A {@linkplain BambooLeavesType bamboo leaves type} indicating that the bamboo block has no leaves.
     *
     * @since 1.0
     */
    public static final BambooLeavesType NONE = new BambooLeavesType("none");

    /**
     * A {@linkplain BambooLeavesType bamboo leaves type} indicating that the bamboo block has small leaves.
     *
     * @since 1.0
     */
    public static final BambooLeavesType SMALL = new BambooLeavesType("small");

    /**
     * A {@linkplain BambooLeavesType bamboo leaves type} indicating that the bamboo block has large leaves.
     *
     * @since 1.0
     */
    public static final BambooLeavesType LARGE = new BambooLeavesType("large");

    private final String name;

    private BambooLeavesType(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "BambooLeavesType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}