package net.hypejet.jet.server.registry.codecs.entity.variant.pig;

import net.hypejet.jet.entity.variant.pig.PigVariant;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain PigVariant pig variants}.
 *
 * @since 1.0
 * @see PigVariant
 * @see BinaryTagCodec
 */
@NullMarked
public final class PigVariantBinaryTagCodec implements BinaryTagCodec<PigVariant> {

    private static final String MODEL_TYPE_FIELD = "model";
    private static final String ASSET_FIELD = "asset_id";

    private static final BinaryTagCodec<PigVariant.ModelType> MODEL_TYPE_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "normal", PigVariant.ModelType.NORMAL,
                    "cold", PigVariant.ModelType.COLD
            )),
            StringBinaryTagCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain PigVariantBinaryTagCodec pig-variant binary tag codec}.
     *
     * @since 1.0
     */
    public static final PigVariantBinaryTagCodec INSTANCE = new PigVariantBinaryTagCodec();

    private PigVariantBinaryTagCodec() {}

    @Override
    public PigVariant decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            BinaryTag modelTypeTag = compound.get(MODEL_TYPE_FIELD);
            return new PigVariant(
                    modelTypeTag == null ? PigVariant.ModelType.NORMAL : MODEL_TYPE_CODEC.decode(modelTypeTag),
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a pig variant"
            );
        }
    }

    @Override
    public BinaryTag encode(PigVariant value) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset()));

        PigVariant.ModelType modelType = value.modelType();
        if (modelType != PigVariant.ModelType.NORMAL) {
            builder.put(MODEL_TYPE_FIELD, MODEL_TYPE_CODEC.encode(modelType));
        }

        return builder.build();
    }
}