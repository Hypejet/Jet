package net.hypejet.jet.registry.key;

import net.hypejet.jet.registry.MinecraftRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents holder of {@linkplain Key key} of a {@linkplain MinecraftRegistry Minecraft registry}.
 * For use with the {@linkplain TypedKey typed key} and the registry API.
 *
 * @param key the key
 * @param <V> a type of values of the Minecraft registry this key represents
 * @since 1.0
 * @see Key
 * @see MinecraftRegistry
 */
public record RegistryKey<V>(@NotNull Key key) implements Keyed {
    /**
     * Constructs the {@linkplain RegistryKey registry key}.
     *
     * @param key the key
     * @since 1.0
     */
    public RegistryKey {
        Objects.requireNonNull(key, "key");
    }

    /**
     * Creates a {@linkplain TypedKey typed key} to be associated with a registry entry
     * of {@linkplain MinecraftRegistry Minecraft registry} this key represents.
     *
     * @param key a key that the typed key should have
     * @return the created typed key
     * @since 1.0
     */
    public @NotNull TypedKey<V> createTypedKey(@NotNull Key key) {
        return new TypedKey<>(this, key);
    }
}