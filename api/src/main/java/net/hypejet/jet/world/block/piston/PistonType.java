package net.hypejet.jet.world.block.piston;

import org.jspecify.annotations.NullMarked;

/**
 * A variant of the piston block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class PistonType {
    /**
     * The regular piston block variant.
     *
     * @since 1.0
     */
    public static final PistonType NORMAL = new PistonType("normal");

    /**
     * The sticky piston block variant.
     *
     * @since 1.0
     */
    public static final PistonType STICKY = new PistonType("sticky");

    private final String name;

    private PistonType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PistonType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}