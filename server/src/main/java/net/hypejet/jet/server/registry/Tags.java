package net.hypejet.jet.server.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a specification of tags attached to a {@linkplain JetRegistryEntry registry entry}.
 *
 * @param tags a collection of the tags, which have been attached to the registry entry
 * @since 1.0
 * @author Codestech
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
        tags = Set.copyOf(tags);
    }
}