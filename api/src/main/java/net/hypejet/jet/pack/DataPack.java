package net.hypejet.jet.pack;

import net.kyori.adventure.key.Namespaced;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft data pack.
 *
 * @param namespace a namespace of the data pack
 * @param identifier an identifier of the data pack
 * @param version a version of the data pack
 */
public record DataPack(@NonNull String namespace, @NonNull String identifier, @NonNull String version)
        implements Namespaced {}