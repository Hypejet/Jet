package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.JsonIntProvider;
import net.hypejet.jet.data.json.model.JsonWeighted;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents something converting {@linkplain IntProvider int providers} to a Jet data equivalent.
 *
 * @since 1.0
 * @see IntProvider
 */
public final class IntProviderAdapter {

    private static final Field CLAMPED_SOURCE_FIELD;
    private static final Field CLAMPED_NORMAL_MEAN_FIELD;
    private static final Field CLAMPED_NORMAL_DEVIATION_FIELD;
    private static final Field WEIGHTED_LIST_DISTRIBUTION_FIELD;

    static {
        try {
            CLAMPED_SOURCE_FIELD = ClampedInt.class.getDeclaredField("source");
            CLAMPED_NORMAL_MEAN_FIELD = ClampedNormalInt.class.getDeclaredField("mean");
            CLAMPED_NORMAL_DEVIATION_FIELD = ClampedNormalInt.class.getDeclaredField("deviation");
            WEIGHTED_LIST_DISTRIBUTION_FIELD = WeightedListInt.class.getDeclaredField("distribution");
            CLAMPED_SOURCE_FIELD.setAccessible(true);
            CLAMPED_NORMAL_MEAN_FIELD.setAccessible(true);
            CLAMPED_NORMAL_DEVIATION_FIELD.setAccessible(true);
            WEIGHTED_LIST_DISTRIBUTION_FIELD.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    private IntProviderAdapter() {}

    /**
     * Converts the specified {@linkplain IntProvider int provider} to a Jet data equivalent.
     *
     * @param provider the int provider to convert
     * @return the converted int provider
     * @since 1.0
     */
    public static @NonNull JsonIntProvider convert(@NonNull IntProvider provider) {
        return switch (provider) {
            case ConstantInt constant -> new JsonIntProvider.Constant(constant.getValue());
            case ClampedInt clamped -> {
                try {
                    IntProvider source = (IntProvider) CLAMPED_SOURCE_FIELD.get(clamped);
                    yield new JsonIntProvider.Clamped(convert(source), clamped.getMinValue(), clamped.getMaxValue());
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            }
            case ClampedNormalInt clamped -> {
                try {
                    float mean = CLAMPED_NORMAL_MEAN_FIELD.getFloat(clamped);
                    float deviation = CLAMPED_NORMAL_DEVIATION_FIELD.getFloat(clamped);
                    int minValue = clamped.getMinValue();
                    int maxValue = clamped.getMaxValue();
                    yield new JsonIntProvider.ClampedNormal(mean, deviation, minValue, maxValue);
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            }
            case UniformInt uniform -> new JsonIntProvider.Uniform(uniform.getMinValue(), uniform.getMaxValue());
            case BiasedToBottomInt biasedToBottom ->
                    new JsonIntProvider.BiasedToBottom(biasedToBottom.getMinValue(), biasedToBottom.getMaxValue());
            case WeightedListInt list -> {
                try {
                    WeightedList<?> distribution = (WeightedList<?>) WEIGHTED_LIST_DISTRIBUTION_FIELD.get(list);
                    List<JsonWeighted<JsonIntProvider>> convertedDistribution = new ArrayList<>();

                    for (Weighted<?> weighted : distribution.unwrap()) {
                        IntProvider value = (IntProvider) weighted.value();
                        convertedDistribution.add(new JsonWeighted<>(convert(value), weighted.weight()));
                    }

                    yield new JsonIntProvider.WeightedRandom(convertedDistribution);
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            }
            default ->
                    throw new IllegalArgumentException("Unknown int provider: " + provider.getClass().getSimpleName());
        };
    }
}