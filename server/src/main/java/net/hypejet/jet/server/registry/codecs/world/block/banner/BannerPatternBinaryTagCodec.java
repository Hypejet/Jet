package net.hypejet.jet.server.registry.codecs.world.block.banner;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.world.block.banner.BannerPattern;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain BannerPattern banner patterns}.
 *
 * @since 1.0
 * @see BannerPattern
 * @see BinaryTagCodec
 */
@NullMarked
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
    public BannerPattern decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new BannerPattern(
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound), server),
                    requiredTag(TRANSLATION_KEY_FIELD, compound, BinaryTagTypes.STRING).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to a banner pattern"
            );
        }
    }

    @Override
    public BinaryTag encode(BannerPattern value, JetMinecraftServer server) {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset(), server))
                .putString(TRANSLATION_KEY_FIELD, value.translationKey())
                .build();
    }
}