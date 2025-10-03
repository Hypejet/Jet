package net.hypejet.jet.data.json.model.block.state.property;

import net.hypejet.jet.data.json.model.block.state.JsonBlockState;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.Set;

/**
 * A property of a {@linkplain JsonBlockState block state}.
 *
 * @since 1.0
 * @see JsonBlockState
 */
@NullMarked
public sealed interface JsonStateProperty {
    /**
     * A {@linkplain JsonStateProperty state property} accepting {@code boolean} values.
     *
     * @since 1.0
     * @see JsonStateProperty
     */
    final class Boolean implements JsonStateProperty {
        /**
         * An instance of the {@linkplain Boolean boolean state property}.
         *
         * @since 1.0
         */
        public static final Boolean INSTANCE = new Boolean();

        private Boolean() {}

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Boolean;
        }

        @Override
        public int hashCode() {
            return Objects.hash();
        }

        @Override
        public String toString() {
            return "Boolean{}";
        }
    }

    /**
     * A {@linkplain JsonStateProperty state property} accepting {@code int} values.
     *
     * @param min the minimum int value that is accepted by the integer state property, inclusive
     * @param max the maximum int value that is accepted by the integer state property, inclusive
     * @since 1.0
     * @see JsonStateProperty
     */
    record Integer(int min, int max) implements JsonStateProperty {}

    /**
     * A {@linkplain JsonStateProperty state property} accepting enum instance values.
     *
     * @param valueType the type of enum whose instances are accepted by this state property
     * @param acceptedValues set of string representations of exact enum instances accepted by this state property
     * @since 1.0
     * @see JsonStateProperty
     */
    record Enum(JsonEnumStatePropertyValueType valueType, Set<String> acceptedValues) implements JsonStateProperty {
        /**
         * Constructs the {@linkplain Enum enum state property}.
         *
         * @param valueType the type of enum whose instances should be accepted by the state property
         * @param acceptedValues set of string representations of exact enum instances
         *                       that should be accepted by the state property
         * @since 1.0
         */
        public Enum {
            Objects.requireNonNull(valueType, "value type");
            acceptedValues = Set.copyOf(Objects.requireNonNull(acceptedValues, "accepted values"));
        }
    }
}