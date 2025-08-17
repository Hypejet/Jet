package net.hypejet.jet.server.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.plugin.PluginManager;
import net.hypejet.jet.plugin.dependency.PluginDependency;
import net.hypejet.jet.server.plugin.json.PluginDependencyDeserializer;
import net.hypejet.jet.server.plugin.json.PluginMetadataDeserializer;
import net.hypejet.jet.server.plugin.metadata.PluginMetadata;
import net.hypejet.jet.server.plugin.version.PluginVersionParser;
import net.hypejet.jet.server.plugin.version.part.NumberVersionPart;
import net.hypejet.jet.server.plugin.version.part.VersionPart;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * An implementation of the {@linkplain PluginManager plugin manager}.
 *
 * @since 1.0
 * @see PluginManager
 */
public final class JetPluginManager implements PluginManager {

    private static final String METADATA_FILE_NAME = "jet-plugin-metadata.json";
    private static final Key MAIN_ENTRYPOINT = Key.key("hypejet", "main");
    private static final Path PLUGIN_PATH = Path.of("plugins");

    private static final Logger LOGGER = LoggerFactory.getLogger(JetPluginManager.class);

    private static final Gson PLUGIN_METADATA_GSON = new GsonBuilder()
            .registerTypeAdapter(PluginDependency.class, new PluginDependencyDeserializer())
            .registerTypeAdapter(PluginMetadata.class, new PluginMetadataDeserializer())
            .create();

    private final Map<String, JetPlugin> nameToPluginMap;
    private final Map<Object, JetPlugin> instanceToPluginMap;

    /**
     * Constructs the {@linkplain JetPluginManager plugin manager}.
     *
     * @param eventNode an event node that should be passed as an injection argument to plugins when instantiating them
     * @since 1.0
     */
    public JetPluginManager(@NonNull EventNode<Object> eventNode) {
        Objects.requireNonNull(eventNode, "event node");

        Injector injector = Guice.createInjector(
                binder -> binder
                        .bind(new TypeLiteral<EventNode<Object>>() {})
                        .toInstance(eventNode)
        );

        try {
            Map<String, PluginPair> pluginPairs = loadPluginPairs();
            Map<String, PluginLoadResult> pluginLoadResults = new HashMap<>();

            for (Map.Entry<String, PluginPair> entry : pluginPairs.entrySet()) {
                String name = entry.getKey();
                if (pluginLoadResults.containsKey(name)) continue;
                this.load(entry.getValue(), pluginPairs, pluginLoadResults, injector);
            }

            Map<String, JetPlugin> nameToPluginMap = new HashMap<>();
            Map<Object, JetPlugin> instanceToPlugin = new HashMap<>();

            for (Map.Entry<String, PluginLoadResult> entry : pluginLoadResults.entrySet()) {
                if (!(entry.getValue() instanceof PluginLoadResult.Success(JetPlugin plugin))) continue;
                nameToPluginMap.put(entry.getKey(), plugin);
                instanceToPlugin.put(plugin.instance(), plugin);
            }

            this.nameToPluginMap = Map.copyOf(nameToPluginMap);
            this.instanceToPluginMap = Map.copyOf(instanceToPlugin);
        } catch (Throwable throwable) {
            throw new RuntimeException("An error occurred while loading plugins", throwable);
        }
    }

    @Override
    public @Nullable JetPlugin getPlugin(@NonNull String name) {
        return this.nameToPluginMap.get(Objects.requireNonNull(name, "name"));
    }

    @Override
    public @Nullable Plugin getPlugin(@NonNull Object instance) {
        return this.instanceToPluginMap.get(Objects.requireNonNull(instance, "instance"));
    }

    @Override
    public boolean isLoaded(@NonNull String name) {
        return this.nameToPluginMap.containsKey(name);
    }

    @Override
    public @NonNull Collection<JetPlugin> plugins() {
        return Set.copyOf(this.nameToPluginMap.values());
    }

    /**
     * Shuts down the plugin manager.
     *
     * @since 1.0
     */
    public void shutdown() {
        for (JetPlugin plugin : this.plugins()) {
            try {
                plugin.classLoader().close();
            } catch (Throwable throwable) {
                LOGGER.error("An error occurred while unloading a plugin", throwable);
            }
        }
    }

    /**
     * Loads a plugin.
     *
     * @param pluginPair a pair of plugin to load
     * @param pairs all plugin pairs detected
     * @param results results of loaded plugins, must be mutable
     * @param injector an injector to create plugin main class instances with
     * @param dependants names of plugins requiring the plugin to be loaded
     * @return {@code true} if the plugin has been loaded successfully, {@code false} otherwise
     * @since 1.0
     */
    private boolean load(@NonNull PluginPair pluginPair, @NonNull Map<String, PluginPair> pairs,
                         @NonNull Map<String, PluginLoadResult> results, @NonNull Injector injector,
                         @NonNull String @NonNull ... dependants) {
        PluginMetadata pluginMetadata = pluginPair.metadata();
        URLClassLoader classLoader = pluginPair.classLoader();

        String pluginName = pluginMetadata.name();

        try {
            for (String dependant : dependants) {
                if (!dependant.equals(pluginName)) continue;
                throw new IllegalArgumentException(String.format(
                        "Plugin \"%s\" requires itself or plugins requiring it",
                        pluginName
                ));
            }

            for (PluginDependency dependency : pluginMetadata.dependencies()) {
                if (!this.loadDependency(dependency, pluginName, pairs, injector, results, dependants)
                        && dependency.required()) {
                    LOGGER.warn(
                            "Could not load plugin \"{}\", because plugin \"{}\" has not been loaded successfully",
                            pluginName, dependency.pluginName()
                    );
                    return false;
                }
            }

            String mainEntrypoint = pluginMetadata.entrypoints().get(MAIN_ENTRYPOINT);
            if (mainEntrypoint == null) {
                throw new IllegalArgumentException(String.format(
                        "The \"%s\" entrypoint has not been specified",
                        MAIN_ENTRYPOINT
                ));
            }

            Class<?> mainClass = Class.forName(mainEntrypoint, true, classLoader);
            JetPlugin plugin = new JetPlugin(pluginMetadata, injector.getInstance(mainClass), classLoader);

            Collection<String> authors = pluginMetadata.authors();
            String version = pluginMetadata.version();

            String pluginLoadedMessage = String.format(
                    "Loaded plugin \"%s\" with version of \"%s\"",
                    pluginName, version
            );

            if (!authors.isEmpty()) {
                StringJoiner joiner = new StringJoiner(", ");
                authors.forEach(joiner::add);
                pluginLoadedMessage = String.format("%s made by %s", pluginLoadedMessage, joiner);
            }

            LOGGER.info(pluginLoadedMessage);
            results.put(pluginName, new PluginLoadResult.Success(plugin));
            return true;
        } catch (Throwable throwable) {
            try {
                classLoader.close();
            } catch (IOException exception) {
                LOGGER.error("An error occurred while closing a plugin classloader", exception);
            }

            results.put(pluginName, PluginLoadResult.Fail.INSTANCE);
            LOGGER.error("An error occurred while loading a plugin with name of \"{}\"", pluginName, throwable);
            return false;
        }
    }

    /**
     * Loads a plugin of {@linkplain PluginDependency a plugin dependency}.
     *
     * @param dependency the plugin dependency
     * @param pluginName a name of plugin, which depends on the other plugin
     * @param pairs all plugin pairs detected
     * @param injector an injector to create plugin main class instances with
     * @param results results of loaded plugins, must be mutable
     * @param dependants names of plugins requiring the depending on plugin to be loaded
     * @return {@code true} if the plugin dependency has been loaded successfully, {@code false} otherwise
     * @since 1.0
     */
    private boolean loadDependency(@NonNull PluginDependency dependency, @NonNull String pluginName,
                                   @NonNull Map<String, PluginPair> pairs, @NonNull Injector injector,
                                   @NonNull Map<String, PluginLoadResult> results,
                                   @NonNull String @NonNull [] dependants) {
        String dependencyPluginName = dependency.pluginName();
        PluginPair dependencyPluginPair = pairs.get(dependencyPluginName);

        if (dependencyPluginPair == null) {
            throw new IllegalArgumentException(String.format(
                    "Plugin \"%s\" requires a dependency with name of \"%s\"",
                    pluginName, dependencyPluginName
            ));
        }

        String dependencyVersion = dependencyPluginPair.metadata().version();
        if (!isCompatible(dependencyVersion, dependency)) {
            String message = String.format(
                    "Version \"%s\" of dependency \"%s\" is incompatible with plugin \"%s\"",
                    dependencyVersion, dependencyPluginName, pluginName
            );

            if (dependency.required())
                throw new IllegalArgumentException(message);
            LOGGER.warn(message);
        }

        if (results.containsKey(dependencyPluginName))
            return results.get(dependencyPluginName) instanceof PluginLoadResult.Success;

        String[] newDependants = Arrays.copyOf(dependants, dependants.length + 1);
        newDependants[dependants.length] = pluginName;
        return this.load(dependencyPluginPair, pairs, results, injector, newDependants);
    }

    /**
     * Retrieves plugin files from {@linkplain #PLUGIN_PATH a plugin path} and creates
     * {@linkplain PluginPair plugin pairs} using them.
     *
     * @return a map of plugin names mapped to plugin pairs associated with them
     * @throws IOException when an IO error occurs while loading the plugin pairs
     * @since 1.0
     */
    private static @NonNull Map<String, PluginPair> loadPluginPairs() throws IOException {
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
                PluginPair pluginPair = null;

                try {
                    pluginPair = createPluginPair(path);
                    String pluginName = pluginPair.metadata().name();

                    if (pairs.containsKey(pluginName)) {
                        throw new IllegalArgumentException(String.format(
                                "Plugin with name of \"%s\" has been already registered",
                                pluginName
                        ));
                    }

                    pairs.put(pluginName, pluginPair);
                } catch (Throwable throwable) {
                    if (pluginPair != null)
                        pluginPair.classLoader().close();

                    LOGGER.error(
                            "An error occurred while creating a plugin with file name of \"{}\"",
                            fileName, throwable
                    );
                }
            }
        }

        return Map.copyOf(pairs);
    }

    /**
     * Creates {@linkplain PluginPair a plugin pair} for a plugin with {@linkplain Path path} specified.
     *
     * @param path the path
     * @return the plugin pair
     * @throws IOException when an IO error occurs while creating the plugin pair
     * @since 1.0
     */
    private static @NonNull PluginPair createPluginPair(@NonNull Path path) throws IOException {
        String fileName = path.getFileName().toString();
        URLClassLoader classLoader = null;

        try {
            classLoader = new URLClassLoader(
                    fileName, new URL[]{ path.toUri().toURL() },
                    JetPluginManager.class.getClassLoader()
            );

            PluginMetadata metadata;
            try (InputStream stream = classLoader.getResourceAsStream(METADATA_FILE_NAME)) {
                if (stream == null) {
                    throw new IllegalArgumentException(String.format(
                            "The plugin does not contain a file with name of \"%s\"",
                            METADATA_FILE_NAME
                    ));
                }
                metadata = PLUGIN_METADATA_GSON.fromJson(new InputStreamReader(stream), PluginMetadata.class);
            }

            return new PluginPair(metadata, classLoader);
        } catch (Throwable throwable) {
            if (classLoader != null)
                classLoader.close();
            throw throwable; // Re-throw the throwable, since it has not been handled
        }
    }

    private static boolean isCompatible(@NonNull String versionString, @NonNull PluginDependency dependency) {
        for (String version : dependency.versionsSupported())
            if (isVersionCompatible(versionString, version))
                return true;
        return false;
    }

    private static boolean isVersionCompatible(@NonNull String versionString, @NonNull String requiredVersion) {
        List<VersionPart> parsedVersion = PluginVersionParser.parseVersion(versionString);
        List<VersionPart> parsedRequiredVersion = PluginVersionParser.parseVersion(requiredVersion);

        int maxSize = Math.max(parsedVersion.size(), parsedRequiredVersion.size());

        for (int index = 0; index < maxSize; index++) {
            if (!(parsedVersion.get(index) instanceof NumberVersionPart part))
                throw new IllegalArgumentException("A plugin version string cannot contain non-number version parts");

            VersionPart requiredPart = parsedRequiredVersion.get(index);
            if (!requiredPart.supports(part))
                return false;
        }

        return true;
    }

    /**
     * Represents something that contains {@linkplain PluginMetadata a plugin metadata} and
     * {@linkplain URLClassLoader an URL class loader} of {@linkplain JetPlugin a plugin}, which has not been
     * initialized yet.
     *
     * @param metadata the metadata
     * @param classLoader the class loader
     * @since 1.0
     */
    private record PluginPair(@NonNull PluginMetadata metadata, @NonNull URLClassLoader classLoader) {
        /**
         * Constructs the {@linkplain PluginPair plugin pair}.
         *
         * @param metadata the metadata
         * @param classLoader the class loader
         * @since 1.0
         */
        private PluginPair {
            Objects.requireNonNull(metadata, "metadata");
            Objects.requireNonNull(classLoader, "class loader");
        }
    }

    /**
     * Represents result of loading {@linkplain JetPlugin a plugin}.
     *
     * @since 1.0
     * @see JetPlugin
     */
    private sealed interface PluginLoadResult {
        /**
         * Represents {@linkplain PluginLoadResult a plugin load result}, which is used when a plugin has been loaded
         * successfully.
         *
         * @param plugin the plugin that has been loaded
         * @since 1.0
         */
        record Success(@NonNull JetPlugin plugin) implements PluginLoadResult {
            /**
             * Constructs the {@linkplain Success success plugin load result}.
             *
             * @param plugin the plugin that has been loaded
             * @since 1.0
             */
            public Success {
                Objects.requireNonNull(plugin, "plugin");
            }
        }

        /**
         * Represents {@linkplain PluginLoadResult a plugin load result}, which is used when a plugin has not been
         * loaded successfully.
         *
         * @since 1.0
         */
        final class Fail implements PluginLoadResult {
            private static final Fail INSTANCE = new Fail();
            private Fail() {}
        }
    }
}