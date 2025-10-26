package net.hypejet.jet.world.block.structure;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * The mode that a structure block is working in.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class StructureBlockMode {
    /**
     * The save {@linkplain StructureBlockMode structure block mode}.
     *
     * @since 1.0
     */
    public static final StructureBlockMode SAVE = new StructureBlockMode("save");

    /**
     * The load {@linkplain StructureBlockMode structure block mode}.
     *
     * @since 1.0
     */
    public static final StructureBlockMode LOAD = new StructureBlockMode("load");

    /**
     * The corner {@linkplain StructureBlockMode structure block mode}.
     *
     * @since 1.0
     */
    public static final StructureBlockMode CORNER = new StructureBlockMode("corner");

    /**
     * The data {@linkplain StructureBlockMode structure block mode}.
     *
     * @since 1.0
     */
    public static final StructureBlockMode DATA = new StructureBlockMode("data");

    private final String name;

    private StructureBlockMode(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "StructureBlockMode{" +
                "name='" + this.name + '\'' +
                '}';
    }
}