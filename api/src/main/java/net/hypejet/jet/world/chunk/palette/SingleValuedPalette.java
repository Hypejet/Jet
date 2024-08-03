package net.hypejet.jet.world.chunk.palette;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain Palette palette}, which contains a single value.
 *
 * @param value the value
 * @since 1.0
 * @author Codsestech
 * @see Palette
 */
public record SingleValuedPalette(int value) implements Palette {
    @Override
    public long @NonNull [] entries() {
        return new long[0];
    }
}