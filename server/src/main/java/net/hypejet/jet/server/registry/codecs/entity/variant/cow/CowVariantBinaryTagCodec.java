package net.hypejet.jet.server.registry.codecs.entity.variant.cow;

import net.hypejet.jet.entity.variant.cow.CowVariant;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain CowVariant cow variants}.
 *
 * @since 1.0
 * @see CowVariant
 * @see BinaryTagCodec
 */
public final class CowVariantBinaryTagCodec implements BinaryTagCodec<CowVariant> {

    private static final String MODEL_TYPE_FIELD = "model";
    private static final String ASSET_FIELD = "asset_id";

    private static final BinaryTagCodec<CowVariant.ModelType> MODEL_TYPE_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "normal", CowVariant.ModelType.NORMAL,
                    "cold", CowVariant.ModelType.COLD,
                    "warm", CowVariant.ModelType.WARM
            )),
            StringBinaryTagCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain CowVariantBinaryTagCodec cow variant binary-tag codec}.
     *
     * @since 1.0
     */
    public static final CowVariantBinaryTagCodec INSTANCE = new CowVariantBinaryTagCodec();

    private CowVariantBinaryTagCodec() {}

    @Override
    public @NotNull CowVariant decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            BinaryTag modelTypeTag = compound.get(MODEL_TYPE_FIELD);
            return new CowVariant(
                    modelTypeTag == null ? CowVariant.ModelType.NORMAL : MODEL_TYPE_CODEC.decode(modelTypeTag),
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a cow variant"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull CowVariant decoded) throws Exception {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.asset()));

        CowVariant.ModelType modelType = decoded.modelType();
        if (modelType != CowVariant.ModelType.NORMAL) {
            builder.put(MODEL_TYPE_FIELD, MODEL_TYPE_CODEC.encode(modelType));
        }

        return builder.build();
    }
}