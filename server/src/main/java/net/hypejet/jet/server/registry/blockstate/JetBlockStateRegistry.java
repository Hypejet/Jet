package net.hypejet.jet.server.registry.blockstate;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.state.JsonBlockState;
import net.hypejet.jet.data.json.resource.JsonDataResourceFiles;
import net.hypejet.jet.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.util.data.JetDataUtil;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.state.BlockStateReference;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An implementation of the {@linkplain BlockStateRegistry block-state registry}.
 *
 * @since 1.0
 * @see BlockStateRegistry
 */
@NullMarked
public final class JetBlockStateRegistry implements BlockStateRegistry {

    private final List<JetBlockState> blockStates;
    private final Object2IntMap<JetBlockState> blockStateToIndexMap;
    private final Map<Key, Map<Map<String, Object>, JetBlockState>> possibleStates;

    /**
     * Constructs the {@linkplain BlockStateRegistry block state registry}.
     *
     * @since 1.0
     */
    public JetBlockStateRegistry() {
        List<JsonRegistryEntry<JsonBlockState>> blockStateDataEntries = JetDataUtil.deserializeEntries(
                JsonDataResourceFiles.BLOCK_STATES,
                JsonBlockState.class
        );

        List<JsonRegistryEntry<JsonBlock>> blockDataEntries = JetDataUtil.deserializeEntries(
                JsonDataResourceFiles.BLOCKS,
                JsonBlock.class
        );

        List<JetBlockState> blockStates = new ArrayList<>();
        Object2IntMap<JetBlockState> blockStateToIndexMap = new Object2IntOpenHashMap<>();

        for (int index = 0; index < blockStateDataEntries.size(); index++) {
            JsonRegistryEntry<JsonBlockState> dataEntry = blockStateDataEntries.get(index);
            JsonBlockState blockState = dataEntry.value();
            Holder.Reference<BlockType> blockTypeReference = new Holder.Reference<>(dataEntry.key());

            JetBlockState convertedBlockState = new JetBlockState(
                    blockTypeReference,
                    BlockStateProperties.convertProperties(blockTypeReference, blockState.properties()),
                    blockState.isAir(),
                    blockState.hasFluidState(),
                    blockState.blocksMotion(),
                    blockState.isLeaves()
            );

            blockStates.add(index, convertedBlockState);
            blockStateToIndexMap.put(convertedBlockState, index);
        }

        this.blockStates = List.copyOf(blockStates);
        this.blockStateToIndexMap = Object2IntMaps.unmodifiable(blockStateToIndexMap);

        Map<Key, Map<Map<String, Object>, JetBlockState>> possibleStates = new HashMap<>();
        for (JsonRegistryEntry<JsonBlock> blockDataEntry : blockDataEntries) {
            Key blockTypeKey = blockDataEntry.key();
            JsonBlock block = blockDataEntry.value();

            Map<Map<String, Object>, JetBlockState> propertiesToStateMap = new HashMap<>();
            block.blockStateIds().forEach(stateIdentifier -> {
                JetBlockState state = this.byIndex(stateIdentifier);
                propertiesToStateMap.put(Map.copyOf(state.properties()), state);
            });

            possibleStates.put(blockTypeKey, Map.copyOf(propertiesToStateMap));
        }

        this.possibleStates = Map.copyOf(possibleStates);
    }

    @Override
    public JetBlockState defaultBlockState(Holder.Reference<BlockType> blockType) {
        Objects.requireNonNull(blockType, "block type");
        return this.blockState(new BlockStateReference(blockType, BlockStateProperties.defaultProperties(blockType)));
    }

    @Override
    public JetBlockState blockState(BlockStateReference reference) {
        Objects.requireNonNull(reference, "reference");

        Key blockTypeKey = reference.blockType().key();
        Map<String, Object> properties = reference.properties();

        Map<Map<String, Object>, JetBlockState> possibleStates = this.possibleStates.get(blockTypeKey);
        if (possibleStates == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a map of possible block states for \"%s\" block type",
                    blockTypeKey
            ));
        }

        JetBlockState blockState = possibleStates.get(properties);
        if (blockState == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block state with properties of %s for \"%s\" block type",
                    properties, blockTypeKey
            ));
        }

        return blockState;
    }

    /**
     * Gets a registry index of the specified {@linkplain BlockState block state}.
     *
     * @param blockState the block state
     * @return the registry index
     * @throws IllegalArgumentException if the specified block state is not a valid block state or this registry
     *                                  does not provide a registry index for it
     * @since 1.0
     */
    public int indexOf(BlockState blockState) {
        if (!(blockState instanceof JetBlockState))
            throw new IllegalArgumentException("The specified block state is not a valid block state");
        if (!this.blockStateToIndexMap.containsKey(blockState))
            throw new IllegalArgumentException("Could not find an index for the specified block state");
        return this.blockStateToIndexMap.getInt(blockState);
    }

    /**
     * Gets a {@linkplain JetBlockState block state} by its registry index.
     *
     * @param index the registry index
     * @return the block state
     * @throws IndexOutOfBoundsException if the specified index is out of bounds of the block state list
     * @since 1.0
     */
    public JetBlockState byIndex(int index) {
        return this.blockStates.get(index);
    }
}