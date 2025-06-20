package net.hypejet.jet.scoreboard.score.number;

import java.util.Objects;

/**
 * Represents {@linkplain NumberFormat a number format} specifying
 * that the score number should not be displayed at all.
 *
 * @since 1.0
 */
public final class BlankNumberFormat implements NumberFormat {
    /**
     * An instance of the {@linkplain BlankNumberFormat blank number format}.
     *
     * @since 1.0
     */
    public static final BlankNumberFormat INSTANCE = new BlankNumberFormat();

    private BlankNumberFormat() {}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlankNumberFormat;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "BlankNumberFormat{}";
    }
}