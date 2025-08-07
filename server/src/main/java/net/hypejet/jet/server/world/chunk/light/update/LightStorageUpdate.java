package net.hypejet.jet.server.world.chunk.light.update;

import java.util.Objects;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain AbstractLightStorage a light storage}.
 *
 * @param position a position where the change should be made
 * @param value a value that a light level at the coordinate specified should be replaced with
 * @since 1.0
 * @see AbstractLightStorage
 */
public record LightStorageUpdate(@NonNull ChunkPaletteRelativePosition position, byte value) {
    /**
     * Constructs the {@linkplain LightStorageUpdate light storage update}.
     *
     * @param position a position where the change should be made
     * @param value a value that a light level at the coordinate specified should be replaced with
     * @since 1.0
     */
    public LightStorageUpdate {
        Objects.requireNonNull(position, "position");
    }
}