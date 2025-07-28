package net.hypejet.jet.inventory.item.trim;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Trim pattern of a Minecraft armor.
 *
 * @param assetId key of the trim pattern model to be rendered on top of the armor
 * @param description the name of the trim pattern to be displayed on the armor tooltip
 * @param decal whether the trim is a decal
 * @since 1.0
 */
public record TrimPattern(@NonNull Key assetId, @NonNull Component description, boolean decal) {
    /**
     * Constructs the {@linkplain TrimPattern trim pattern}.
     *
     * @param assetId key of the trim pattern model to be rendered on top of the armor
     * @param description the name of the trim pattern to be displayed on the armor tooltip
     * @param decal whether the trim is a decal
     * @since 1.0
     */
    public TrimPattern {
        Objects.requireNonNull(assetId, "asset id");
        Objects.requireNonNull(description, "description");
    }
}