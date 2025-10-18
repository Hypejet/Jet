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

    private static final Map<Key, Map<String, StateProperty<?>>> BLOCK_TYPE_TO_PROPERTIES_MAP;
    private static final Map<Key, Map<String, Object>> BLOCK_TYPE_TO_DEFAULT_PROPERTIES_MAP;

    private BlockStateProperties() {}

    static {
        List<JsonRegistryEntry<JsonBlock>> blockEntries = JetDataUtil.deserializeEntries(
                JsonDataResourceFiles.BLOCKS,
                JsonBlock.class
        );

        Map<Key, Map<String, StateProperty<?>>> blockTypeToPropertiesMap = new HashMap<>();
        for (JsonRegistryEntry<JsonBlock> blockEntry : blockEntries) {
            Map<String, StateProperty<?>> properties = new HashMap<>();
            for (Map.Entry<String, JsonStateProperty> entry : blockEntry.value().blockStateProperties().entrySet())
                properties.put(entry.getKey(), StateProperty.convert(entry.getValue()));
            blockTypeToPropertiesMap.put(blockEntry.key(), Map.copyOf(properties));
        }

        BLOCK_TYPE_TO_PROPERTIES_MAP = Map.copyOf(blockTypeToPropertiesMap);

        Map<Key, Map<String, Object>> blockTypeToDefaultPropertiesMap = new HashMap<>();
        for (JsonRegistryEntry<JsonBlock> blockEntry : blockEntries) {
            Holder.Reference<BlockType> blockType = new Holder.Reference<>(blockEntry.key());
            Map<String, String> defaultProperties = blockEntry.value().defaultBlockStateProperties();
            blockTypeToDefaultPropertiesMap.put(blockType.key(), convertProperties(blockType, defaultProperties));
        }

        BLOCK_TYPE_TO_DEFAULT_PROPERTIES_MAP = Map.copyOf(blockTypeToDefaultPropertiesMap);
    }

    /**
     * Gets a {@linkplain Map map} associating property names with {@linkplain StateProperty state properties}
     * supported by the specified {@linkplain BlockType block type}.
     *
     * @param blockType a holder referencing to the block type whose state properties are desired
     * @return the map of supported state property names
     * @throws IllegalArgumentException if state properties were not loaded for the specified block type
     * @since 1.0
     */
    public static Map<String, StateProperty<?>> properties(Holder.Reference<BlockType> blockType) {
        Key blockTypeKey = blockType.key();
        Map<String, StateProperty<?>> properties = BLOCK_TYPE_TO_PROPERTIES_MAP.get(blockTypeKey);

        if (properties == null) {
            throw new IllegalArgumentException(String.format(
                    "State properties were not loaded for %s block type",
                    blockTypeKey
            ));
        }

        return properties;
    }

    /**
     * Gets a {@linkplain Map map} associating {@linkplain StateProperty state property} names with
     * their default values for the specified {@linkplain BlockType block type}.
     *
     * @param blockType a holder referencing to the block type whose default state properties are desired
     * @return the map of default state properties
     * @throws IllegalArgumentException if default state properties were not loaded for the specified block type
     * @since 1.0
     */
    public static Map<String, Object> defaultProperties(Holder.Reference<BlockType> blockType) {
        Key blockTypeKey = blockType.key();
        Map<String, Object> properties = BLOCK_TYPE_TO_DEFAULT_PROPERTIES_MAP.get(blockTypeKey);

        if (properties == null) {
            throw new IllegalArgumentException(String.format(
                    "Default state properties were not loaded for %s block type",
                    blockTypeKey
            ));
        }

        return properties;
    }

    /**
     * Converts the specified {@linkplain Map map} associating {@linkplain StateProperty state property}
     * names with string representations of their values to a {@linkplain Map map} associating these
     * state property names with actual property values.
     *
     * @param blockType a holder referencing to a block type owning the block
     *                  state that the properties are being converted for
     * @param unconvertedProperties the property map to convert
     * @return the converted property map
     * @since 1.0
     */
    public static Map<String, Object> convertProperties(Holder.Reference<BlockType> blockType,
                                                        Map<String, String> unconvertedProperties) {
        Map<String, StateProperty<?>> stateProperties = BlockStateProperties.properties(blockType);
        Map<String, Object> convertedProperties = new HashMap<>();

        for (Map.Entry<String, String> entry : unconvertedProperties.entrySet()) {
            String propertyName = entry.getKey();
            String valueStringRepresentation = entry.getValue();

            StateProperty<?> stateProperty = stateProperties.get(propertyName);
            if (stateProperty == null) {
                throw new IllegalStateException(String.format(
                        "Block type \"%s\" does not have a block state property with name of \"%s\"",
                        blockType.key(), propertyName
                ));
            }

            convertedProperties.put(propertyName, stateProperty.valueFromString(valueStringRepresentation));
        }

        return Map.copyOf(convertedProperties);
    }
}