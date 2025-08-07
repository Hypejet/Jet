package net.hypejet.jet.scoreboard.score.render;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a way of displaying score numbers of {@linkplain net.hypejet.jet.scoreboard.score.Score scores}.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.scoreboard.score.Score
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
        this.name = Objects.requireNonNull(name, "name");
    }

    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       as all instances are defined in the constant section of this class. */

    @Override
    public String toString() {
        return "RenderType{" +
                "name='" + this.name + '\'' +
                '}';
    }
}