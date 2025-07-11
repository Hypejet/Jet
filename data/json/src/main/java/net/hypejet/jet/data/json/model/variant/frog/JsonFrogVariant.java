package net.hypejet.jet.data.json.model.variant.frog;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft frog entity.
 *
 * @param asset the key of the texture that frogs of this variant should have
 * @since 1.0
 */
public record JsonFrogVariant(@NonNull Key asset) {
    /**
     * Constructs the {@linkplain JsonFrogVariant frog variant}.
     *
     * @param asset the key of the texture that frogs of this variant should have
     * @since 1.0
     */
    public JsonFrogVariant {
        Objects.requireNonNull(asset, "asset");
    }
}