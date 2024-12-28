package net.hypejet.jet.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

/**
 * Represents something that manages {@linkplain Plugin plugins} of
 * {@linkplain net.hypejet.jet.MinecraftServer a minecraft server}.
 *
 * @since 1.0
 * @see Plugin
 * @see net.hypejet.jet.MinecraftServer
 */
public interface PluginManager {
    /**
     * Gets {@linkplain Plugin a plugin} by a name of it.
     *
     * @param name the name
     * @return the plugin, {@code null} if there is no a plugin registered with the name specified
     * @since 1.0
     */
    @Nullable Plugin getPlugin(@NonNull String name);

    /**
     * Gets {@linkplain Plugin a plugin} by a main class instance of it.
     *
     * @param instance the instance
     * @return the plugin
     * @since 1.0
     */
    @Nullable Plugin getPlugin(@NonNull Object instance);

    /**
     * Gets whether {@linkplain Plugin a plugin} with a name specified has been loaded.
     *
     * @param name the name
     * @return {@code true} if the plugin has been loaded, {@code false} otherwise
     * @since 1.0
     */
    boolean isLoaded(@NonNull String name);

    /**
     * Gets {@linkplain Collection a collection} of {@linkplain Plugin plugins} registered on the server.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<? extends Plugin> plugins();
}