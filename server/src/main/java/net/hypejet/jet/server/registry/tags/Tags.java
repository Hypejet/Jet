package net.hypejet.jet.server.registry.tags;

import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a specification of tags attached to {@linkplain JetRegistryEntry a registry entry}.
 *
 * @param tags a collection of the tags, which have been attached to the registry entry
 * @since 1.0
 * @see JetRegistryEntry
 */
public record Tags(@NonNull Collection<Key> tags) {
    /**
     * Constructs the {@linkplain Tags tags}.
     *
     * @param tags a collection of the tags
     * @since 1.0
     */
    public Tags {
        tags = Set.copyOf(Objects.requireNonNull(tags, "tags"));
    }
}