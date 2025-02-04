package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain JetBlockState a block state} that should be made
 * in {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} on a coordinate with values specified.
 *
 * @param position a position where the change should be made
 * @param blockState a new block state that should be put on the coordinate with values specified
 * @since 1.0
 */
public record BlockStateUpdate(@NonNull ChunkRelativePosition position, @NonNull JetBlockState blockState) {
    /**
     * Constructs the {@linkplain BlockStateUpdate block state update}.
     *
     * @param position a position where the change should be made
     * @param blockState a new block state that should be put on the coordinate with values specified
     * @since 1.0
     */
    public BlockStateUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockState, "block state");

        if (position.paletteType() != ChunkPaletteType.BLOCK_STATE)
            throw new IllegalArgumentException("The position has not been made for block state chunk palettes");
    }
}