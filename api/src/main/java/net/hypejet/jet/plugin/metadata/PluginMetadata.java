package net.hypejet.jet.plugin.metadata;

import net.hypejet.jet.plugin.Plugin;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a metadata of a {@linkplain Plugin plugin}.
 *
 * @param name a name of the plugin, must not be empty
 * @param version a version of the plugin, must not be empty
 * @param entrypoints entrypoints of the plugin, the values should be valid class paths
 * @param authors authors of the plugin
 * @param dependencies dependencies of the plugin
 * @since 1.0
 * @author Codestech
 */
public record PluginMetadata(@NonNull String name, @NonNull String version, @NonNull Map<Key, String> entrypoints,
                             @NonNull Collection<String> authors, @NonNull Collection<PluginDependency> dependencies) {
    /**
     * Constructs the {@linkplain PluginMetadata plugin metadata}.
     *
     * @param name a name of the plugin, must not be empty
     * @param version a version of the plugin, must not be empty
     * @param entrypoints entrypoints of the plugin, the values should be valid class paths
     * @param authors authors of the plugin
     * @param dependencies dependencies of the plugin
     * @since 1.0
     * @throws IllegalArgumentException if the name or version is empty
     */
    public PluginMetadata {
        Objects.requireNonNull(name, "The name must not be null");
        Objects.requireNonNull(version, "The version must not be null");
        entrypoints = Map.copyOf(Objects.requireNonNull(entrypoints, "The entrypoints must not be null"));
        authors = Set.copyOf(Objects.requireNonNull(authors, "The authors must not be null"));
        dependencies = Set.copyOf(Objects.requireNonNull(dependencies, "The dependencies must not be null"));

        if (name.isEmpty())
            throw new IllegalArgumentException("The name must not be empty");
        if (version.isEmpty())
            throw new IllegalArgumentException("The version must not be empty");
    }
}