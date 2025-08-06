package net.hypejet.jet.server.registry;

import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.feature.KnownPack;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an implementation of the {@linkplain RegistryEntry registry entry}.
 *
 * @param key a key of the entry
 * @param value a value of the entry
 * @param knownPack a metadata of a feature pack, which enables the entry, {@code null} if no feature
 *                  pack enables the entry
 * @param <V> a type of value of the entry
 * @since 1.0
 * @see RegistryEntry
 */
// TODO: Replace usages with Holder and remove this class
public record JetRegistryEntry<V>(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack)
        implements RegistryEntry<V> {
    /**
     * Constructs the {@linkplain JetRegistryEntry registry entry}.
     *
     * @param key a key of the entry
     * @param value a value of the entry
     * @param knownPack a metadata of a feature pack, which enables the entry, {@code null} if no feature
     *                  pack enables the entry
     * @since 1.0
     */
    public JetRegistryEntry {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
    }

    @Override
    public @NotNull Holder.Reference<V> createHolder() {
        return new Holder.Reference<>(this.key);
    }
}