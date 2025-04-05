package net.hypejet.jet.world.chunk.light;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a section of light data of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public interface LightSection {
    /**
     * Gets {@linkplain LightStorage a light storage} of skylight values of this light section.
     *
     * @return the light-value array
     * @since 1.0
     */
    @NonNull LightStorage skyLightStorage();

    /**
     * Gets {@linkplain LightStorage a light storage} of block light values of this light section.
     *
     * @return the light-value array
     * @since 1.0
     */
    @NonNull LightStorage blockLightStorage();
}