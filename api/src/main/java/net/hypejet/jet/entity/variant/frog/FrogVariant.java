package net.hypejet.jet.entity.variant.frog;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft frog entity.
 *
 * @param asset the key of the texture that frogs of this variant should have
 * @since 1.0
 */
public record FrogVariant(@NonNull Key asset) {
    /**
     * Constructs the {@linkplain FrogVariant frog variant}.
     *
     * @param asset the key of the texture that frogs of this variant should have
     * @since 1.0
     */
    public FrogVariant {
        Objects.requireNonNull(asset, "asset");
    }
}