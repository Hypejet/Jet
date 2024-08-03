package net.hypejet.jet.pack;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft data pack.
 *
 * @param key an identifier of the data pack
 * @param version a version of the data pack
 * @since 1.0
 * @author Codestech
 */
public record DataPack(@NonNull Key key, @NonNull String version) implements Keyed {}