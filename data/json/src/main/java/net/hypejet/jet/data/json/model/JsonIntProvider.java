package net.hypejet.jet.data.json.model;

import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a way of providing an {@code int}.
 *
 * @since 1.0
 */
public sealed interface JsonIntProvider {
    /**
     * An {@linkplain JsonIntProvider int provider} providing a constant value.
     *
     * @param value the constant value to provide
     * @since 1.0
     */
    record Constant(int value) implements JsonIntProvider {}

    /**
     * An {@linkplain JsonIntProvider int provider} getting a value from the specified
     * {@linkplain JsonIntProvider int provider}, clamping it and then providing it.
     *
     * @param source the int provider to get values from
     * @param minimum a minimum value that the clamped value can have
     * @param maximum a maximum value that the clamped value can have
     * @since 1.0
     */
    record Clamped(@NonNull JsonIntProvider source, int minimum, int maximum) implements JsonIntProvider {
        /**
         * Constructs the {@linkplain Clamped clamped int provider}.
         *
         * @param source the int provider to get values from
         * @param minimum a minimum value that the clamped value can have
         * @param maximum a maximum value that the clamped value can have
         * @since 1.0
         */
        public Clamped {
            Objects.requireNonNull(source, "source");
        }
    }

    /**
     * An {@linkplain JsonIntProvider int provider} randomly generating an {@code int} using gaussian distribution,
     * clamping the randomly generated {@code int} and providing the clamped value.
     *
     * @param mean the central value where the peak distribution should occur
     * @param deviation the "width" that the distribution should have
     * @param minimum a minimum value that the clamped value can have
     * @param maximum a maximum value that the clamped value can have
     * @since 1.0
     */
    record ClampedNormal(float mean, float deviation, int minimum, int maximum) implements JsonIntProvider {}

    /**
     * An {@linkplain JsonIntProvider int provider} providing a random value in the specified range.
     *
     * @param minimum a minimum value of the range
     * @param maximum a maximum value of the range
     * @since 1.0
     */
    record Uniform(int minimum, int maximum) implements JsonIntProvider {}

    /**
     * An {@linkplain JsonIntProvider int provider} providing a random value in the specified range with a bias
     * towards the lower end.
     *
     * @param minimum a minimum value of the range
     * @param maximum a maximum value of the range
     * @since 1.0
     */
    record BiasedToBottom(int minimum, int maximum) implements JsonIntProvider {}

    /**
     * An {@linkplain JsonIntProvider int provider} providing an {@code int} from a random
     * {@linkplain JsonIntProvider int provider} from a weighted list.
     *
     * @param distribution the weighted list
     * @since 1.0
     */
    record WeightedRandom(@NonNull List<JsonWeighted<JsonIntProvider>> distribution) implements JsonIntProvider {
        /**
         * Constructs the {@linkplain WeightedRandom weighted random int provider}.
         *
         * @param distribution the weighted list
         * @since 1.0
         */
        public WeightedRandom {
            distribution = List.copyOf(Objects.requireNonNull(distribution, "distribution"));
            if (distribution.isEmpty())
                throw new IllegalArgumentException("The weighted list must not be empty");
        }
    }
}