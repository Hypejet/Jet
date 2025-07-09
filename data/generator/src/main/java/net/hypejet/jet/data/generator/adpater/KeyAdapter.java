package net.hypejet.jet.data.generator.adpater;

import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain ResourceLocation resource locations} to {@linkplain Key keys}.
 *
 * @since 1.0
 * @see ResourceLocation
 */
public final class KeyAdapter {

    private KeyAdapter() {}

    /**
     * Converts the specified {@linkplain ResourceLocation resource location} to a {@linkplain Key key}.
     *
     * @param location the resource location to convert
     * @return the key
     * @since 1.0
     */
    public static @NonNull Key convert(@NonNull ResourceLocation location) {
        return Key.key(location.toString());
    }
}