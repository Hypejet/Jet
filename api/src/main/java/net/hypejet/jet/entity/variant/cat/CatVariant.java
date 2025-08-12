package net.hypejet.jet.entity.variant.cat;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft cat entity.
 *
 * @param asset the key of the texture that cats with this variant should have
 * @since 1.0
 */
public record CatVariant(@NonNull Key asset) {
    /**
     * Constructs the {@linkplain CatVariant cat variant}.
     *
     * @param asset the key of the texture that cats with this variant should have
     * @since 1.0
     */
    public CatVariant {
        Objects.requireNonNull(asset, "asset");
    }
}