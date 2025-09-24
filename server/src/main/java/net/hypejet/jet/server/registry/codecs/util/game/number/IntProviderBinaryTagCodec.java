package net.hypejet.jet.server.registry.codecs.util.game.number;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.ListBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.util.game.random.WeightedBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.util.game.number.IntProvider;
import net.hypejet.jet.util.game.random.Weighted;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain IntProvider int providers}.
 *
 * @since 1.0
 * @see IntProvider
 * @see BinaryTagCodec
 */
@NullMarked
public final class IntProviderBinaryTagCodec implements BinaryTagCodec<IntProvider> {

    /**
     * An instance of the {@linkplain IntProviderBinaryTagCodec int provider binary-tag codec}.
     *
     * @since 1.0
     */
    /* We put this constant before private constants because the distribution
       codec needs this constant to be initialized first. */
    public static final IntProviderBinaryTagCodec INSTANCE = new IntProviderBinaryTagCodec();

    private static final String TYPE_FIELD = "type";
    private static final String VALUE_FIELD = "value";
    private static final String MINIMUM_FIELD = "min_inclusive";
    private static final String MAXIMUM_FIELD = "max_inclusive";
    private static final String SOURCE_FIELD = "source";
    private static final String DISTRIBUTION_FIELD = "distribution";
    private static final String MEAN_FIELD = "mean";
    private static final String DEVIATION_FIELD = "deviation";

    private static final Index<Key, Class<? extends IntProvider>> KEY_INDEX = IndexUtil.fromMap(Map.of(
            IntProvider.Constant.class, Key.key("constant"),
            IntProvider.Uniform.class, Key.key("uniform"),
            IntProvider.BiasedToBottom.class, Key.key("biased_to_bottom"),
            IntProvider.Clamped.class, Key.key("clamped"),
            IntProvider.WeightedRandom.class, Key.key("weighted_list"),
            IntProvider.ClampedNormal.class, Key.key("clamped_normal")
    ));

    private static final BinaryTagCodec<List<Weighted<IntProvider>>> DISTRIBUTION_CODEC = new ListBinaryTagCodec<>(
            new WeightedBinaryTagCodec<>(INSTANCE)
    );

    private IntProviderBinaryTagCodec() {}

    @Override
    public IntProvider decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof IntBinaryTag tag)
            return new IntProvider.Constant(tag.value());

        if (!(binaryTag instanceof CompoundBinaryTag compound)) {
            throw new IllegalArgumentException(
                    "The encoded tag must be of either int or compound type to decode it to an int provider"
            );
        }

        Key typeKey = KeyBinaryTagCodec.INSTANCE.decode(requiredTag(TYPE_FIELD, compound), server);
        Class<? extends IntProvider> providerClass = KEY_INDEX.valueOrThrow(typeKey);

        if (IntProvider.Constant.class.isAssignableFrom(providerClass)) {
            return new IntProvider.Constant(requiredTag(VALUE_FIELD, compound, BinaryTagTypes.INT).value());
        } else if (IntProvider.Uniform.class.isAssignableFrom(providerClass)) {
            return new IntProvider.Uniform(
                    requiredTag(MINIMUM_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(MAXIMUM_FIELD, compound, BinaryTagTypes.INT).value()
            );
        } else if (IntProvider.BiasedToBottom.class.isAssignableFrom(providerClass)) {
            return new IntProvider.BiasedToBottom(
                    requiredTag(MINIMUM_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(MAXIMUM_FIELD, compound, BinaryTagTypes.INT).value()
            );
        } else if (IntProvider.Clamped.class.isAssignableFrom(providerClass)) {
            return new IntProvider.Clamped(
                    INSTANCE.decode(requiredTag(SOURCE_FIELD, compound), server),
                    requiredTag(MINIMUM_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(MAXIMUM_FIELD, compound, BinaryTagTypes.INT).value()
            );
        } else if (IntProvider.WeightedRandom.class.isAssignableFrom(providerClass)) {
            return new IntProvider.WeightedRandom(
                    DISTRIBUTION_CODEC.decode(requiredTag(DISTRIBUTION_FIELD, compound), server)
            );
        } else if (IntProvider.ClampedNormal.class.isAssignableFrom(providerClass)) {
            return new IntProvider.ClampedNormal(
                    requiredTag(MEAN_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    requiredTag(DEVIATION_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    requiredTag(MINIMUM_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(MAXIMUM_FIELD, compound, BinaryTagTypes.INT).value()
            );
        } else {
            throw unknownProviderClass(providerClass);
        }
    }

    @Override
    public BinaryTag encode(IntProvider value, JetMinecraftServer server) {
        if (value instanceof IntProvider.Constant(int constantValue))
            return IntBinaryTag.intBinaryTag(constantValue);

        Class<? extends IntProvider> providerClass = value.getClass();
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        builder.put(TYPE_FIELD, KeyBinaryTagCodec.INSTANCE.encode(KEY_INDEX.keyOrThrow(providerClass), server));

        switch (value) {
            case IntProvider.Uniform(int minimum, int maximum) -> {
                builder.putInt(MINIMUM_FIELD, minimum);
                builder.putInt(MAXIMUM_FIELD, maximum);
            }
            case IntProvider.BiasedToBottom(int minimum, int maximum) -> {
                builder.putInt(MINIMUM_FIELD, minimum);
                builder.putInt(MAXIMUM_FIELD, maximum);
            }
            case IntProvider.Clamped(IntProvider source, int minimum, int maximum) -> {
                builder.put(SOURCE_FIELD, INSTANCE.encode(source, server));
                builder.putInt(MINIMUM_FIELD, minimum);
                builder.putInt(MAXIMUM_FIELD, maximum);
            }
            case IntProvider.ClampedNormal(float mean, float deviation, int minimum, int maximum) -> {
                builder.putFloat(MEAN_FIELD, mean);
                builder.putFloat(DEVIATION_FIELD, deviation);
                builder.putInt(MINIMUM_FIELD, minimum);
                builder.putInt(MAXIMUM_FIELD, maximum);
            }
            case IntProvider.WeightedRandom(List<Weighted<IntProvider>> distribution) ->
                    builder.put(DISTRIBUTION_FIELD, DISTRIBUTION_CODEC.encode(distribution, server));
            default -> throw unknownProviderClass(providerClass);
        }

        return builder.build();
    }

    private static IllegalArgumentException unknownProviderClass(Class<?> providerClass) {
        return new IllegalArgumentException("Unknown int provider class: " + providerClass.getSimpleName());
    }
}