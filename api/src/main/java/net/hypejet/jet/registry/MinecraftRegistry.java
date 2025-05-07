package net.hypejet.jet.registry;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Represents a Minecraft registry.
 *
 * @param <V> a type of values of entries in this registry
 * @since 1.0
 */
public interface MinecraftRegistry<V> {
    /**
     * Gets {@linkplain Key a key} of this Minecraft registry.
     *
     * @return the key
     * @since 1.0
     */
    @NonNull Key registryKey();

    /**
     * Gets {@linkplain Class a class} of values of entries of this Minecraft registry.
     *
     * @return the class
     * @since 1.0
     */
    @NonNull Class<V> entryValueClass();

    /**
     * Gets a registry entry by a key of it.
     *
     * @param key the key
     * @return the registry entry
     * @since 1.0
     */
    @Nullable RegistryEntry<V> get(@NonNull Key key);

    /**
     * Gets a registry entry by a numeric identifier of it.
     *
     * @param identifier the numeric identifier
     * @return the registry entry
     * @since 1.0
     */
    @Nullable RegistryEntry<V> get(int identifier);

    /**
     * Gets whether a registry entry specified was registered in this Minecraft registry.
     *
     * @param entry the registry entry
     * @return {@code true} if the registry entry was registered in this registry, {@code false} otherwise
     * @since 1.0
     */
    boolean isRegistered(@NonNull RegistryEntry<V> entry);

    /**
     * Gets a numeric identifier of a registry entry registered in this Minecraft registry.
     *
     * @param entry the registry entry
     * @return a numeric identifier of the registry entry
     * @throws IllegalArgumentException if the registry entry specified has not been specified in this registry
     * @since 1.0
     */
    int identifierOf(@NonNull RegistryEntry<V> entry);

    /**
     * Gets {@linkplain List a list} of all entries registered in this Minecraft registry.
     *
     * <p>The entries are sorted by their numeric identifier.</p>
     *
     * @return the list
     * @since 1.0
     */
    @NonNull List<? extends RegistryEntry<V>> entries();

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition} defining whether
     * {@linkplain RegistryEntry a registry entry} specified has a tag specified attached.
     *
     * <p>The acquisition throws {@linkplain IllegalArgumentException an illegal argument exception} when
     * {@link CollectionAcquisition#collection()} is called and if the registry entry specified has not been registered
     * in this registry. </p>
     *
     * @param entry the registry entry
     * @param tag the tag
     * @return the acquisition with a value of {@code true} if the registry entry specified has
     *         a tag specified attached, {@code false} otherwise
     * @since 1.0
     */
    @NonNull BooleanAcquisition hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag);

    /**
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of {@linkplain Collection a collection} of
     * tags attached to {@linkplain RegistryEntry a registry entry} specified.
     *
     * <p>The acquisition throws {@linkplain IllegalArgumentException an illegal argument exception} when
     * {@link CollectionAcquisition#collection()} is called and if the registry entry specified has not been registered
     * in this registry. </p>
     *
     * @param entry the registry entry
     * @return the collection
     * @since 1.0
     */
    @NonNull CollectionAcquisition<Key, ?> tagsFor(@NonNull RegistryEntry<V> entry);

    /**
     * Updates tags for {@linkplain RegistryEntry a registry entry} specified.
     *
     * @param entry the registry entry
     * @param tagUnaryOperator a unary operator to update the tags with, the provided collection is a collection of
     *                         current tags, the returned collection is a collection of new tags
     * @throws IllegalArgumentException if the registry entry specified has not been specified in this registry
     * @since 1.0
     */
    void updateTags(@NonNull RegistryEntry<V> entry, @NonNull UnaryOperator<Collection<Key>> tagUnaryOperator);
}