package net.hypejet.jet.plugin.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a plugin version of a {@linkplain PluginDependency plugin dependency}.
 *
 * @param name a name of the version
 * @param backwardsCompatible whether all previous versions should also be supported
 * @param forwardCompatible whether all newer versions should also be supported
 * @since 1.0
 * @author Codestech
 */
public record PluginVersion(@NonNull String name, boolean backwardsCompatible, boolean forwardCompatible) {}