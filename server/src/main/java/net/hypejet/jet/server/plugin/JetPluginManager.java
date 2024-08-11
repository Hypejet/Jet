package net.hypejet.jet.server.plugin;

import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Represents an implementation of the {@linkplain PluginManager plugin manager}.
 *
 * @since 1.0
 * @author Codestech
 * @see PluginManager
 */
public final class JetPluginManager implements PluginManager {

    private static final Path PLUGIN_PATH = Path.of("plugins");

    private final Map<String, Plugin> plugins;

    /**
     * Constructs the {@linkplain JetPluginManager plugin manager}.
     *
     * @since 1.0
     */
    public JetPluginManager() {
        try {
            if (!Files.exists(PLUGIN_PATH))
                Files.createDirectories(PLUGIN_PATH);

            if (!Files.isDirectory(PLUGIN_PATH))
                throw new IllegalArgumentException("The plugin path is not a directory");

            try (Stream<Path> pluginPaths = Files.list(PLUGIN_PATH)) {
                pluginPaths.forEach(System.out::println);
            }

            this.plugins = Map.of();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public @Nullable Plugin getPlugin(@NonNull String name) {
        return this.plugins.get(name);
    }

    @Override
    public @NonNull Collection<Plugin> plugins() {
        return Set.copyOf(this.plugins.values());
    }
}