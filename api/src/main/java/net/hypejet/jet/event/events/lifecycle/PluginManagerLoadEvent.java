package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain PluginManager plugin manager} of the {@linkplain MinecraftServer server}
 * has been initialized, meaning that all {@linkplain Plugin plugins} have been loaded.
 *
 * @param pluginManager the initialized plugin manager
 * @since 1.0
 * @see PluginManager
 * @see MinecraftServer
 */
public record PluginManagerLoadEvent(@NonNull PluginManager pluginManager) {
    /**
     * Constructs the {@linkplain PluginManagerLoadEvent plugin manager load event}.
     *
     * @param pluginManager the initialized plugin manager
     * @since 1.0
     */
    public PluginManagerLoadEvent {
        Objects.requireNonNull(pluginManager, "plugin manager");
    }
}