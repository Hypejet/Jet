package net.hypejet.jet.server.world.chunk.light.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.storage.LightStorage;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain LightStorage light storage}.
 *
 * @param position a position where the change should be made
 * @param value a value that the light value at the coordinate specified should be updated with
 * @since 1.0
 * @see LightStorage
 */
public record LightStorageUpdate(@NonNull ChunkPaletteRelativePosition position, byte value) {
    /**
     * Constructs the {@linkplain LightStorageUpdate light storage update}.
     *
     * @param position a position where the change should be made
     * @param value a value that the light value at the coordinate specified should be updated with
     * @since 1.0
     */
    public LightStorageUpdate {
        NullabilityUtil.requireNonNull(position, "position");
    }
}