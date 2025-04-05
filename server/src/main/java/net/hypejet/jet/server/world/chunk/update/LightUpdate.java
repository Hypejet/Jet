package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.light.LightType;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of a light value that should be made
 * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position}
 * in some {@linkplain JetChunk chunk}.
 *
 * @param position a position where the change should be made
 * @param lightValue a new light value that should be put at the position specified
 * @param lightType a type of light whose value should be updated
 * @since 1.0
 */
public record LightUpdate(@NonNull ChunkRelativeBlockPosition position,
                          byte lightValue, @NonNull LightType lightType) {
    /**
     * Constructs the {@linkplain LightUpdate light update}.
     *
     * @param position a position where the change should be made
     * @param lightValue a new light value that should be put at the position specified
     * @param lightType a type of light whose value should be updated
     * @since 1.0
     */
    public LightUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(lightType, "light type");
    }
}