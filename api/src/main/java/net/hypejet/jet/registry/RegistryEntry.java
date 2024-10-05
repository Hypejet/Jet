package net.hypejet.jet.registry;

import net.hypejet.jet.data.model.pack.DataPack;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an entry of a {@linkplain Registry registry}.
 *
 * @param <V> a type of value of the entry
 * @since 1.0
 * @author Codestech
 * @see Registry
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
     * Gets a {@linkplain DataPack data pack}, which enables this {@linkplain RegistryEntry registry entry}.
     *
     * @return the data pack, {@code null} if no data packs enable this entry
     * @since 1.0
     */
    @Nullable DataPack dataPack();
}