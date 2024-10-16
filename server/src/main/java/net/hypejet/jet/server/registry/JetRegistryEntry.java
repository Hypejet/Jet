package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an implementation of the {@linkplain RegistryEntry registry entry}.
 *
 * @param key a key of the entry
 * @param value a value of the entry
 * @param knownPackInfo an information of a feature pack, which enables the entry, {@code null} if no feature pack
 *                      enables the entry
 * @param <V> a type of value of the entry
 * @since 1.0
 * @author Codestech
 * @see RegistryEntry
 */
public record JetRegistryEntry<V>(@NonNull Key key, @NonNull V value, @Nullable PackInfo knownPackInfo)
        implements RegistryEntry<V> {
    /**
     * Constructs the {@linkplain JetRegistryEntry registry entry}.
     *
     * @param key a key of the entry
     * @param value a value of the entry
     * @param knownPackInfo an information of a feature pack, which enables the entry, {@code null} if no feature pack
     *                      enables the entry
     */
    public JetRegistryEntry {
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(value, "value");
    }
}