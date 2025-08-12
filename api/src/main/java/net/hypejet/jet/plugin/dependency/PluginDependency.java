package net.hypejet.jet.plugin.dependency;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Represents dependency of {@linkplain net.hypejet.jet.plugin.Plugin a plugin}, which consists of another plugin.
 *
 * @param pluginName a name of the plugin, which is the dependency
 * @param versionsSupported set of strings representing versions of the plugin, which are supported,
 *                          must not be empty
 * @param required whether the dependency plugin is required
 * @since 1.0
 * @see net.hypejet.jet.plugin.Plugin
 */
public record PluginDependency(@NonNull String pluginName, @NonNull Set<String> versionsSupported, boolean required) {
    /**
     * Constructs the {@linkplain PluginDependency plugin dependency}.
     *
     * @param pluginName a name of the plugin, which is the dependency
     * @param versionsSupported set of strings representing versions of the plugin, which are supported,
     *                          must not be empty
     * @param required whether the dependency plugin is required
     * @throws IllegalArgumentException if the set of versions supported is empty
     * @since 1.0
     */
    public PluginDependency {
        Objects.requireNonNull(pluginName, "plugin name");
        Objects.requireNonNull(versionsSupported, "versions supported");

        if (versionsSupported.isEmpty())
            throw new IllegalArgumentException("The versions supported must not be empty");

        versionsSupported = Set.copyOf(versionsSupported);
    }
}