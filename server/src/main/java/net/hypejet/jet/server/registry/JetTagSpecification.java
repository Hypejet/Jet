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
public record JetTagSpecification(@NonNull Collection<Key> tags) {
    /**
     * Constructs the {@linkplain JetTagSpecification tag specification}.
     *
     * @param tags a collection of the tags
     * @since 1.0
     */
    public JetTagSpecification {
        tags = Set.copyOf(tags);
    }

    /**
     * Gets whether a tag has been attached to the {@linkplain JetRegistryEntry registry entry}.
     *
     * @param key a key of the tag
     * @return {@code true} if the tag has been attached to the registry entry, {@code false} otherwise
     */
    public boolean hasTag(@NonNull Key key) {
        return this.tags.contains(key);
    }
}