package net.hypejet.jet.server.registry.codecs.number;

import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain IntegerProvider integer provider}.
 *
 * @since 1.0
 * @author Codestech
 * @see IntegerProvider
 * @see BinaryTagCodec
 */
public final class IntegerProviderBinaryTagCodec implements BinaryTagCodec<IntegerProvider> {

    private static final IntegerProviderBinaryTagCodec INSTANCE = new IntegerProviderBinaryTagCodec();

    private static final String TYPE_CONSTANT = "constant";
    private static final String TYPE_UNIFORM = "uniform";
    private static final String TYPE_BIASED_TO_BOTTOM = "biased_to_bottom";
    private static final String TYPE_CLAMPED = "clamped";
    private static final String TYPE_CLAMPED_NORMAL = "clamped_normal";
    private static final String TYPE_WEIGHTED_LIST = "weighted_list";

    private static final String TYPE_FIELD = "type";
    private static final String VALUE_FIELD = "value";
    private static final String MIN_INCLUSIVE_FIELD = "min_inclusive";
    private static final String MAX_INCLUSIVE_FIELD = "max_inclusive";
    private static final String SOURCE_FIELD = "source";
    private static final String MEAN_FIELD = "mean";
    private static final String DEVIATION_FIELD = "deviation";
    private static final String DISTRIBUTION_FIELD = "data";

    private IntegerProviderBinaryTagCodec() {}

    @Override
    public @NonNull IntegerProvider read(@NonNull BinaryTag tag) {
        if (tag instanceof IntBinaryTag intTag)
            return new IntegerProvider.ConstantInteger(intTag.value());
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified is not a compound binary tag");

        String typeName = compound.getString(TYPE_FIELD);

        return switch (typeName) {
            case TYPE_CONSTANT -> new IntegerProvider.ConstantInteger(compound.getInt(VALUE_FIELD));
            case TYPE_UNIFORM -> new IntegerProvider.Uniform(compound.getInt(MIN_INCLUSIVE_FIELD),
                    compound.getInt(MAX_INCLUSIVE_FIELD));
            case TYPE_BIASED_TO_BOTTOM -> new IntegerProvider.BiasedToBottom(compound.getInt(MIN_INCLUSIVE_FIELD),
                    compound.getInt(MAX_INCLUSIVE_FIELD));
            case TYPE_CLAMPED -> {
                BinaryTag sourceBinaryTag = compound.get(SOURCE_FIELD);
                if (sourceBinaryTag == null)
                    throw new IllegalArgumentException("The source field was not specified");
                yield new IntegerProvider.Clamped(compound.getInt(MIN_INCLUSIVE_FIELD),
                        compound.getInt(MAX_INCLUSIVE_FIELD),
                        this.read(sourceBinaryTag));
            }
            case TYPE_CLAMPED_NORMAL -> new IntegerProvider.ClampedNormal(compound.getFloat(MEAN_FIELD),
                    compound.getFloat(DEVIATION_FIELD), compound.getInt(MIN_INCLUSIVE_FIELD),
                    compound.getInt(MAX_INCLUSIVE_FIELD));
            case TYPE_WEIGHTED_LIST -> {
                ListBinaryTag distributionBinaryTag = compound.getList(DISTRIBUTION_FIELD);
                List<IntegerProvider.WeightedList.Entry> entries = new ArrayList<>();
                for (BinaryTag entryBinaryTag : distributionBinaryTag)
                    entries.add(WeightedListEntryBinaryTagCodec.instance().read(entryBinaryTag));
                yield new IntegerProvider.WeightedList(List.copyOf(entries));
            }
            default -> throw new IllegalArgumentException(String.format("Unknown integer provider: %s", typeName));
        };
    }

    @Override
    public @NonNull BinaryTag write(@NonNull IntegerProvider object) {
        if (object instanceof IntegerProvider.ConstantInteger constantInteger)
            return IntBinaryTag.intBinaryTag(constantInteger.value());

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
                    distributionBuilder.add(WeightedListEntryBinaryTagCodec.instance().write(entry));

                builder.put(DISTRIBUTION_FIELD, distributionBuilder.build());
            }
            default -> throw new IllegalArgumentException(String.format(
                    "Unknown integer provider \"%s\"",
                    object.getClass().getSimpleName()
            ));
        }

        return builder.putString(TYPE_FIELD, typeName).build();
    }

    /**
     * Gets an instance of the {@linkplain IntegerProviderBinaryTagCodec integer provider binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull IntegerProviderBinaryTagCodec instance() {
        return INSTANCE;
    }
}