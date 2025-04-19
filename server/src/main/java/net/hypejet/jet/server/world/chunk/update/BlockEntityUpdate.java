package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of a block entity data of a block that should be made
 * at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} in some {@linkplain JetChunk chunk}.
 *
 * @param position a position where the change should be made
 * @param blockEntity a new block entity data that block at the position should have
 * @since 1.0
 */
public record BlockEntityUpdate(@NonNull ChunkRelativeBlockPosition position, @NonNull CompoundBinaryTag blockEntity) {
    /**
     * Constructs the {@linkplain BlockEntityUpdate block entity update}.
     *
     * @param position a position where the change should be made
     * @param blockEntity a new block entity data that block at the position should have
     * @since 1.0
     */
    public BlockEntityUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(blockEntity, "block entity");
    }
}