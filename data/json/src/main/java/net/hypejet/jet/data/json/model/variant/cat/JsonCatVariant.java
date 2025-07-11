package net.hypejet.jet.data.json.model.variant.cat;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft cat entity.
 *
 * @param asset the key of the texture that cats with this variant should have
 * @since 1.0
 */
public record JsonCatVariant(@NonNull Key asset) {
    /**
     * Constructs the {@linkplain JsonCatVariant cat variant}.
     *
     * @param asset the key of the texture that cats with this variant should have
     * @since 1.0
     */
    public JsonCatVariant {
        Objects.requireNonNull(asset, "asset");
    }
}