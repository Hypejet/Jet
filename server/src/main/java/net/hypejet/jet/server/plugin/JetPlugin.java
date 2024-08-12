package net.hypejet.jet.server.plugin;

import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.plugin.metadata.PluginMetadata;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.URLClassLoader;

/**
 * Represents an implementation of the {@linkplain Plugin plugin}.
 *
 * @param metadata a metadata of the plugin
 * @param instance a main class instance of the plugin
 * @param classLoader a class loader of the plugin
 * @since 1.0
 * @author Codestech
 */
public record JetPlugin(@NonNull PluginMetadata metadata, @NonNull Object instance,
                        @NonNull URLClassLoader classLoader) implements Plugin {}