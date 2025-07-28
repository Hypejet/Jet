package net.hypejet.jet.server.registry.blockstate;

import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockState;
import net.hypejet.jet.data.json.resource.JsonDataResourceFiles;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.util.data.JetDataUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.block.JetBlockType;
import net.hypejet.jet.world.block.BlockType;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an implementation of a {@linkplain BlockStateRegistry block-state registry}.
 *
 * @since 1.0
 */
public final class JetBlockStateRegistry implements BlockStateRegistry {

    private final ElementOrder<JetBlockState> order;

    private final Map<JetRegistryEntry<JetBlockType>, Map<Map<String, String>, JetBlockState>> possibleStates;
    private final Map<JetRegistryEntry<JetBlockType>, JetBlockState> defaultStates;

    /**
     * Constructs the {@linkplain BlockStateRegistry block state registry}.
     *
     * @param blockTypeRegistry a registry of all possible block types
     * @param blockDataEntries a list of deserialized data registry entries representing unconverted block types
     * @since 1.0
     */
    public JetBlockStateRegistry(@NonNull JetMinecraftRegistry<JetBlockType> blockTypeRegistry,
                                 @NonNull List<JsonRegistryEntry<JsonBlock>> blockDataEntries) {
        NullabilityUtil.requireNonNull(blockTypeRegistry, "block type registry");

        List<JsonRegistryEntry<JsonBlockState>> blockStateDataEntries = JetDataUtil.deserializeEntries(
                JsonDataResourceFiles.BLOCK_STATES,
                JsonBlockState.class
        );

        List<JetBlockState> blockStates = new ArrayList<>();
        for (JsonRegistryEntry<JsonBlockState> blockStateEntry : blockStateDataEntries) {
            Key blockTypeKey = blockStateEntry.key();
            JetRegistryEntry<JetBlockType> blockType = blockTypeRegistry.get(blockTypeKey);

            if (blockType == null)
                throw new IllegalArgumentException(String.format("Could not find a %s block type", blockTypeKey));
            JsonBlockState blockState = blockStateEntry.value();

            blockStates.add(new JetBlockState(
                    blockType, blockState.properties(), blockState.isAir(),
                    blockState.hasFluidState(), blockState.blocksMotion()
            ));
        }

        ElementOrder<JetBlockState> blockStateOrder = new ElementOrder<>(blockStates);

        Map<JetRegistryEntry<JetBlockType>, Map<Map<String, String>, JetBlockState>> possibleStates = new HashMap<>();
        Map<JetRegistryEntry<JetBlockType>, JetBlockState> defaultStates = new HashMap<>();

        for (JsonRegistryEntry<JsonBlock> blockDataEntry : blockDataEntries) {
            Key blockTypeKey = blockDataEntry.key();
            JetRegistryEntry<JetBlockType> blockType = blockTypeRegistry.get(blockTypeKey);

            if (blockType == null)
                throw new IllegalArgumentException(String.format("Could not find a %s block type", blockTypeKey));
            JsonBlock block = blockDataEntry.value();

            JetBlockState defaultState = blockStateOrder.getOrThrow(block.defaultBlockStateId());
            defaultStates.put(blockType, defaultState);

            Map<Map<String, String>, JetBlockState> propertiesToStateMap = new HashMap<>();
            block.blockStateIds().forEach(stateIdentifier -> {
                JetBlockState state = blockStateOrder.getOrThrow(stateIdentifier);
                propertiesToStateMap.put(state.properties(), state);
            });

            possibleStates.put(blockType, Map.copyOf(propertiesToStateMap));
        }

        this.order = blockStateOrder;
        this.possibleStates = Map.copyOf(possibleStates);
        this.defaultStates = Map.copyOf(defaultStates);
    }

    @Override
    public @NonNull JetBlockState defaultBlockState(@NonNull RegistryEntry<? extends BlockType> blockType) {
        NullabilityUtil.requireNonNull(blockType, "block type");
        if (!(blockType instanceof JetRegistryEntry<? extends BlockType>)) {
            throw new IllegalArgumentException(
                    "The block-type registry entry specified is not a valid registry entry"
            );
        }

        JetBlockState blockState = this.defaultStates.get(blockType);
        if (blockState == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a default block state for a %s block type",
                    blockType.key()
            ));
        }

        return blockState;
    }

    @Override
    public @NonNull JetBlockState blockState(@NonNull RegistryEntry<? extends BlockType> blockType,
                                             @NonNull Map<String, String> properties) {
        NullabilityUtil.requireNonNull(blockType, "block type");
        NullabilityUtil.requireNonNull(properties, "properties");

        if (!(blockType instanceof JetRegistryEntry<? extends BlockType>)) {
            throw new IllegalArgumentException(
                    "The block-type registry entry specified is not a valid registry entry"
            );
        }

        Map<Map<String, String>, JetBlockState> possibleStates = this.possibleStates.get(blockType);
        if (possibleStates == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a map of possible block states for a %s block type",
                    blockType.key()
            ));
        }

        JetBlockState blockState = possibleStates.get(properties);
        if (blockState == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block state with properties of %s for %s block type",
                    properties, blockType.key()
            ));
        }

        return blockState;
    }

    /**
     * Gets {@linkplain ElementOrder an element order} of all possible {@linkplain JetBlockState block states}.
     *
     * @return the element order
     * @since 1.0
     */
    public @NonNull ElementOrder<JetBlockState> order() {
        return this.order;
    }
}