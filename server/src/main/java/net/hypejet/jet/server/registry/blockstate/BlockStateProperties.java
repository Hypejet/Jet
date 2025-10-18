package net.hypejet.jet.server.registry.blockstate;

import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.hypejet.jet.data.json.resource.JsonDataResourceFiles;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.util.data.JetDataUtil;
import net.hypejet.jet.server.world.block.JetBlockType;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.server.world.block.state.property.StateProperty;
import net.hypejet.jet.world.block.BlockType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Something providing {@linkplain StateProperty state properties} valid for
 * {@linkplain JetBlockState block states} of all {@linkplain JetBlockType block types}.
 *
 * @since 1.0
 * @see StateProperty
 * @see JetBlockState
 * @see JetBlockType
 */
@NullMarked
public final class BlockStateProperties {

    private static final Map<Key, Map<String, StateProperty<?>>> BLOCK_TYPE_TO_PROPERTIES;

    private BlockStateProperties() {}

    static {
        List<JsonRegistryEntry<JsonBlock>> blockEntries = JetDataUtil.deserializeEntries(
                JsonDataResourceFiles.BLOCKS,
                JsonBlock.class
        );

        Map<Key, Map<String, StateProperty<?>>> blockTypeToPropetiesMap = new HashMap<>();
        for (JsonRegistryEntry<JsonBlock> blockEntry : blockEntries) {
            Map<String, StateProperty<?>> properties = new HashMap<>();
            for (Map.Entry<String, JsonStateProperty> entry : blockEntry.value().blockStateProperties().entrySet())
                properties.put(entry.getKey(), StateProperty.convert(entry.getValue()));
            blockTypeToPropetiesMap.put(blockEntry.key(), Map.copyOf(properties));
        }

        BLOCK_TYPE_TO_PROPERTIES = Map.copyOf(blockTypeToPropetiesMap);
    }

    /**
     * Gets a {@linkplain Map map} associating property names with {@linkplain StateProperty state properties}
     * supported by the specified {@linkplain BlockType block type}.
     *
     * @param blockType a holder referencing to the block type whose state properties are desired
     * @return the map of supported state property names
     * @throws IllegalArgumentException if no state properties were loaded for the specified block type
     * @since 1.0
     */
    public static Map<String, StateProperty<?>> properties(Holder.Reference<BlockType> blockType) {
        Key blockTypeKey = blockType.key();
        Map<String, StateProperty<?>> properties = BLOCK_TYPE_TO_PROPERTIES.get(blockTypeKey);

        if (properties == null) {
            throw new IllegalArgumentException(String.format(
                    "No state properties were loaded for %s block type",
                    blockTypeKey
            ));
        }

        return properties;
    }
}