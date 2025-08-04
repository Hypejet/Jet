package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * Represents a Minecraft registry.
 *
 * @param <V> a type of entry values of this registry
 * @since 1.0
 */
public interface MinecraftRegistry<V> {
    /**
     * Gets a registry entry by its key.
     *
     * @param key the key
     * @return the registry entry
     * @since 1.0
     */
    @Nullable RegistryEntry<V> get(@NonNull Key key);

    /**
     * Gets a {@linkplain Collection collection} of all entries registered in this registry.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<? extends RegistryEntry<V>> entries();

    /**
     * Gets whether the specified {@linkplain RegistryEntry registry entry} is associated with the specified tag.
     *
     * @param entry the registry entry
     * @param tag   the key of the tag
     * @return {@code true} if the registry entry specified is associated with
     *         the specified tag, {@code false} otherwise
     * @throws IllegalArgumentException if the specified registry entry has not been registered in this registry
     * @since 1.0
     */
    boolean hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag);

    /**
     * Creates a {@linkplain Set set} of tags associated with
     * the specified {@linkplain RegistryEntry registry entry}.
     *
     * @param entry the registry entry
     * @return the set of tags
     * @throws IllegalArgumentException if the specified registry entry has not been registered in this registry
     * @since 1.0
     */
    @NonNull Set<Key> tagsFor(@NonNull RegistryEntry<V> entry);

    /**
     * Updates tags for the specified {@linkplain RegistryEntry registry entry}.
     *
     * @param entry            the registry entry
     * @param tagUnaryOperator a unary operator to update the tags with, the provided set
     *                         is a set of current tags, the returned set is a set of new tags
     * @throws IllegalArgumentException if the specified registry entry has not been specified in this registry
     * @since 1.0
     */
    void updateTags(@NonNull RegistryEntry<V> entry, @NonNull UnaryOperator<Set<Key>> tagUnaryOperator);
}