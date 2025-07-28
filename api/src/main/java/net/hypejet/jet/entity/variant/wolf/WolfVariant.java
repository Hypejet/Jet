package net.hypejet.jet.entity.variant.wolf;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft wolf entity.
 *
 * @param wildAsset the key of the texture to use when the wolf with of variant is wild
 * @param tameAsset the key of the texture to use when the wolf with of variant is tamed
 * @param angryAsset the key of the texture to use when the wolf with of variant is angry
 * @since 1.0
 */
public record WolfVariant(@NonNull Key wildAsset, @NonNull Key tameAsset, @NonNull Key angryAsset) {
    /**
     * Constructs the {@linkplain WolfVariant wolf variant}.
     *
     * @param wildAsset the key of the texture to use when the wolf with of variant is wild
     * @param tameAsset the key of the texture to use when the wolf with of variant is tamed
     * @param angryAsset the key of the texture to use when the wolf with of variant is angry
     * @since 1.0
     */
    public WolfVariant {
        Objects.requireNonNull(wildAsset, "wild asset");
        Objects.requireNonNull(tameAsset, "tame asset");
        Objects.requireNonNull(angryAsset, "angry asset");
    }
}