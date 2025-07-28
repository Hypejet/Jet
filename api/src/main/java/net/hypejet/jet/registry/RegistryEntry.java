package net.hypejet.jet.registry;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents an entry of {@linkplain MinecraftRegistry a Minecraft registry}.
 *
 * @param <V> a type of value of the entry
 * @since 1.0
 * @see MinecraftRegistry
 */
@ApiStatus.NonExtendable
public non-sealed interface RegistryEntry<V> extends Keyed, Holder<V> {
    /**
     * Gets {@linkplain Key a key} of the registry entry.
     *
     * @return the key
     * @since 1.0
     */
    @Override
    @NonNull Key key();

    /**
     * Gets the {@linkplain MinecraftRegistry registry} owning this registry entry.
     *
     * @return the registry owning this registry entry
     * @since 1.0
     */
    @NonNull MinecraftRegistry<? super V> registry();

    /**
     * Gets a value of the registry entry.
     *
     * @return the value
     * @since 1.0
     */
    @NonNull V value();

    /**
     * Gets an information of a feature pack, which enables this registry entry.
     *
     * @return the feature pack, {@code null} if no feature packs enable the entry
     * @since 1.0
     */
    @Nullable PackInfo knownPackInfo();
}