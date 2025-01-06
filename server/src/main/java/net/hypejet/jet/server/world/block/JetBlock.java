package net.hypejet.jet.server.world.block;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.world.block.Block;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

/**
 * Represents implementation of {@linkplain Block a block}.
 *
 * @param requiredFeatureFlags feature flags that are needed to be enabled to use the block
 * @param defaultBlockState a default block state of the block
 * @since 1.0
 * @see Block
 */
public record JetBlock(@NonNull Set<Key> requiredFeatureFlags, @NonNull JetBlockState defaultBlockState)
        implements Block {
    /**
     * Constructs the {@linkplain JetBlock block}.
     *
     * @param requiredFeatureFlags feature flags that are needed to be enabled to use the block
     * @param defaultBlockState a default block state of the block
     * @since 1.0
     */
    public JetBlock {
        NullabilityUtil.requireNonNull(requiredFeatureFlags, "required feature flags");
        NullabilityUtil.requireNonNull(defaultBlockState, "default block state");
        requiredFeatureFlags = Set.copyOf(requiredFeatureFlags);
    }
}