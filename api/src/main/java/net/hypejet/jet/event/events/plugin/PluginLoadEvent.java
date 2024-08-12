package net.hypejet.jet.event.events.plugin;

import net.hypejet.jet.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Plugin plugin} has been loaded.
 *
 * @param plugin the plugin
 * @since 1.0
 * @author Codestech
 */
public record PluginLoadEvent(@NonNull Plugin plugin) {}