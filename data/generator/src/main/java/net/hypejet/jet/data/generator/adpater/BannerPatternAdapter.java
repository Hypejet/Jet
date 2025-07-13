package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.pattern.banner.JsonBannerPattern;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain BannerPattern banner patterns} to a Jet data equivalent.
 *
 * @since 1.0
 * @see BannerPattern
 */
public final class BannerPatternAdapter {

    private BannerPatternAdapter() {}

    /**
     * Converts the specified {@linkplain BannerPattern banner pattern} to a Jet data equivalent.
     *
     * @param pattern the banner pattern to convert
     * @return the converted banner pattern
     * @since 1.0
     */
    public static @NonNull JsonBannerPattern convert(@NonNull BannerPattern pattern) {
        return new JsonBannerPattern(KeyAdapter.convert(pattern.assetId()), pattern.translationKey());
    }
}