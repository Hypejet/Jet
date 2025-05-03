package net.hypejet.jet.server.world.block;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.entity.BlockEntityType;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.world.block.BlockType;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

/**
 * Represents an implementation of {@linkplain BlockType a block type}.
 *
 * @param requiredFeatureFlags required feature flags that are required to enable the block type
 * @param blockEntityType a registry entry of a block entity type that blocks of the block type should use
 *                        for their block entities, {@code null} if the block type does not support block entity
 *                        types
 * @since 1.0
 */
public record JetBlockType(@NonNull Set<Key> requiredFeatureFlags,
                           @Nullable JetRegistryEntry<BlockEntityType> blockEntityType) implements BlockType {
    /**
     * Constructs the {@linkplain JetBlockType block type implementation}.
     *
     * @param requiredFeatureFlags required feature flags that are required to enable the block type
     * @param blockEntityType a registry entry of a block entity type that blocks of the block type should use
     *                        for their block entities, {@code null} if the block type does not support block entity
     *                        types
     * @since 1.0
     */
    public JetBlockType {
        NullabilityUtil.requireNonNull(requiredFeatureFlags, "required feature flags");
    }
}