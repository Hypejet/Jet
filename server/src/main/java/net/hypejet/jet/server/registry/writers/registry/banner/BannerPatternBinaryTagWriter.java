package net.hypejet.jet.server.registry.writers.registry.banner;

import net.hypejet.jet.data.model.api.registries.banner.BannerPattern;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BannerPattern a banner pattern}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see BannerPattern
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BannerPatternBinaryTagWriter implements Writer<BannerPattern, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BannerPatternBinaryTagWriter banner pattern binary tag writer}.
     *
     * @since 1.0
     */
    public static final BannerPatternBinaryTagWriter INSTANCE = new BannerPatternBinaryTagWriter();

    private static final String ASSET_ID_FIELD = "asset_id";
    private static final String TRANSLATION_KEY_FIELD = "translation_key";

    private BannerPatternBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull BannerPattern object) {
        return CompoundBinaryTag.builder()
                .put(ASSET_ID_FIELD, PackedKeyBinaryTagWriter.INSTANCE.write(object.asset()))
                .putString(TRANSLATION_KEY_FIELD, object.translationKey())
                .build();
    }
}