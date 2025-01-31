package net.hypejet.jet.server.world.chunk.update;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of a light value that should be made
 * in {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} on a coordinate with values specified.
 *
 * @param blockX a section-relative {@code X} coordinate value where the change should be made
 * @param blockY an absolute {@code Y} coordinate value where the change should be made
 * @param blockZ a section-relative {@code Z} coordinate value where the change should be made
 * @param lightValue a new light value that should be put on the coordinate with values specified
 * @param lightType a type of light whose value should be updated
 * @since 1.0
 */
public record LightUpdate(byte blockX, short blockY, byte blockZ, byte lightValue, @NonNull LightType lightType) {
    /**
     * A type of Minecraft light.
     *
     * @since 1.0
     */
    public enum LightType {
        /**
         * {@linkplain LightType a light type}, which is used when the light comes from sky.
         *
         * @since 1.0
         */
        SKY,
        /**
         * {@linkplain LightType a light type}, which is used when the light comes from a block.
         *
         * @since 1.0
         */
        BLOCK
    }
}