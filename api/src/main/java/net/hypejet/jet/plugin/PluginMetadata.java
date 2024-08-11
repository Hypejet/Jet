package net.hypejet.jet.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents a metadata of a {@linkplain Plugin plugin}.
 *
 * @param name a name of the plugin
 * @param version a version of the plugin
 * @param mainClass a full name of the main class of the plugin
 * @param authors authors of the plugin
 * @param dependencies dependencies of the plugin
 * @since 1.0
 * @author Codestech
 */
public record PluginMetadata(@NonNull String name, @NonNull String version, @NonNull String mainClass,
                             @NonNull Collection<String> authors, @NonNull Collection<PluginDependency> dependencies) {}