package net.hypejet.jet.server.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.hypejet.jet.plugin.PluginDependency;
import net.hypejet.jet.plugin.PluginManager;
import net.hypejet.jet.plugin.PluginMetadata;
import net.hypejet.jet.server.plugin.json.PluginDependencyDeserializer;
import net.hypejet.jet.server.plugin.json.PluginMetadataDeserializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Represents an implementation of the {@linkplain PluginManager plugin manager}.
 *
 * @since 1.0
 * @author Codestech
 * @see PluginManager
 */
public final class JetPluginManager implements PluginManager {

    private static final String PLUGIN_JSON_FILE_NAME = "jet-plugin.json";
    private static final Path PLUGIN_PATH = Path.of("plugins");

    private static final Logger LOGGER = LoggerFactory.getLogger(JetPluginManager.class);

    private static final Gson PLUGIN_METADATA_GSON = new GsonBuilder()
            .registerTypeAdapter(PluginDependency.class, new PluginDependencyDeserializer())
            .registerTypeAdapter(PluginMetadata.class, new PluginMetadataDeserializer())
            .create();

    private final Map<String, JetPlugin> plugins;

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

            Map<String, PluginPair> pairs = new HashMap<>();

            try (Stream<Path> paths = Files.list(PLUGIN_PATH)) {
                Iterator<Path> pathIterator = paths.iterator();

                while (pathIterator.hasNext()) {
                    Path path = pathIterator.next();
                    if (!Files.isRegularFile(path)) continue; // Might be a plugin data directory

                    URLClassLoader classLoader = new URLClassLoader(new URL[] { path.toUri().toURL() },
                            this.getClass().getClassLoader());
                    PluginMetadata pluginMetadata;

                    try (InputStream stream = classLoader.getResourceAsStream(PLUGIN_JSON_FILE_NAME)) {
                        if (stream == null) {
                            throw new IllegalArgumentException("The plugin does not contain a \""
                                    + PLUGIN_JSON_FILE_NAME + "\" file");
                        }

                        pluginMetadata = PLUGIN_METADATA_GSON.fromJson(new InputStreamReader(stream),
                                PluginMetadata.class);
                    } catch (Throwable throwable) {
                        classLoader.close();
                        throw new RuntimeException("An error occurred while creating a plugin with file of \""
                                + path.getFileName().toString() + "\"", throwable);
                    }

                    pairs.put(pluginMetadata.name(), new PluginPair(pluginMetadata, classLoader));
                }
            }

            Map<String, JetPlugin> plugins = new HashMap<>();

            for (Map.Entry<String, PluginPair> entry : pairs.entrySet()) {
                String name = entry.getKey();
                if (plugins.containsKey(name)) continue;

                try {
                    plugins.put(name, load(entry.getValue(), pairs));
                } catch (Throwable throwable) {
                    entry.getValue().classLoader().close();
                    LOGGER.error("An error occurred while loading a plugin with name of \"{}\"", name, throwable);
                }
            }

            this.plugins = Map.copyOf(plugins);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public @Nullable JetPlugin getPlugin(@NonNull String name) {
        return this.plugins.get(name);
    }

    @Override
    public @NonNull Collection<JetPlugin> plugins() {
        return Set.copyOf(this.plugins.values());
    }

    /**
     * Shuts down the {@linkplain JetPluginManager plugin manager}.
     *
     * @since 1.0
     */
    public void shutdown() {
        for (JetPlugin plugin : this.plugins()) {
            try {
                plugin.classLoader().close();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private static @NonNull JetPlugin load(@NonNull PluginPair pluginPair, @NonNull Map<String, PluginPair> pairs,
                                           @NonNull String @NonNull ... dependants) {
        try {
            PluginMetadata pluginMetadata = pluginPair.metadata();
            URLClassLoader classLoader = pluginPair.classLoader();

            String pluginName = pluginMetadata.name();

            for (String dependant : dependants) {
                if (!dependant.equals(pluginName)) continue;
                throw new IllegalArgumentException("A plugin cannot require itself or a set of plugins requiring it");
            }

            for (PluginDependency dependency : pluginMetadata.dependencies()) {
                String dependencyPluginName = dependency.pluginName();
                PluginPair dependencyPluginPair = pairs.get(dependencyPluginName);

                if (dependencyPluginPair != null) {
                    String[] newDependants = Arrays.copyOf(dependants, dependants.length + 1);
                    newDependants[dependants.length] = pluginName;
                    load(dependencyPluginPair, pairs, newDependants);
                }

                if (dependency.required()) {
                    throw new IllegalArgumentException("Plugin with name of \"" + pluginName + "\" requires a plugin"
                            + " with name of \"" + dependencyPluginName + "\"");
                }
            }

            Class<?> mainClass = Class.forName(pluginMetadata.mainClass(), true, classLoader);
            Object instance = mainClass.getDeclaredConstructor().newInstance();

            JetPlugin plugin = new JetPlugin(pluginMetadata, instance, classLoader);

            StringBuilder builder = new StringBuilder();

            Collection<String> authors = pluginMetadata.authors();
            String version = pluginMetadata.version();

            builder.append("Loaded plugin \"")
                    .append(pluginName)
                    .append("\" with version of \"")
                    .append(version)
                    .append("\"");

            if (!authors.isEmpty()) {
                StringJoiner joiner = new StringJoiner(", ");
                authors.forEach(joiner::add);
                builder.append(" made by ").append(joiner);
            }

            LOGGER.info(builder.toString());
            return plugin;
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Represents something that contains a {@linkplain PluginMetadata metadata} and a {@linkplain URLClassLoader
     * class loader} of a {@linkplain JetPlugin plugin}, which has not been initialized yet.
     *
     * @param metadata the metadata
     * @param classLoader the class loader
     * @since 1.0
     * @author Codestech
     */
    private record PluginPair(@NonNull PluginMetadata metadata, @NonNull URLClassLoader classLoader) {}
}