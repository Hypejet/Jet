package net.hypejet.jet.scoreboard.score.render;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a way how score numbers of {@linkplain ??? a scoreboard objective} should be displayed.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example.</p>
 *
 * @since 1.0
 * @see ???
 */
public final class RenderType {
    /**
     * {@linkplain RenderType A render type} where score numbers are displayed as integers.
     *
     * @since 1.0
     */
    public static final RenderType INTEGER = new RenderType("integer");

    /**
     * {@linkplain RenderType A render type} where score numbers are displayed as hearts, where half a heart represents
     * one point.
     *
     * @since 1.0
     */
    public static final RenderType HEARTS = new RenderType("hearts");

    private final String name;

    /**
     * Constructs the {@linkplain RenderType render type}.
     *
     * @param name a name that the render type should have
     * @since 1.0
     */
    private RenderType(@NonNull String name) {
        this.name = NullabilityUtil.requireNonNull(name, "name");
    }


    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       since all instances are defined in constants of this class. */

    @Override
    public String toString() {
        return "RenderType{" +
                "name='" + name + '\'' +
                '}';
    }
}