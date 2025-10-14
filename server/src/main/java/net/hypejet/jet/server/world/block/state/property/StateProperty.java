package net.hypejet.jet.server.world.block.state.property;

import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.server.world.block.state.property.enums.EnumPropertyValueEquivalents;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A property of a {@linkplain JetBlockState block state}.
 *
 * @param <V> the type of values that this property accepts
 * @since 1.0
 * @see JetBlockState
 */
@NullMarked
public sealed abstract class StateProperty<V> permits BooleanStateProperty, EnumLikeStateProperty, IntegerStateProperty {

    private final Class<V> valueClass;
    private final BinaryTagCodec<V> binaryTagCodec = new ValueBinaryTagCodec();

    /**
     * Constructs the {@linkplain StateProperty state property}.
     *
     * @param valueClass the class of values that the state property should accept
     * @since 1.0
     */
    public StateProperty(Class<V> valueClass) {
        this.valueClass = Objects.requireNonNull(valueClass, "value class");
    }

    /**
     * Gets the {@linkplain Class class} of values that this {@linkplain StateProperty state property} accepts.
     *
     * @return the value class
     * @since 1.0
     */
    public final Class<V> valueClass() {
        return this.valueClass;
    }

    /**
     * Gets a {@linkplain BinaryTagCodec binary-tag codec} writing
     * values accepted by this {@linkplain StateProperty state property}.
     *
     * @return the binary-tag codec
     * @since 1.0
     */
    public final BinaryTagCodec<V> binaryTagCodec() {
        return this.binaryTagCodec;
    }

    /**
     * Gets a string representation of the specified value.
     *
     * @param value the value that the string representation should be created for
     * @return the string representation
     * @throws IllegalArgumentException if the specified value is not accepted by this state property
     * @since 1.0
     */
    public abstract String valueToString(V value);

    /**
     * Finds a value with the specified string representation.
     *
     * @param valueString the string representation
     * @return the found value
     * @throws IllegalArgumentException if no value (accepted by this state property)
     *                                  was found for the specified string representation
     * @since 1.0
     */
    public abstract V valueFromString(String valueString);

    /**
     * Converts the specified {@linkplain JsonStateProperty Jet data state property} to a Jet equivalent.
     *
     * @param property the state property to convert
     * @return the converted state property
     * @since 1.0
     */
    public static StateProperty<?> convert(JsonStateProperty property) {
        return switch (property) {
            case JsonStateProperty.Boolean ignored -> BooleanStateProperty.INSTANCE;
            case JsonStateProperty.Integer castProperty -> new IntegerStateProperty(
                    castProperty.min(),
                    castProperty.max()
            );
            case JsonStateProperty.Enum castProperty -> convertEnumProperty(
                    castProperty.acceptedValues(),
                    EnumPropertyValueEquivalents.equivalentClass(castProperty.valueType())
            );
        };
    }

    private static <V> EnumLikeStateProperty<V> convertEnumProperty(Set<String> acceptedValueStrings,
                                                                    Class<V> equivalentClass) {
        Map<String, V> stringRepresentations = EnumPropertyValueEquivalents.stringRepresentations(equivalentClass);
        Map<V, String> acceptedValues = new HashMap<>();

        for (String acceptedValueString : acceptedValueStrings) {
            V acceptedValue = stringRepresentations.get(acceptedValueString);
            if (acceptedValue == null) {
                throw new IllegalArgumentException(String.format(
                        "Could not find a value with string representation of \"%s\"",
                        acceptedValueString
                ));
            }
            acceptedValues.put(acceptedValue, acceptedValueString);
        }

        return new EnumLikeStateProperty<>(equivalentClass, acceptedValues);
    }

    /**
     * A {@linkplain BinaryTagCodec binary-tag codec} of values
     * accepted by this {@linkplain StateProperty state property}.
     *
     * @since 1.0
     * @see StateProperty
     * @see BinaryTagCodec
     */
    private final class ValueBinaryTagCodec implements BinaryTagCodec<V> {
        @Override
        public V decode(BinaryTag binaryTag) {
            if (!(binaryTag instanceof StringBinaryTag tag)) {
                throw new IllegalArgumentException(
                        "The encoded tag must be of string type to decode it into a state property value"
                );
            }
            return StateProperty.this.valueFromString(tag.value());
        }

        @Override
        public BinaryTag encode(V value) {
            return StringBinaryTag.stringBinaryTag(StateProperty.this.valueToString(value));
        }
    }
}