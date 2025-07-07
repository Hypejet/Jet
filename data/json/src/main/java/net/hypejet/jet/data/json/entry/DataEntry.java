package net.hypejet.jet.data.json.entry;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Represents an extracted and converted entry from a Minecraft registry.
 *
 * @param key a key of the entry
 * @param value a value of the entry
 * @param tags tags of the entry
 * @param requiredPacks keys of required feature packs to enable the entry
 * @param <V> a type of the value
 * @since 1.0
 */
public record DataEntry<V>(@NonNull Key key, @NonNull V value, @NonNull Set<Key> tags,
                           @NonNull Set<Key> requiredPacks) {
    /**
     * Constructs the {@linkplain DataEntry data entry}.
     *
     * @param key a key of the entry
     * @param tags tags of the entry
     * @param value a value of the entry
     * @param requiredPacks keys of required feature packs to enable the entry
     * @since 1.0
     */
    public DataEntry {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        tags = Set.copyOf(Objects.requireNonNull(tags, "tags"));
        requiredPacks = Set.copyOf(Objects.requireNonNull(requiredPacks, "required packs"));
    }
}