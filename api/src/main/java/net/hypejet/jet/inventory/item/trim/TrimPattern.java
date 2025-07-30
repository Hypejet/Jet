package net.hypejet.jet.inventory.item.trim;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Trim pattern of a Minecraft armor.
 *
 * @param asset key of the trim pattern model to be rendered on top of the armor
 * @param description the name of the trim pattern to be displayed on the armor tooltip
 * @param decal whether the trim is a decal
 * @since 1.0
 */
public record TrimPattern(@NonNull Key asset, @NonNull Component description, boolean decal) {
    /**
     * Constructs the {@linkplain TrimPattern trim pattern}.
     *
     * @param asset key of the trim pattern model to be rendered on top of the armor
     * @param description the name of the trim pattern to be displayed on the armor tooltip
     * @param decal whether the trim is a decal
     * @since 1.0
     */
    public TrimPattern {
        Objects.requireNonNull(asset, "asset");
        Objects.requireNonNull(description, "description");
    }
}