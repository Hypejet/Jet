package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents a Minecraft registry.
 *
 * @param <V> a type of values of entries in this registry
 * @since 1.0
 * @author Codestech
 */
public interface MinecraftRegistry<V> {
    /**
     * Gets a {@linkplain Key key} of this {@linkplain MinecraftRegistry registry}.
     *
     * @return the key
     * @since 1.0
     */
    @NonNull Key registryKey();

    /**
     * Gets a {@linkplain Class class} of values of entries of this {@linkplain MinecraftRegistry registry}.
     *
     * @return the class
     * @since 1.0
     */
    @NonNull Class<V> entryValueClass();

    /**
     * Gets a registry entry by an identifier of it.
     *
     * @param identifier the identifier
     * @return the registry entry
     * @since 1.0
     */
    @Nullable RegistryEntry<V> get(@NonNull Key identifier);

    /**
     * Gets a registry entry by a numeric identifier of it.
     *
     * @param numericIdentifier the numeric identifier
     * @return the registry entry
     * @since 1.0
     */
    @Nullable RegistryEntry<V> get(int numericIdentifier);

    /**
     * Gets whether a registry entry specified was registered in this {@linkplain MinecraftRegistry registry}.
     *
     * @param entry the registry entry
     * @return {@code true} if the registry entry was registered in this registry, {@code false} otherwise
     * @since 1.0
     */
    boolean isRegistered(@NonNull RegistryEntry<V> entry);

    /**
     * Gets a {@linkplain Key key} of a registry entry registered in this {@linkplain MinecraftRegistry registry}.
     *
     * @param entry the registry entry
     * @return the identifier
     * @since 1.0
     */
    @NonNull Key keyOf(@NonNull RegistryEntry<V> entry);

    /**
     * Gets a numeric identifier of a registry entry registered in this {@link MinecraftRegistry registry}.
     *
     * @param entry the registry entry
     * @return a numeric identifier of the registry entry
     * @since 1.0
     */
    int identifierOf(@NonNull RegistryEntry<V> entry);

    /**
     * Gets a {@linkplain List list} of all entries registered in this {@linkplain MinecraftRegistry registry}.
     *
     * <p>The entries are sorted by their numeric identifier.</p>
     *
     * @return the list
     * @since 1.0
     */
    @NonNull List<? extends RegistryEntry<V>> entries();

    /**
     * Gets whether a {@linkplain RegistryEntry registry entry} specified has a tag specified attached.
     *
     * @param entry the registry entry
     * @param tag the tag
     * @return {@code true} if the registry entry specified has a tag specified attached, {@code false} otherwise
     * @since 1.0
     */
    boolean hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag);

    /**
     * Gets a {@linkplain Collection collection} of tags attached to a {@linkplain RegistryEntry registry entry}
     * specified.
     *
     * @param entry the registry entry
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<Key> tagsFor(@NonNull RegistryEntry<V> entry);
}