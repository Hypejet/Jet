package net.hypejet.jet.server.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.events.plugin.PluginLoadEvent;
import net.hypejet.jet.event.events.plugin.PluginUnloadEvent;
import net.hypejet.jet.plugin.metadata.PluginDependency;
import net.hypejet.jet.plugin.PluginManager;
import net.hypejet.jet.plugin.metadata.PluginMetadata;
import net.hypejet.jet.plugin.metadata.PluginVersion;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.plugin.json.PluginDependencyDeserializer;
import net.hypejet.jet.server.plugin.json.PluginMetadataDeserializer;
import net.hypejet.jet.server.plugin.json.PluginVersionDeserializer;
import net.hypejet.jet.server.util.version.VersioningUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
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

    private static final String METADATA_FILE_NAME = "jet-plugin-metadata.json";
    private static final Path PLUGIN_PATH = Path.of("plugins");

    private static final Key MAIN_ENTRYPOINT = Key.key("hypejet", "main");

    private static final Logger LOGGER = LoggerFactory.getLogger(JetPluginManager.class);

    private static final Gson PLUGIN_METADATA_GSON = new GsonBuilder()
            .registerTypeAdapter(PluginVersion.class, new PluginVersionDeserializer())
            .registerTypeAdapter(PluginDependency.class, new PluginDependencyDeserializer())
            .registerTypeAdapter(PluginMetadata.class, new PluginMetadataDeserializer())
            .create();

    private final JetMinecraftServer server;
    private final Map<String, JetPlugin> plugins;

    /**
     * Constructs the {@linkplain JetPluginManager plugin manager}.
     *
     * @since 1.0
     */
    public JetPluginManager(@NonNull JetMinecraftServer server) {
        this.server = Objects.requireNonNull(server, "The server must not be null");
        Injector injector = Guice.createInjector(binder -> binder.bind(MinecraftServer.class).toInstance(server));

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

                    String fileName = path.getFileName().toString();

                    URLClassLoader classLoader = new URLClassLoader(fileName, new URL[] { path.toUri().toURL() },
                            this.getClass().getClassLoader());
                    PluginMetadata pluginMetadata;

                    try (InputStream stream = classLoader.getResourceAsStream(METADATA_FILE_NAME)) {
                        if (stream == null) {
                            throw new IllegalArgumentException("The plugin does not contain a \""
                                    + METADATA_FILE_NAME + "\" file");
                        }

                        pluginMetadata = PLUGIN_METADATA_GSON.fromJson(new InputStreamReader(stream),
                                PluginMetadata.class);
                        pairs.put(pluginMetadata.name(), new PluginPair(pluginMetadata, classLoader));
                    } catch (Throwable throwable) {
                        classLoader.close();
                        LOGGER.error("An error occurred while creating a plugin with file name of \"{}\"", fileName,
                                throwable);
                    }
                }
            }

            Map<String, JetPlugin> plugins = new HashMap<>();

            for (Map.Entry<String, PluginPair> entry : pairs.entrySet()) {
                String name = entry.getKey();
                if (plugins.containsKey(name)) continue;

                try {
                    this.load(entry.getValue(), pairs, plugins, injector);
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
        return this.plugins.get(Objects.requireNonNull(name, "The name must not be null"));
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
                this.server.eventNode().call(new PluginUnloadEvent(plugin));
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private void load(@NonNull PluginPair pluginPair, @NonNull Map<String, PluginPair> pairs,
                      @NonNull Map<String, JetPlugin> plugins, @NonNull Injector injector,
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
                    String dependencyVersion = dependencyPluginPair.metadata().version();

                    if (!isCompatible(dependencyVersion, dependency)) {
                        if (!dependency.required()) continue;
                        throw new IllegalArgumentException(String.format(
                                "Version \"%s\" of dependency \"%s\" is incompatible",
                                dependencyVersion,
                                dependencyPluginName
                        ));
                    }

                    if (plugins.containsKey(dependencyPluginName))
                        continue;

                    String[] newDependants = Arrays.copyOf(dependants, dependants.length + 1);
                    newDependants[dependants.length] = pluginName;
                    this.load(dependencyPluginPair, pairs, plugins, injector, newDependants);
                    continue;
                }

                throw new IllegalArgumentException(String.format(
                        "Plugin \"%s\" requires a dependency with name of \"%s\"",
                        pluginName,
                        dependencyPluginName
                ));
            }

            String mainEntrypoint = pluginMetadata.entrypoints().get(MAIN_ENTRYPOINT);
            if (mainEntrypoint == null) {
                throw new IllegalArgumentException(String.format(
                        "The \"%s\" entrypoint has been not specified",
                        MAIN_ENTRYPOINT
                ));
            }

            Class<?> mainClass = Class.forName(mainEntrypoint, true, classLoader);
            JetPlugin plugin = new JetPlugin(pluginMetadata, injector.getInstance(mainClass), classLoader);

            this.server.eventNode().call(new PluginLoadEvent(plugin));

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
            plugins.put(pluginName, plugin);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static boolean isCompatible(@NonNull String versionString, @NonNull PluginDependency dependency) {
        for (PluginVersion version : dependency.versionsSupported())
            if (isVersionCompatible(versionString, version))
                return true;
        return false;
    }

    private static boolean isVersionCompatible(@NonNull String versionString, @NonNull PluginVersion requiredVersion) {
        int[] parsedVersion = VersioningUtil.parseVersion(versionString);
        int[] parsedRequiredVersion = VersioningUtil.parseVersion(requiredVersion.name());

        int minLength = Math.min(parsedVersion.length, parsedRequiredVersion.length);

        for (int index = 0; index < minLength; index++) {
            int digit = parsedVersion[index];
            int requiredDigit = parsedRequiredVersion[index];

            boolean compatible = digit == requiredDigit
                    || (requiredVersion.backwardsCompatible() && digit < requiredDigit)
                    || (requiredVersion.forwardCompatible() && digit > requiredDigit);

            if (!compatible)
                return false;
        }

        return true;
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