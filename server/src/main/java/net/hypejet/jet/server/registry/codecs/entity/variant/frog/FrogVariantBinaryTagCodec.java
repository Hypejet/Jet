package net.hypejet.jet.server.registry.codecs.entity.variant.frog;

import net.hypejet.jet.entity.variant.frog.FrogVariant;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain FrogVariant frog variants}.
 *
 * @since 1.0
 * @see FrogVariant
 * @see BinaryTagCodec
 */
@NullMarked
public final class FrogVariantBinaryTagCodec implements BinaryTagCodec<FrogVariant> {

    private static final String ASSET_FIELD = "asset_id";

    /**
     * An instance of the {@linkplain FrogVariantBinaryTagCodec frog-variant binary tag codec}.
     *
     * @since 1.0
     */
    public static final FrogVariantBinaryTagCodec INSTANCE = new FrogVariantBinaryTagCodec();

    private FrogVariantBinaryTagCodec() {}

    @Override
    public FrogVariant decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new FrogVariant(KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound), server));
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a frog variant"
            );
        }
    }

    @Override
    public BinaryTag encode(FrogVariant value, JetMinecraftServer server) {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset(), server))
                .build();
    }
}