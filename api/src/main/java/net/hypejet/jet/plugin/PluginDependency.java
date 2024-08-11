package net.hypejet.jet.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a dependency of a plugin.
 *
 * @param pluginName a name of the plugin, which is the dependency
 * @param required whether the plugin is required
 * @since 1.0
 * @author Codestech
 * @see PluginMetadata
 */
public record PluginDependency(@NonNull String pluginName, boolean required) {}