package net.hypejet.jet.registry;

import net.hypejet.jet.registry.holder.Holder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

/**
 * Represents an entry of {@linkplain MinecraftRegistry a Minecraft registry}.
 *
 * @param <V> a type of value of the entry
 * @since 1.0
 * @see MinecraftRegistry
 */
@ApiStatus.NonExtendable
public interface RegistryEntry<V> extends Keyed {
    /**
     * Gets {@linkplain Key a key} of the registry entry.
     *
     * @return the key
     * @since 1.0
     */
    @Override
    @NonNull Key key();

    /**
     * Gets a value of the registry entry.
     *
     * @return the value
     * @since 1.0
     */
    @NonNull V value();

    /**
     * Creates a {@linkplain Holder.Reference reference holder}
     * referencing to this {@linkplain RegistryEntry registry entry}.
     *
     * @return the reference holder
     * @since 1.0
     */
    Holder.@NonNull Reference<V> createHolder();
}