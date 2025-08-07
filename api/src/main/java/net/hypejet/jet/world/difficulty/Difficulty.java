package net.hypejet.jet.world.difficulty;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a level of Minecraft difficulty.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example.</p>
 *
 * @since 1.0
 */
public final class Difficulty {
    /**
     * A peaceful difficulty level.
     *
     * @since 1.0
     */
    public static final Difficulty PEACEFUL = new Difficulty("peaceful");

    /**
     * An easy difficulty level.
     *
     * @since 1.0
     */
    public static final Difficulty EASY = new Difficulty("easy");

    /**
     * A normal difficulty level.
     *
     * @since 1.0
     */
    public static final Difficulty NORMAL = new Difficulty("normal");

    /**
     * A hard difficulty level.
     *
     * @since 1.0
     */
    public static final Difficulty HARD = new Difficulty("hard");

    private final String name;

    private Difficulty(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    /**
     * Gets a readable lower-case name of this difficulty level.
     *
     * @return the name
     * @since 1.0
     */
    public @NonNull String name() {
        return this.name;
    }

    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       since all instances are defined in constants of this class. */

    @Override
    public String toString() {
        return "Difficulty{" +
                "name='" + this.name + '\'' +
                '}';
    }
}