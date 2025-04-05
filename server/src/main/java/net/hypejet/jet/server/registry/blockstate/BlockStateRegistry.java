package net.hypejet.jet.server.registry.blockstate;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.block.BlockType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain BlockState block states}.
 *
 * @since 1.0
 */
public final class BlockStateRegistry {

    private final ElementOrder<BlockState> order;
    private final Map<BlockState, JetRegistryEntry<BlockType>> stateToTypeMap;

    /**
     * Constructs the {@linkplain BlockStateRegistry block state registry}.
     *
     * @param order an order of all possible block states
     * @param blockTypeRegistry a registry of all possible block types
     * @since 1.0
     */
    public BlockStateRegistry(@NonNull ElementOrder<BlockState> order,
                              @NonNull JetMinecraftRegistry<BlockType> blockTypeRegistry) {
        this.order = NullabilityUtil.requireNonNull(order, "order");

        Map<BlockState, JetRegistryEntry<BlockType>> stateToTypeMap = new IdentityHashMap<>();
        for (JetRegistryEntry<BlockType> entry : blockTypeRegistry.entries())
            for (BlockState state : entry.value().possibleStates())
                stateToTypeMap.put(state, entry);

        this.stateToTypeMap = stateToTypeMap;
    }

    /**
     * Gets {@linkplain ElementOrder an element order} of all possible {@linkplain BlockState block states}.
     *
     * @return the element order
     * @since 1.0
     */
    public @NonNull ElementOrder<BlockState> order() {
        return this.order;
    }

    /**
     * Gets {@linkplain JetRegistryEntry a registry entry} of {@linkplain BlockType a block type}, which owns
     * {@linkplain BlockState a block state} specified.
     *
     * @param blockState the block state
     * @return the block type
     * @since 1.0
     */
    public @NonNull JetRegistryEntry<BlockType> blockType(@NonNull BlockState blockState) {
        JetRegistryEntry<BlockType> blockType = this.stateToTypeMap.get(blockState);
        if (blockType == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block type for a block state of %s",
                    blockState
            ));
        }
        return blockType;
    }
}