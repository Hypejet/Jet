package net.hypejet.jet.inventory.item.trim;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a group of assets to be used on a {@linkplain TrimMaterial trim material}.
 *
 * @param baseAsset the trim color model to be rendered on top of the armor
 * @param overrides assets overriding the base asset depending on types of armor
 * @since 1.0
 * @see TrimMaterial
 */
public record MaterialAssetGroup(@NonNull Asset baseAsset, @NonNull Map<Key, Asset> overrides) {
    /**
     * Constructs the {@linkplain MaterialAssetGroup material asset group}.
     *
     * @param baseAsset the trim color model to be rendered on top of the armor
     * @param overrides assets overriding the base asset depending on types of armor
     * @since 1.0
     */
    public MaterialAssetGroup {
        Objects.requireNonNull(baseAsset, "base asset");
        overrides = Map.copyOf(Objects.requireNonNull(overrides, "overrides"));
    }

    /**
     * A texture asset used to render parts of an armor with a {@linkplain TrimMaterial trim material}.
     *
     * @param value a path of the asset in {@code trim/color_palettes} namespace
     * @since 1.0
     * @see TrimMaterial
     */
    public record Asset(@KeyPattern.Value @NonNull String value) {
        /**
         * Constructs the {@linkplain Asset asset}.
         *
         * @param value a path of the asset in {@code trim/color_palettes} namespace
         * @since 1.0
         */
        public Asset {
            Objects.requireNonNull(value, "value");
            Key.checkValue(value).ifPresent(MaterialAssetGroup::throwIllegalCharacter);
        }
    }

    private static void throwIllegalCharacter(int position) {
        throw new IllegalArgumentException(
                "The asset value does not follow key value pattern by having an illegal character at position: "
                        + position
        );
    }
}