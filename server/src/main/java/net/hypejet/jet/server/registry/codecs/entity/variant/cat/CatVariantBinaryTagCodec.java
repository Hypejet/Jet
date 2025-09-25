package net.hypejet.jet.server.registry.codecs.entity.variant.cat;

import net.hypejet.jet.entity.variant.cat.CatVariant;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain CatVariant cat variants}.
 *
 * @since 1.0
 * @see CatVariant
 * @see BinaryTagCodec
 */
@NullMarked
public final class CatVariantBinaryTagCodec implements BinaryTagCodec<CatVariant> {

    private static final String ASSET_FIELD = "asset_id";

    /**
     * An instance of the {@linkplain CatVariantBinaryTagCodec cat variant binary-tag codec}.
     *
     * @since 1.0
     */
    public static final CatVariantBinaryTagCodec INSTANCE = new CatVariantBinaryTagCodec();

    private CatVariantBinaryTagCodec() {}

    @Override
    public CatVariant decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new CatVariant(KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound)));
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a cat variant"
            );
        }
    }

    @Override
    public BinaryTag encode(CatVariant value) {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset()))
                .build();
    }
}