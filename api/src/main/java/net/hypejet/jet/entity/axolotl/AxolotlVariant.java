package net.hypejet.jet.entity.axolotl;

import org.jspecify.annotations.NonNull;

import net.hypejet.jet.entity.Entity;

/**
 * A variant of an axolotl {@linkplain Entity entity}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Entity
 */
public final class AxolotlVariant {

    /**
     * A lucy variant.
     *
     * @since 1.0
     */
    public static final AxolotlVariant LUCY = new AxolotlVariant("lucy");

    /**
     * A wild variant.
     *
     * @since 1.0
     */
    public static final AxolotlVariant WILD = new AxolotlVariant("wild");

     /**
     * A gold variant.
     *
     * @since 1.0
     */
    public static final AxolotlVariant GOLD = new AxolotlVariant("gold");

    /**
     * A cyan variant.
     *
     * @since 1.0
     */
    public static final AxolotlVariant CYAN = new AxolotlVariant("cyan");

    /**
     * A blue variant.
     *
     * @since 1.0
     */
    public static final AxolotlVariant BLUE = new AxolotlVariant("blue");

    private final String name;

    private AxolotlVariant(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AxolotlVariant{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
