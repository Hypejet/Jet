package net.hypejet.jet.data.json.model.trim.material;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Trim material of a Minecraft armor.
 *
 * @param assets assets to be used when rendering an armor with this trim material
 * @param description the name of the trim material to be displayed on the armor tooltip
 * @since 1.0
 */
public record JsonTrimMaterial(@NonNull JsonMaterialAssetGroup assets, @NonNull Component description) {
    /**
     * Constructs the {@linkplain JsonTrimMaterial trim material}.
     *
     * @param assets assets to be used when rendering an armor with this trim material
     * @param description the name of the trim material to be displayed on the armor tooltip
     * @since 1.0
     */
    public JsonTrimMaterial {
        Objects.requireNonNull(assets, "assets");
        Objects.requireNonNull(description, "description");
    }
}