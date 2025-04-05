package net.hypejet.jet.world.chunk.factory.light;

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
     * Creates {@linkplain LightStorage a light storage} for an array specified. The array must use a light-value
     * array format, specified in a header javadoc of {@linkplain LightStorage a light storage} class.
     *
     * @param lightValues the array
     * @return the light storage
     * @since 1.0
     */
    @NonNull LightStorage createDirect(byte @NonNull [] lightValues);

    /**
     * Gets an instance of {@linkplain LightStorage a light storage} with all values set to {@code 0}.
     *
     * @return the instance
     * @since 1.0
     */
    @NonNull LightStorage emptyLightStorage();
}