package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which updates tag registries on
 * a client.
 *
 * @param registries the new registries
 * @since 1.0
 * @author Codestech
 * @see TagRegistry
 * @see Tag
 */
public record ServerUpdateTagsConfigurationPacket(@NonNull Collection<TagRegistry> registries)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerUpdateTagsConfigurationPacket update tags configuration packet}.
     *
     * @param registries the new registries
     * @since 1.0
     */
    public ServerUpdateTagsConfigurationPacket {
        registries = List.copyOf(registries);
    }

    /**
     * Represents a tag registry used by the {@linkplain ServerUpdateTagsConfigurationPacket update tags configuration
     * packet}.
     *
     * @param identifier an identifier of the registry
     * @param tags tags, which the registry should hold
     * @since 1.0
     * @author Codestech
     * @see Tag
     * @see ServerUpdateTagsConfigurationPacket
     */
    public record TagRegistry(@NonNull Key identifier, @NonNull Collection<Tag> tags) {
        /**
         * Constructs the {@linkplain TagRegistry tag registry}.
         *
         * @param identifier an identifier of the registry
         * @param tags tags, which the registry should hold
         * @since 1.0
         */
        public TagRegistry {
            tags = List.copyOf(tags);
        }
    }

    /**
     * Represents a tag used by the {@linkplain TagRegistry tag registry}.
     *
     * @param identifier an identifier of the tag
     * @param entries numeric IDs of data with type that this tag supports (block, items, etc.)
     * @since 1.0
     * @author Codestech
     * @see TagRegistry
     * @see ServerUpdateTagsConfigurationPacket
     */
    public record Tag(@NonNull Key identifier, int @NonNull [] entries) {}
}