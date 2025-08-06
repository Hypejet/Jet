package net.hypejet.jet.data.json.entry;

import net.hypejet.jet.data.json.model.feature.JsonKnownPack;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * Represents an extracted and converted entry from a Minecraft registry.
 *
 * @param key a key of the entry
 * @param value a value of the entry
 * @param tags tags of the entry
 * @param knownPack information about a feature pack that can enable the registry entry without need to sending value
 *                  to the client, {@code null} if there is no such pack
 * @param <V> a type of the value
 * @since 1.0
 */
public record JsonRegistryEntry<V>(@NonNull Key key, @NonNull V value, @NonNull Set<Key> tags,
                                   @Nullable JsonKnownPack knownPack) {
    /**
     * Constructs the {@linkplain JsonRegistryEntry registry entry}.
     *
     * @param key a key of the entry
     * @param value a value of the entry
     * @param tags tags of the entry
     * @param knownPack information about a feature pack that can enable the registry entry without need to sending value
     *                  to the client, {@code null} if there is no such pack
     * @since 1.0
     */
    public JsonRegistryEntry {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        tags = Set.copyOf(Objects.requireNonNull(tags, "tags"));
    }
}