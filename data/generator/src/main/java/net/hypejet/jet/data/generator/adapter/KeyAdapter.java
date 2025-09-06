package net.hypejet.jet.data.generator.adapter;

import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Set;

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

    /**
     * Converts the specified {@linkplain ResourceLocation resource location} {@linkplain Set set}
     * to a {@linkplain Key key} {@linkplain Set set}.
     *
     * @param locations the resource location set to convert
     * @return the key set
     * @since 1.0
     */
    public static @NonNull Set<Key> convertSet(@NonNull Set<ResourceLocation> locations) {
        Set<Key> keySet = new HashSet<>();
        locations.forEach(location -> keySet.add(convert(location)));
        return keySet;
    }
}
