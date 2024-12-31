package net.hypejet.jet.server.registry.writers.number;

import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain IntegerProvider an integer provider} into
 * {@linkplain BinaryTag a binary tag}.
 *
 * @since 1.0
 * @see IntegerProvider
 * @see BinaryTag
 * @see Writer
 */
public final class IntegerProviderBinaryTagWriter implements Writer<IntegerProvider, BinaryTag> {
    /**
     * An instance of the {@linkplain IntegerProviderBinaryTagWriter integer provider binary tag writer}.
     *
     * @since 1.0
     */
    public static final IntegerProviderBinaryTagWriter INSTANCE = new IntegerProviderBinaryTagWriter();

    private static final String TYPE_UNIFORM = "uniform";
    private static final String TYPE_BIASED_TO_BOTTOM = "biased_to_bottom";
    private static final String TYPE_CLAMPED = "clamped";
    private static final String TYPE_CLAMPED_NORMAL = "clamped_normal";
    private static final String TYPE_WEIGHTED_LIST = "weighted_list";

    private static final String TYPE_FIELD = "type";
    private static final String MIN_INCLUSIVE_FIELD = "min_inclusive";
    private static final String MAX_INCLUSIVE_FIELD = "max_inclusive";
    private static final String SOURCE_FIELD = "source";
    private static final String MEAN_FIELD = "mean";
    private static final String DEVIATION_FIELD = "deviation";
    private static final String DISTRIBUTION_FIELD = "data";

    private IntegerProviderBinaryTagWriter() {}

    @Override
    public @NonNull BinaryTag write(@NonNull IntegerProvider object) {
        if (object instanceof IntegerProvider.ConstantInteger(int value))
            return IntBinaryTag.intBinaryTag(value);

        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        String typeName;

        switch (object) {
            case IntegerProvider.BiasedToBottom provider -> {
                typeName = TYPE_BIASED_TO_BOTTOM;
                builder.putInt(MIN_INCLUSIVE_FIELD, provider.minimum());
                builder.putInt(MAX_INCLUSIVE_FIELD, provider.maximum());
            }
            case IntegerProvider.Clamped provider -> {
                typeName = TYPE_CLAMPED;
                builder.putInt(MIN_INCLUSIVE_FIELD, provider.minimum());
                builder.putInt(MAX_INCLUSIVE_FIELD, provider.maximum());
                builder.put(SOURCE_FIELD, this.write(provider.source()));
            }
            case IntegerProvider.ClampedNormal provider -> {
                typeName = TYPE_CLAMPED_NORMAL;
                builder.putFloat(MEAN_FIELD, provider.mean());
                builder.putFloat(DEVIATION_FIELD, provider.deviation());
                builder.putInt(MIN_INCLUSIVE_FIELD, provider.minimum());
                builder.putInt(MAX_INCLUSIVE_FIELD, provider.maximum());
            }
            case IntegerProvider.Uniform provider -> {
                typeName = TYPE_UNIFORM;
                builder.putInt(MIN_INCLUSIVE_FIELD, provider.minimum());
                builder.putInt(MAX_INCLUSIVE_FIELD, provider.maximum());
            }
            case IntegerProvider.WeightedList provider -> {
                typeName = TYPE_WEIGHTED_LIST;

                ListBinaryTag.Builder<BinaryTag> distributionBuilder = ListBinaryTag.builder();
                for (IntegerProvider.WeightedList.Entry entry : provider.entries())
                    distributionBuilder.add(WeightedListEntryBinaryTagWriter.INSTANCE.write(entry));

                builder.put(DISTRIBUTION_FIELD, distributionBuilder.build());
            }
            default -> throw new IllegalArgumentException(String.format(
                    "Unknown integer provider \"%s\"",
                    object.getClass().getSimpleName()
            ));
        }

        return builder.putString(TYPE_FIELD, typeName).build();
    }
}