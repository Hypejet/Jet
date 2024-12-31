package net.hypejet.jet.server.plugin.metadata;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.plugin.dependency.PluginDependency;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * Represents a metadata of {@linkplain Plugin a plugin}.
 *
 * @param name a name of the plugin, must not be empty
 * @param version a version of the plugin, must not be empty
 * @param entrypoints entrypoints of the plugin, the values must be valid class paths
 * @param authors authors of the plugin
 * @param dependencies dependencies of the plugin
 * @since 1.0
 */
public record PluginMetadata(@NonNull String name, @NonNull String version, @NonNull Map<Key, String> entrypoints,
                             @NonNull Set<String> authors, @NonNull Set<PluginDependency> dependencies) {
    /**
     * Constructs the {@linkplain PluginMetadata plugin metadata}.
     *
     * @param name a name of the plugin, must not be empty
     * @param version a version of the plugin, must not be empty
     * @param entrypoints entrypoints of the plugin, the values must be valid class paths
     * @param authors authors of the plugin
     * @param dependencies dependencies of the plugin
     * @since 1.0
     * @throws IllegalArgumentException if the name or version is empty
     */
    public PluginMetadata {
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(version, "version");

        if (name.isEmpty())
            throw new IllegalArgumentException("The name must not be empty");
        if (version.isEmpty())
            throw new IllegalArgumentException("The version must not be empty");

        entrypoints = Map.copyOf(NullabilityUtil.requireNonNull(entrypoints, "entrypoints"));
        authors = Set.copyOf(NullabilityUtil.requireNonNull(authors, "authors"));
        dependencies = Set.copyOf(NullabilityUtil.requireNonNull(dependencies, "dependencies"));
    }
}