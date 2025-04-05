package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an update of a block that should be made
 * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} in some {@linkplain JetChunk chunk}.
 *
 * @param position a position where the change should be made
 * @param blockState a new block state that block at the position should have
 * @param blockEntity a new block entity that block at the position should have, {@code null} if none
 * @since 1.0
 */
public record BlockUpdate(@NonNull ChunkRelativeBlockPosition position, @NonNull BlockState blockState,
                          @Nullable BlockEntity blockEntity) {
    /**
     * Constructs the {@linkplain BlockUpdate block update}.
     *
     * @param position a position where the change should be made
     * @param blockState a new block state that block at the position should have
     * @param blockEntity a new block entity that block at the position should have, {@code null} if none
     * @since 1.0
     */
    public BlockUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockState, "block state");
    }
}