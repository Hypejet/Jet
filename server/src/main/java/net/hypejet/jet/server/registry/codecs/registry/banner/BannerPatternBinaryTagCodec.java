package net.hypejet.jet.server.registry.codecs.registry.banner;

import net.hypejet.jet.data.model.registry.registries.banner.BannerPattern;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain BannerPattern banner pattern}.
 *
 * @since 1.0
 * @author Codestech
 * @see BannerPattern
 * @see BinaryTagCodec
 */
public final class BannerPatternBinaryTagCodec implements BinaryTagCodec<BannerPattern> {

    private static final BannerPatternBinaryTagCodec INSTANCE = new BannerPatternBinaryTagCodec();

    private static final String ASSET_ID_FIELD = "asset_id";
    private static final String TRANSLATION_KEY_FIELD = "translation_key";

    private BannerPatternBinaryTagCodec() {}

    @Override
    public @NonNull BannerPattern read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified is not a compound binary tag");

        Key assetId = BinaryTagUtil.readOptional(ASSET_ID_FIELD, compound, PackedIdentifierBinaryTagCodec.instance());
        if (assetId == null)
            throw new IllegalArgumentException("The asset id field was not specified");

        return new BannerPattern(assetId, compound.getString(TRANSLATION_KEY_FIELD));
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BannerPattern object) {
        return CompoundBinaryTag.builder()
                .put(ASSET_ID_FIELD, PackedIdentifierBinaryTagCodec.instance().write(object.asset()))
                .putString(TRANSLATION_KEY_FIELD, object.translationKey())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BannerPatternBinaryTagCodec banner pattern binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BannerPatternBinaryTagCodec instance() {
        return INSTANCE;
    }
}