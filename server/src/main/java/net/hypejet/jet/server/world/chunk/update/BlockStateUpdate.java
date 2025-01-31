package net.hypejet.jet.server.world.chunk.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.block.JetBlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an update of {@linkplain JetBlockState a block state} that should be made
 * in {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} on a coordinate with values specified.
 *
 * @param blockX a section-relative {@code X} coordinate value where the change should be made
 * @param blockY an absolute {@code Y} coordinate value where the change should be made
 * @param blockZ a section-relative {@code Z} coordinate value where the change should be made
 * @param blockState a new block state that should be put on the coordinate with values specified
 * @since 1.0
 */
public record BlockStateUpdate(byte blockX, short blockY, byte blockZ, @NonNull JetBlockState blockState) {
    /**
     * Constructs the {@linkplain BlockStateUpdate block state update}.
     *
     * @param blockX a section-relative {@code X} coordinate value where the change should be made
     * @param blockY an absolute {@code Y} coordinate value where the change should be made
     * @param blockZ a section-relative {@code Z} coordinate value where the change should be made
     * @param blockState a new block state that should be put on the coordinate with values specified
     * @since 1.0
     */
    public BlockStateUpdate {
        NullabilityUtil.requireNonNull(blockState, "block state");
    }
}