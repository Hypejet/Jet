package net.hypejet.jet.server.registry.codecs.world.block.banner;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.world.block.banner.BannerPattern;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain BannerPattern banner patterns}.
 *
 * @since 1.0
 * @see BannerPattern
 * @see BinaryTagCodec
 */
public final class BannerPatternBinaryTagCodec implements BinaryTagCodec<BannerPattern> {

    private static final String ASSET_FIELD = "asset_id";
    private static final String TRANSLATION_KEY_FIELD = "translation_key";

    /**
     * An instance of the {@linkplain BannerPatternBinaryTagCodec banner pattern binary-tag codec}.
     *
     * @since 1.0
     */
    public static final BannerPatternBinaryTagCodec INSTANCE = new BannerPatternBinaryTagCodec();

    private BannerPatternBinaryTagCodec() {}

    @Override
    public @NotNull BannerPattern decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            return new BannerPattern(
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound)),
                    requiredTag(TRANSLATION_KEY_FIELD, compound, BinaryTagTypes.STRING).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to a banner pattern"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull BannerPattern decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.asset()))
                .putString(TRANSLATION_KEY_FIELD, decoded.translationKey())
                .build();
    }
}