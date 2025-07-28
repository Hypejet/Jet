package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents an implementation of the {@linkplain RegistryEntry registry entry}.
 *
 * @param key a key of the entry
 * @param registry a registry owning the registry entry
 * @param value a value of the entry
 * @param knownPackInfo an information of a feature pack, which enables the entry, {@code null} if no feature pack
 *                      enables the entry
 * @param <V> a type of value of the entry
 * @since 1.0
 * @see RegistryEntry
 */
public record JetRegistryEntry<V>(
        @NonNull Key key, @NonNull MinecraftRegistry<? super V> registry,
        @NonNull V value, @Nullable PackInfo knownPackInfo
) implements RegistryEntry<V> {
    /**
     * Constructs the {@linkplain JetRegistryEntry registry entry}.
     *
     * @param key a key of the entry
     * @param registry a registry owning the registry entry
     * @param value a value of the entry
     * @param knownPackInfo an information of a feature pack, which enables the entry, {@code null} if no feature pack
     *                      enables the entry
     */
    public JetRegistryEntry {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(value, "value");
    }
}