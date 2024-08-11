package net.hypejet.jet.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a dependency of a plugin.
 *
 * @param plugin the plugin, which is the dependency
 * @param required whether the plugin is required
 * @since 1.0
 * @author Codestech
 */
public record PluginDependency(@NonNull Plugin plugin, boolean required) {}