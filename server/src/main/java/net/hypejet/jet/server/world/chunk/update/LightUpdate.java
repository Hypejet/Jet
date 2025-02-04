package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of a light value that should be made
 * in {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} on a coordinate with values specified.
 *
 * @param position a position where the change should be made
 * @param lightValue a new light value that should be put on the coordinate with values specified
 * @param lightType a type of light whose value should be updated
 * @since 1.0
 */
public record LightUpdate(@NonNull ChunkRelativePosition position, byte lightValue, @NonNull LightType lightType) {
    /**
     * Constructs the {@linkplain LightUpdate light update}.
     *
     * @param position a position where the change should be made
     * @param lightValue a new light value that should be put on the coordinate with values specified
     * @param lightType a type of light whose value should be updated
     * @since 1.0
     */
    public LightUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(lightType, "light type");
    }

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