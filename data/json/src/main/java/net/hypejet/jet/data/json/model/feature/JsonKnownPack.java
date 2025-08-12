package net.hypejet.jet.data.json.model.feature;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A metadata of a Minecraft feature pack.
 *
 * @param namespace a namespace of the feature pack identifier
 * @param path a path of the feature pack identifier
 * @param version a version of the feature pack
 * @since 1.0
 */
public record JsonKnownPack(@NonNull String namespace, @NonNull String path, @NonNull String version) {
    /**
     * Constructs the {@linkplain JsonKnownPack known pack}.
     *
     * @param namespace a namespace of the feature pack identifier
     * @param path a path of the feature pack identifier
     * @param version a version of the feature pack
     * @since 1.0
     */
    public JsonKnownPack {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(version, "version");
    }
}