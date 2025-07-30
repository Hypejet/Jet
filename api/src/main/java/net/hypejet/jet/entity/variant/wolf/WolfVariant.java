package net.hypejet.jet.entity.variant.wolf;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft wolf entity.
 *
 * @param assetInfo a set of textures that entities of this variant should use
 * @since 1.0
 */
public record WolfVariant(@NonNull AssetInfo assetInfo) {
    /**
     * Constructs the {@linkplain WolfVariant wolf variant}.
     *
     * @param assetInfo a set of textures that entities of this variant should use
     * @since 1.0
     */
    public WolfVariant {
        Objects.requireNonNull(assetInfo, "asset info");
    }

    /**
     * A set of textures to use for a wolf entity.
     *
     * @param wildAsset the key of the texture to use when the wolf is wild
     * @param tameAsset the key of the texture to use when the wolf is tamed
     * @param angryAsset the key of the texture to use when the wolf is angry
     * @since 1.0
     */
    public record AssetInfo(@NonNull Key wildAsset, @NonNull Key tameAsset, @NonNull Key angryAsset) {
        /**
         * Constructs the {@linkplain AssetInfo asset info}.
         *
         * @param wildAsset the key of the texture to use when the wolf is wild
         * @param tameAsset the key of the texture to use when the wolf is tamed
         * @param angryAsset the key of the texture to use when the wolf is angry
         * @since 1.0
         */
        public AssetInfo {
            Objects.requireNonNull(wildAsset, "wild asset");
            Objects.requireNonNull(tameAsset, "tame asset");
            Objects.requireNonNull(angryAsset, "angry asset");
        }
    }
}