package net.hypejet.jet.world.block.banner;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A pattern of Minecraft banner block.
 *
 * @param asset the key of the texture representing the pattern
 * @param translationKey the translation key representing the pattern, shown in tooltip of items
 * @since 1.0
 */
public record BannerPattern(@NonNull Key asset, @NonNull String translationKey) {
    /**
     * Constructs the {@linkplain BannerPattern banner pattern}.
     *
     * @param asset the key of the texture representing the pattern
     * @param translationKey the translation key representing the pattern, shown in tooltip of items
     * @since 1.0
     */
    public BannerPattern {
        Objects.requireNonNull(asset, "asset");
        Objects.requireNonNull(translationKey, "translation key");
    }
}