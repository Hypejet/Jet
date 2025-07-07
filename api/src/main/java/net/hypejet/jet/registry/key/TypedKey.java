package net.hypejet.jet.registry.key;

import net.hypejet.jet.registry.MinecraftRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents holder of a {@linkplain Key key} of certain entry from
 * a {@linkplain MinecraftRegistry Minecraft registry}. For use with the registry API.
 *
 * @param registryKey a registry key representing registry associated with the registry entry using this typed key
 * @param key the key
 * @param <V> a type of value of the registry entry associated with this key
 * @since 1.0
 * @see Key
 * @see MinecraftRegistry
 */
public record TypedKey<V>(@NotNull RegistryKey<V> registryKey, @NotNull Key key) {
    /**
     * Constructs the {@linkplain TypedKey typed key}.
     *
     * @param registryKey a registry key representing registry associated with the registry entry using this typed key
     * @param key the key
     * @since 1.0
     */
    public TypedKey {
        Objects.requireNonNull(registryKey, "registry key");
        Objects.requireNonNull(key, "key");
    }
}