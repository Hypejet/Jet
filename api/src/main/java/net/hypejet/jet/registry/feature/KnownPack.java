package net.hypejet.jet.registry.feature;

import net.hypejet.jet.registry.RegistryEntry;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An information of a feature pack that can enable a {@linkplain RegistryEntry registry entry}.
 *
 * @param namespace a namespace of the feature pack
 * @param path a path of the resource pack
 * @param version a version of the resource pack
 * @since 1.0
 * @see RegistryEntry
 */
public record KnownPack(@NonNull String namespace, @NonNull String path, @NonNull String version) {
    /**
     * Constructs the {@linkplain KnownPack known pack}
     *
     * @param namespace a namespace of the feature pack
     * @param path a path of the resource pack
     * @param version a version of the resource pack
     * @since 1.0
     */
    public KnownPack {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(version, "version");
    }
}