package net.hypejet.jet.data.json.entry;

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
 * @param knownPack information about a feature pack that can enable the data entry without need to sending value
 *                  to the client, {@code null} if there is no such pack
 * @param <V> a type of the value
 * @since 1.0
 */
public record DataEntry<V>(@NonNull Key key, @NonNull V value, @NonNull Set<Key> tags,
                           @Nullable FeaturePack knownPack) {
    /**
     * Constructs the {@linkplain DataEntry data entry}.
     *
     * @param key a key of the entry
     * @param value a value of the entry
     * @param tags tags of the entry
     * @param knownPack information about a feature pack that can enable the data entry without need to sending value
     *                  to the client, {@code null} if there is no such pack
     * @since 1.0
     */
    public DataEntry {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        tags = Set.copyOf(Objects.requireNonNull(tags, "tags"));
    }

    /**
     * Represents a feature pack that can enable a {@linkplain DataEntry data entry}.
     *
     * @param namespace a namespace of the feature pack identifier
     * @param value a value of the feature pack identifier
     * @param version a version of the feature pack
     * @since 1.0
     */
    public record FeaturePack(@NonNull String namespace, @NonNull String value, @NonNull String version) {
        /**
         * Constructs the {@linkplain FeaturePack feature pack}.
         *
         * @param namespace a namespace of the feature pack identifier
         * @param value a value of the feature pack identifier
         * @param version a version of the feature pack
         * @since 1.0
         */
        public FeaturePack {
            Objects.requireNonNull(namespace, "namespace");
            Objects.requireNonNull(value, "value");
            Objects.requireNonNull(version, "version");
        }
    }
}