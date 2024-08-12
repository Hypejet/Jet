package net.hypejet.jet.plugin;

import net.hypejet.jet.plugin.metadata.PluginMetadata;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that changes or adds a functionality on the server.
 *
 * @since 1.0
 * @author Codestech
 */
public interface Plugin {
    /**
     * Gets a {@linkplain PluginMetadata metadata} of the plugin.
     *
     * @return the metadata
     * @since 1.0
     */
    @NonNull PluginMetadata metadata();

    /**
     * Gets an instance of the {@linkplain }
     * @return
     */
    @NonNull Object instance();
}