package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an implementation of the {@linkplain RegistryEntry registry entry}.
 *
 * @param key a key of the entry
 * @param value a value of the entry
 * @param dataPack a data pack, which enables the entry, {@code null} if no data pack enables the entry
 * @param <V> a type of value of the entry
 * @since 1.0
 * @author Codestech
 * @see RegistryEntry
 */
public record JetRegistryEntry<V>(@NonNull Key key, @NonNull V value, @Nullable DataPack dataPack)
        implements RegistryEntry<V> {
    /**
     * Constructs the {@linkplain JetRegistryEntry registry entry}.
     *
     * @param key a key of the entry
     * @param value a value of the entry
     * @param dataPack a data pack, which enables the entry, {@code null} if no data pack enables the entry
     */
    public JetRegistryEntry {
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(value, "value");
    }
}