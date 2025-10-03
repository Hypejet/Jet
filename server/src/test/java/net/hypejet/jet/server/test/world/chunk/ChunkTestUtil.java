package net.hypejet.jet.server.test.world.chunk;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.util.game.number.IntProvider;
import net.hypejet.jet.world.dimension.DimensionType;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * A set of utilities for tests related to {@linkplain net.hypejet.jet.server.world.chunk.JetChunk chunks}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.JetChunk
 */
public final class ChunkTestUtil {
    /**
     * A {@linkplain DimensionType dimension type} which can be used for tests related to chunks.
     *
     * @since 1.0
     */
    public static final DimensionType DIMENSION_TYPE = new DimensionType(
            null, true, false, false, true, 1,
            true, true, -64, 384, 384,
            Key.key("infiniburn_overworld"), Key.key("overworld"), 0f, 256,
            new DimensionType.MonsterSettings(false, true, new IntProvider.Uniform(0, 7), 0)
    );

    private ChunkTestUtil() {}

    /**
     * Creates a mockup {@linkplain JetBlockState block state} without optional data.
     *
     * @param key the key of a block type that the block state should be associated with
     * @param isAir whether the block state should be recognised as an air
     * @param hasFluidState whether the block state should be recognised as a block state
     *                      that has a fluid state associated with it
     * @param blockMotion whether the block state should be recognised as a block state that blocks motion
     * @param isLeaves whether the block state should be recognised as a block state associated with a leaves block
     * @return the block state
     * @since 1.0
     */
    public static @NonNull JetBlockState createMockupBlockState(@NonNull Key key, boolean isAir,
                                                                boolean hasFluidState, boolean blockMotion,
                                                                boolean isLeaves) {
        return new JetBlockState(new Holder.Reference<>(key), Map.of(), isAir, hasFluidState, blockMotion, isLeaves);
    }
}