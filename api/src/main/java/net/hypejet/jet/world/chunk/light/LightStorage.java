package net.hypejet.jet.world.chunk.light;

import net.hypejet.jet.util.array.NibbleArray;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a storage, which stores light values
 * of {@linkplain net.hypejet.jet.world.chunk.light.LightSection a light section}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.light.LightSection
 */
public interface LightStorage {
    /**
     * Gets light-level values stored in this light storage as {@linkplain NibbleArray a nibble array}
     * with preserved order.
     *
     * <p>Indices of the nibble array correspond to light-level values of blocks with the same indices in block-state
     * {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette chunk palettes}.</p>
     *
     * @return the light-value array
     * @since 1.0
     */
    @NonNull NibbleArray data();

    /**
     * Gets an array, which contains unique light-level values, which this light storage uses.
     *
     * @return the array
     * @since 1.0
     */
    byte @NonNull [] uniqueValues();

    /**
     * Gets a number of times that a light-level value specified is being used by this light storage.
     *
     * @param value the light-level value
     * @return the number of times
     * @since 1.0
     */
    short valueCount(byte value);
}