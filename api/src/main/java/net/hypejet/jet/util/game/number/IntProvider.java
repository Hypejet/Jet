package net.hypejet.jet.util.game.number;

import net.hypejet.jet.util.game.random.Weighted;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a way of providing an {@code int}.
 *
 * <p>This interface is not sealed since it depends on Minecraft.
 * Adding new implementations could break switch cases for example.</p>
 *
 * @since 1.0
 */
public interface IntProvider {
    /**
     * An {@linkplain IntProvider int provider} providing a constant value.
     *
     * @param value the constant value to provide
     * @since 1.0
     */
    record Constant(int value) implements IntProvider {}

    /**
     * An {@linkplain IntProvider int provider} getting a value from the specified
     * {@linkplain IntProvider int provider}, clamping it and then providing it.
     *
     * @param source the int provider to get values from
     * @param minimum a minimum value that the clamped value can have
     * @param maximum a maximum value that the clamped value can have
     * @since 1.0
     */
    record Clamped(@NonNull IntProvider source, int minimum, int maximum) implements IntProvider {
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
     * An {@linkplain IntProvider int provider} randomly generating an {@code int} using gaussian distribution,
     * clamping the randomly generated {@code int} and providing the clamped value.
     *
     * @param mean the central value where the peak distribution should occur
     * @param deviation the "width" that the distribution should have
     * @param minimum a minimum value that the clamped value can have
     * @param maximum a maximum value that the clamped value can have
     * @since 1.0
     */
    record ClampedNormal(float mean, float deviation, int minimum, int maximum) implements IntProvider {}

    /**
     * An {@linkplain IntProvider int provider} providing a random value in the specified range.
     *
     * @param minimum a minimum value of the range
     * @param maximum a maximum value of the range
     * @since 1.0
     */
    record Uniform(int minimum, int maximum) implements IntProvider {}

    /**
     * An {@linkplain IntProvider int provider} providing a random value in the specified range with a bias
     * towards the lower end.
     *
     * @param minimum a minimum value of the range
     * @param maximum a maximum value of the range
     * @since 1.0
     */
    record BiasedToBottom(int minimum, int maximum) implements IntProvider {}

    /**
     * An {@linkplain IntProvider int provider} providing an {@code int} from a random
     * {@linkplain IntProvider int provider} from a weighted list.
     *
     * @param distribution the weighted list
     * @since 1.0
     */
    record WeightedRandom(@NonNull List<Weighted<IntProvider>> distribution) implements IntProvider {
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