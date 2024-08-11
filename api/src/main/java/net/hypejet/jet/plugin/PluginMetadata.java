package net.hypejet.jet.plugin;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Represents a metadata of a {@linkplain Plugin plugin}.
 *
 * @param name a name of the plugin
 * @param version a version of the plugin
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
     * @param name a name of the plugin
     * @param version a version of the plugin
     * @param entrypoints entrypoints of the plugin, the values should be valid class paths
     * @param authors authors of the plugin
     * @param dependencies dependencies of the plugin
     * @since 1.0
     */
    public PluginMetadata {
        entrypoints = Map.copyOf(entrypoints);
        authors = Set.copyOf(authors);
        dependencies = Set.copyOf(dependencies);
    }
}