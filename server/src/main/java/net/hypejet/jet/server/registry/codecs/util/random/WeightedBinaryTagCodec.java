package net.hypejet.jet.server.registry.codecs.util.random;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.util.game.random.Weighted;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Weighted weighted}.
 *
 * @param <V> the value type of weighted whose serialization is handled by this binary tag codec
 * @since 1.0
 */
public final class WeightedBinaryTagCodec<V> implements BinaryTagCodec<Weighted<V>> {

    private static final String WEIGHT_FIELD = "weight";
    private static final String DATA_FIELD = "data";

    private final BinaryTagCodec<V> valueCodec;

    /**
     * Constructs the {@linkplain WeightedBinaryTagCodec weighted binary tag codec}.
     *
     * @param valueCodec a binary tag codec that should handle serialization of weighted values
     * @since 1.0
     */
    public WeightedBinaryTagCodec(@NonNull BinaryTagCodec<V> valueCodec) {
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
    }

    @Override
    public @NotNull Weighted<V> decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new Weighted<>(
                    this.valueCodec.decode(requiredTag(DATA_FIELD, compound)),
                    requiredTag(WEIGHT_FIELD, compound, BinaryTagTypes.INT).value()
            );
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a weighted");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Weighted<V> decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(DATA_FIELD, this.valueCodec.encode(decoded.value()))
                .putInt(WEIGHT_FIELD, decoded.weight())
                .build();
    }
}