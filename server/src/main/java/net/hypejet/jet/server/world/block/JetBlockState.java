package net.hypejet.jet.server.world.block;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.world.block.BlockState;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents an implementation of {@linkplain BlockState a block state}.
 *
 * @param blockKey a key of a block that owns the block state
 * @param properties a properties of the block state
 * @param isAir whether the block state should be recognised as an air
 * @since 1.0
 * @see BlockState
 */
public record JetBlockState(@NonNull Key blockKey, @NonNull Map<String, String> properties, boolean isAir)
        implements BlockState {
    /**
     * Constructs the {@linkplain JetBlockState block state}.
     *
     * @param blockKey a key of a block that owns the block state
     * @param properties a properties of the block state
     * @param isAir whether the block state should be recognised as an air
     * @since 1.0
     */
    public JetBlockState {
        NullabilityUtil.requireNonNull(blockKey, "block key");
        properties = Map.copyOf(NullabilityUtil.requireNonNull(properties, "properties"));
    }
}