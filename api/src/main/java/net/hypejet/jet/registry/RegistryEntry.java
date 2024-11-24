package net.hypejet.jet.registry;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an entry of a {@linkplain MinecraftRegistry registry}.
 *
 * @param <V> a type of value of the entry
 * @since 1.0
 * @author Codestech
 * @see MinecraftRegistry
 */
public interface RegistryEntry<V> extends Keyed {
    /**
     * Gets a {@linkplain Key key} of the entry.
     *
     * @return the key
     * @since 1.0
     */
    @Override
    @NonNull Key key();

    /**
     * Gets a value of the entry.
     *
     * @return the value
     * @since 1.0
     */
    @NonNull V value();

    /**
     * Gets an information of a feature pack, which enables this entry.
     *
     * @return the feature pack, {@code null} if no feature packs enable the entry
     * @since 1.0
     */
    @Nullable PackInfo knownPackInfo();
}