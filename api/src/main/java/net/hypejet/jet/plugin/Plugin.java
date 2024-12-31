package net.hypejet.jet.plugin;

import net.hypejet.jet.plugin.dependency.PluginDependency;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

/**
 * Represents an addon that changes or adds functionality on
 * {@linkplain net.hypejet.jet.MinecraftServer a Minecraft server}.
 *
 * @since 1.0
 * @see net.hypejet.jet.MinecraftServer
 */
public interface Plugin {
    /**
     * Gets a name of the plugin.
     *
     * @return the metadata
     * @since 1.0
     */
    @NonNull String name();

    /**
     * Gets a version of the plugin.
     *
     * @return the version
     * @since 1.0
     */
    @NonNull String version();

    /**
     * Gets {@linkplain Set a set} of authors of the plugin.
     *
     * @return the set
     * @since 1.0
     */
    @NonNull Set<String> authors();

    /**
     * Gets {@linkplain Set a set} of dependencies of the plugin.
     *
     * @return the set
     * @since 1.0
     */
    @NonNull Set<PluginDependency> dependencies();

    /**
     * Gets a class path of an entrypoint with {@linkplain Key a key} specified.
     *
     * @param key the key
     * @return the class path, {@code null} if the plugin did not specify an entrypoint with the key specified
     * @since 1.0
     */
    @Nullable String getEntrypoint(@NonNull Key key);

    /**
     * Gets an instance of main class of the plugin.
     *
     * @return the instance
     * @since 1.0
     */
    @NonNull Object instance();
}