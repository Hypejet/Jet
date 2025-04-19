package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain BlockState a block state} of a block that should be made
 * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} in some {@linkplain JetChunk chunk}.
 *
 * @param position a position where the change should be made
 * @param blockState a new block state that block at the position should have
 * @since 1.0
 */
public record BlockStateUpdate(@NonNull ChunkRelativeBlockPosition position, @NonNull BlockState blockState) {
    /**
     * Constructs the {@linkplain BlockStateUpdate block state update}.
     *
     * @param position a position where the change should be made
     * @param blockState a new block state that block at the position should have
     * @since 1.0
     */
    public BlockStateUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockState, "block state");
    }
}