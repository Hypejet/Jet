package net.hypejet.jet.server.registry.codecs.world.block.state;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.blockstate.BlockStateProperties;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.world.block.state.property.StateProperty;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.state.BlockStateReference;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.optionalTag;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain BlockStateReference block state references}.
 *
 * @since 1.0
 * @see BlockStateReference
 * @see BinaryTagCodec
 */
@NullMarked
public final class BlockStateReferenceBinaryTagCodec implements BinaryTagCodec<BlockStateReference> {

    private static final String NAME_FIELD = "Name";
    private static final String PROPERTIES_FIELD = "Properties";

    /**
     * An instance of the {@linkplain BlockStateReferenceBinaryTagCodec block state reference binary-tag codec}.
     *
     * @since 1.0
     */
    public static final BlockStateReferenceBinaryTagCodec INSTANCE = new BlockStateReferenceBinaryTagCodec();

    private BlockStateReferenceBinaryTagCodec() {}

    @Override
    public BlockStateReference decode(BinaryTag binaryTag) {
        if (!(binaryTag instanceof CompoundBinaryTag compound)) {
            throw new IllegalArgumentException(
                    "The encoded binary tag must be of compound type to decode it into a block state"
            );
        }

        Key blockTypeKey = KeyBinaryTagCodec.INSTANCE.decode(requiredTag(NAME_FIELD, compound));
        Holder.Reference<BlockType> blockTypeReference = new Holder.Reference<>(blockTypeKey);

        CompoundBinaryTag propertiesTag = optionalTag(PROPERTIES_FIELD, compound, BinaryTagTypes.COMPOUND);
        Map<String, Object> properties = null;

        if (propertiesTag != null) {
            try {
                properties = decodeProperties(blockTypeReference, propertiesTag);
            } catch (Exception ignored) {
                // NOOP, be lenient like vanilla properties codec
            }
        }

        if (properties == null)
            properties = BlockStateProperties.defaultProperties(blockTypeReference);
        return new BlockStateReference(blockTypeReference, properties);
    }

    @Override
    public BinaryTag encode(BlockStateReference value) {
        Holder.Reference<BlockType> blockType = value.blockType();

        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        builder.put(NAME_FIELD, KeyBinaryTagCodec.INSTANCE.encode(blockType.key()));

        CompoundBinaryTag propertiesTag = encodeProperties(blockType, value.properties());
        if (propertiesTag != null)
            builder.put(PROPERTIES_FIELD, propertiesTag);

        return builder.build();
    }

    private static Map<String, Object> decodeProperties(Holder.Reference<BlockType> blockType,
                                                        CompoundBinaryTag compound) {
        Map<String, Object> properties = new HashMap<>();
        Map<String, StateProperty<?>> supportedProperties = BlockStateProperties.properties(blockType);

        for (Map.Entry<String, StateProperty<?>> propertyEntry : supportedProperties.entrySet()) {
            String propertyName = propertyEntry.getKey();
            try {
                properties.put(
                        propertyName,
                        propertyEntry.getValue()
                                .binaryTagCodec()
                                .decode(requiredTag(propertyName, compound))
                );
            } catch (Exception exception) {
                properties.put(propertyName, defaultPropertyValue(blockType, propertyName));
            }
        }

        return Map.copyOf(properties);
    }

    private static @Nullable CompoundBinaryTag encodeProperties(Holder.Reference<BlockType> blockType,
                                                                Map<String, Object> properties) {
        Map<String, StateProperty<?>> supportedProperties = BlockStateProperties.properties(blockType);
        if (supportedProperties.isEmpty()) return null;

        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        for (Map.Entry<String, StateProperty<?>> entry : supportedProperties.entrySet()) {
            String propertyName = entry.getKey();
            StateProperty<?> property = entry.getValue();
            Object value = properties.getOrDefault(propertyName, defaultPropertyValue(blockType, propertyName));
            builder.put(propertyName, encodePropertyValue(propertyName, property, value));
        }

        return builder.build();
    }

    private static <V> BinaryTag encodePropertyValue(String propertyName, StateProperty<V> property, Object value) {
        Class<V> supportedValueClass = property.valueClass();
        if (!supportedValueClass.isInstance(value)) {
            throw new IllegalArgumentException(String.format(
                    "Object %s is not an instance of %s class," +
                            " which is required for an object to be a value of \"%s\" property",
                    value, supportedValueClass.getName(), propertyName
            ));
        }
        return property.binaryTagCodec().encode(supportedValueClass.cast(value));
    }

    private static Object defaultPropertyValue(Holder.Reference<BlockType> blockType, String propertyName) {
        Map<String, Object> defaultProperties = BlockStateProperties.defaultProperties(blockType);
        Object defaultValue = defaultProperties.get(propertyName);

        if (defaultValue == null) {
            throw new IllegalArgumentException(String.format(
                    "No default value was specified for \"%s\" property",
                    propertyName
            ));
        }

        return defaultValue;
    }
}