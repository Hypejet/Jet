package net.hypejet.jet.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

/**
 * Represents something that manages {@linkplain Plugin plugins} on the server.
 *
 * @since 1.0
 * @author Codestech
 * @see Plugin
 */
public interface PluginManager {
    /**
     * Gets a {@linkplain Plugin plugin} by name of it.
     *
     * @param name the name
     * @return the plugin, {@code null} if there is no a plugin registered with the name specified
     * @since 1.0
     */
    @Nullable Plugin getPlugin(@NonNull String name);

    /**
     * Gets a {@linkplain Collection collection} of {@linkplain Plugin plugins} registered on the server.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<Plugin> plugins();
}