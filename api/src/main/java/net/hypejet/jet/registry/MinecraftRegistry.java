package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a Minecraft registry.
 *
 * @param <V> a type of values available in this registry
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface MinecraftRegistry<V> {
    /**
     * Gets a value associated with the specified {@linkplain Key key}.
     *
     * @param key the key
     * @return the value, {@code null} if no value is associated with the specified key
     * @since 1.0
     */
    @Nullable V get(@NonNull Key key);

    /**
     * Gets a {@linkplain Collection collection} of all {@linkplain Key keys}
     * that have corresponding values in this {@linkplain MinecraftRegistry registry}.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Set<Key> keySet();

    /**
     * Creates a {@linkplain Set set} of keys of tags associated with
     * a registry value associated with the specified {@linkplain Key key}.
     *
     * @param key the key of the registry value
     * @return the set of tags
     * @throws IllegalArgumentException if no registry value is associated with the specified key
     * @since 1.0
     */
    @NonNull Set<Key> tagsFor(@NonNull Key key);

    /**
     * Updates tags for a registry value associated with the specified {@linkplain Key key}.
     *
     * @param key the key of the registry value
     * @param tagKeys keys of tags that should be associated with the specified registry value, {@code null}
     *                or {@link Set#of()} removes all tags from being associated with the specified registry value
     * @throws IllegalArgumentException if no registry value is associated with the specified key
     * @since 1.0
     */
    void updateTags(@NonNull Key key, @Nullable Set<Key> tagKeys);
}