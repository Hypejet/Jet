package net.hypejet.jet.world.chunk.palette;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain Palette palette}, whose entries are indices of identifies stored in the data.
 *
 * @param bitsPerEntry an amount of bits, which are used to encode entries
 * @param data the data
 * @param entries the entries
 * @since 1.0
 * @author Codestech
 * @see Palette
 */
public record IndirectPalette(short bitsPerEntry, int @NonNull [] data, long @NonNull [] entries) implements Palette {
    /**
     * Constructs the {@linkplain IndirectPalette indirect palette}.
     *
     * <p>The data and entry arrays are being cloned during construction to prevent modifications on the record./p>
     *
     * @param bitsPerEntry an amount of bits, which are used to encode entries
     * @param data the data
     * @param entries the entries
     * @since 1.0
     */
    public IndirectPalette {
        data = data.clone();
        entries = entries.clone();
    }

    /**
     * Gets the data.
     *
     * <p>The returned array is a clone to prevent modifications of the original array./p>
     *
     * @return the data
     * @since 1.0
     */
    @Override
    public int @NonNull [] data() {
        return this.data;
    }

    /**
     * Gets the entries.
     *
     * <p>The returned array is a clone to prevent modifications of the original array./p>
     *
     * @return the entries
     * @since 1.0
     */
    @Override
    public long @NonNull [] entries() {
        return this.entries;
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records natively do not compare contents of arrays
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof IndirectPalette that)) return false;
        return this.bitsPerEntry == that.bitsPerEntry && Objects.deepEquals(this.data, that.data)
                && Objects.deepEquals(this.entries, that.entries);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records natively do not compare contents of arrays
    @Override
    public int hashCode() {
        return Objects.hash(this.bitsPerEntry, Arrays.hashCode(this.data), Arrays.hashCode(this.entries));
    }
}