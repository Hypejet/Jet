package net.hypejet.jet.server.plugin;

import java.util.Objects;
import net.hypejet.jet.plugin.Plugin;
import net.hypejet.jet.plugin.dependency.PluginDependency;
import net.hypejet.jet.server.plugin.metadata.PluginMetadata;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of the {@linkplain Plugin plugin}.
 *
 * @since 1.0
 */
public final class JetPlugin implements Plugin {

    private final PluginMetadata metadata;
    private final Object instance;

    private final URLClassLoader classLoader;

    /**
     * Constructs the {@linkplain JetPlugin plugin}.
     *
     * @param metadata a metadata that the plugin should have
     * @param instance an instance that should be associated with the plugin
     * @param classLoader a class loader that the plugin has been loaded with
     * @since 1.0
     */
    public JetPlugin(@NonNull PluginMetadata metadata, @NonNull Object instance, @NonNull URLClassLoader classLoader) {
        this.metadata = Objects.requireNonNull(metadata, "metadata");
        this.instance = Objects.requireNonNull(instance, "instance");
        this.classLoader = Objects.requireNonNull(classLoader, "class loader");
    }

    @Override
    public @NonNull String name() {
        return this.metadata.name();
    }

    @Override
    public @NonNull String version() {
        return this.metadata.version();
    }

    @Override
    public @NonNull Set<String> authors() {
        return this.metadata.authors();
    }

    @Override
    public @NonNull Set<PluginDependency> dependencies() {
        return this.metadata.dependencies();
    }

    @Override
    public @Nullable String getEntrypoint(@NonNull Key key) {
        return this.metadata.entrypoints().get(key);
    }

    @Override
    public @NonNull Object instance() {
        return this.instance;
    }

    /**
     * Gets {@linkplain URLClassLoader an URL class loader}, which was used to load the plugin.
     *
     * @return the URL class loader
     * @since 1.0
     */
    public @NonNull URLClassLoader classLoader() {
        return this.classLoader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JetPlugin plugin)) return false;
        return Objects.equals(this.metadata, plugin.metadata)
                && Objects.equals(this.instance, plugin.instance)
                && Objects.equals(this.classLoader, plugin.classLoader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.metadata, this.instance, this.classLoader);
    }

    @Override
    public String toString() {
        return "JetPlugin{" +
                "metadata=" + this.metadata +
                '}';
    }
}