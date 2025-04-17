package net.hypejet.jet.world.chunk.factory.light;

import net.hypejet.jet.util.array.NibbleArray;
import net.hypejet.jet.world.chunk.light.LightStorage;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a factory of {@linkplain LightStorage light storages}.
 *
 * @since 1.0
 * @see LightStorage
 */
public interface LightStorageFactory {
    /**
     * Creates {@linkplain LightStorage a light storage} for {@linkplain NibbleArray a nibble array} of light-level
     * values specified.
     *
     * <p>Indices of the nibble array correspond to light-level values of blocks with the same indices in block-state
     * {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette chunk palettes}.</p>
     *
     * @param array the array
     * @return the light storage
     * @since 1.0
     */
    @NonNull LightStorage createDirect(@NonNull NibbleArray array);

    /**
     * Gets an instance of {@linkplain LightStorage a light storage} with all light-level values set to {@code 0}.
     *
     * @return the instance
     * @since 1.0
     */
    @NonNull LightStorage emptyLightStorage();
}