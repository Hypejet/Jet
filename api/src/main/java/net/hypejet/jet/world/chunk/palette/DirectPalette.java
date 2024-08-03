package net.hypejet.jet.world.chunk.palette;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain Palette palette}, whose entries are identifiers.
 *
 * @param entries the entries
 * @since 1.0
 * @author Codestech
 * @see Palette
 */
public record DirectPalette(long @NonNull [] entries) implements Palette {
    /**
     * Constructs the {@linkplain DirectPalette direct palette}.
     *
     * <p>The entry array is being cloned during construction, to prevent modifications on the record.</p>
     *
     * @param entries the entries
     * @since 1.0
     */
    public DirectPalette {
        entries = entries.clone();
    }

    /**
     * Gets the entries.
     *
     * <p>The array returned is a clone to prevent modifications of the original array.</p>
     *
     * @return the entries
     * @since 1.0
     */
    @Override
    public long @NonNull [] entries() {
        return this.entries.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records natively do not compare contents of arrays
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof DirectPalette that)) return false;
        return Objects.deepEquals(this.entries, that.entries);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records natively do not compare contents of arrays
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.entries);
    }
}