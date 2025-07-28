package net.hypejet.jet.inventory.item.trim;

import org.jspecify.annotations.NonNull;

import java.awt.Component;
import java.util.Objects;

/**
 * Trim material of a Minecraft armor.
 *
 * @param assets assets to be used when rendering an armor with this trim material
 * @param description the name of the trim material to be displayed on the armor tooltip
 * @since 1.0
 */
public record TrimMaterial(@NonNull MaterialAssetGroup assets, @NonNull Component description) {
    /**
     * Constructs the {@linkplain TrimMaterial trim material}.
     *
     * @param assets assets to be used when rendering an armor with this trim material
     * @param description the name of the trim material to be displayed on the armor tooltip
     * @since 1.0
     */
    public TrimMaterial {
        Objects.requireNonNull(assets, "assets");
        Objects.requireNonNull(description, "description");
    }
}