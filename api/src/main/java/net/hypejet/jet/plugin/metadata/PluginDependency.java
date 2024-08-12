package net.hypejet.jet.plugin.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a plugin dependency of another plugin.
 *
 * @param pluginName a name of the plugin, which is the dependency
 * @param versionsSupported versions of the plugin, which are supported, must not be empty
 * @param required whether the plugin is required
 * @since 1.0
 * @author Codestech
 * @see PluginMetadata
 */
public record PluginDependency(@NonNull String pluginName, @NonNull Collection<PluginVersion> versionsSupported,
                               boolean required) {
    /**
     * Constructs the {@linkplain PluginDependency plugin dependency}.
     *
     * @param pluginName a name of the plugin, which is the dependency
     * @param versionsSupported versions of the plugin, which are supported, must not be empty
     * @param required whether the plugin is required
     * @since 1.0
     * @throws IllegalArgumentException if the versions supported is empty
     */
    public PluginDependency {
        Objects.requireNonNull(pluginName, "The plugin name must not be null");
        Objects.requireNonNull(versionsSupported, "The versions supported must not be null");

        if (versionsSupported.isEmpty())
            throw new IllegalArgumentException("The versions supported must not be empty");

        versionsSupported = Set.copyOf(versionsSupported);
    }
}