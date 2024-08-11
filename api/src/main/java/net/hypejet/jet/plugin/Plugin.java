package net.hypejet.jet.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents something that changes or adds a functionality on the server.
 *
 * @param name a name of the plugin
 * @param version a version of the plugin
 * @param mainClassPath a full name of the main class of the plugin
 * @param authors authors of the plugin
 * @param dependencies dependencies of the plugin
 * @since 1.0
 * @author Codestech
 */
public record Plugin(@NonNull String name, @NonNull String version, @NonNull String mainClassPath,
                     @NonNull Collection<String> authors, @NonNull Collection<PluginDependency> dependencies) {
    /**
     * Constructs the {@linkplain Plugin plugin}.
     *
     * @param name a name of the plugin
     * @param version a version of the plugin
     * @param mainClassPath a full name of the main class of the plugin
     * @param authors authors of the plugin
     * @param dependencies dependencies of the plugin
     * @since 1.0
     *
     */
    public Plugin {
        dependencies = Set.copyOf(dependencies);
    }
}