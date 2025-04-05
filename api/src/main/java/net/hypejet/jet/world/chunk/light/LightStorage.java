package net.hypejet.jet.world.chunk.light;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a storage, which stores light values
 * of {@linkplain net.hypejet.jet.world.chunk.light.LightSection a light section}.
 *
 * <p>Values in the light storage are stores as a light-value array format. A light-value array contains two light
 * values per element, where {@code 4} most significant bits store light value for a lower block index
 * and {@code 4} least significant bits store light value for a higher block index.</p>
 *
 * <p>Block indices are the same indices as in block-state
 * {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette chunk palettes}.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.light.LightSection
 */
public interface LightStorage {
    /**
     * Gets light values stored in this light storage as a light-value array with preserved order.
     *
     * @return the light-value array
     * @since 1.0
     */
    byte @NonNull [] data();

    /**
     * Gets an array, which contains unique values, which this light storage uses.
     *
     * @return the array
     * @since 1.0
     */
    byte @NonNull [] uniqueValues();

    /**
     * Gets a number of times that a light value specified is being used by this light storage.
     *
     * @param value the light value
     * @return the number of times
     * @since 1.0
     */
    short valueCount(byte value);
}