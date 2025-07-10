package net.hypejet.jet.data.json.model.trim.material;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a group of assets to be used on a {@linkplain JsonTrimMaterial trim material}.
 *
 * @param baseAsset the trim color model to be rendered on top of the armor
 * @param overrides assets overriding the base asset depending on types of armor
 * @since 1.0
 * @see JsonTrimMaterial
 */
public record JsonMaterialAssetGroup(@NonNull Asset baseAsset, @NonNull Map<Key, Asset> overrides) {
    /**
     * Constructs the {@linkplain JsonMaterialAssetGroup material asset group}.
     *
     * @param baseAsset the trim color model to be rendered on top of the armor
     * @param overrides assets overriding the base asset depending on types of armor
     * @since 1.0
     */
    public JsonMaterialAssetGroup {
        Objects.requireNonNull(baseAsset, "base asset");
        overrides = Map.copyOf(Objects.requireNonNull(overrides, "overrides"));
    }

    /**
     * A texture asset used to render parts of an armor with a {@linkplain JsonTrimMaterial trim material}.
     *
     * @param value a path of the asset in {@code trim/color_palettes} namespace
     * @since 1.0
     * @see JsonTrimMaterial
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
            Key.checkValue(value).ifPresent(JsonMaterialAssetGroup::throwIllegalCharacter);
        }
    }

    private static void throwIllegalCharacter(int position) {
        throw new IllegalArgumentException(
                "The asset value does not follow key value pattern by having an illegal character at position: "
                        + position
        );
    }
}