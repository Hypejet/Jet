package net.hypejet.jet.world.block.comparator;

import org.jspecify.annotations.NullMarked;

/**
 * The mode of the comparator block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class ComparatorMode {
    /**
     * A {@linkplain ComparatorMode comparator mode} indicating that the comparator block is in the comparison mode.
     *
     * @since 1.0
     */
    public static final ComparatorMode COMPARE = new ComparatorMode("compare");

    /**
     * A {@linkplain ComparatorMode comparator mode} indicating that the comparator block is in the subtraction mode.
     *
     * @since 1.0
     */
    public static final ComparatorMode SUBTRACT = new ComparatorMode("subtract");

    private final String name;

    private ComparatorMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ComparatorMode{" +
                "name='" + this.name + '\'' +
                '}';
    }
}