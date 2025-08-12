package net.hypejet.jet.server.network.packet.packets.server.common;

import com.google.common.primitives.ImmutableIntArray;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet}, which performs an update
 * of {@linkplain TagRegistry tag registries} on client to synchronize them with the server.
 *
 * @param registries the tag registries
 * @since 1.0
 * @see TagRegistry
 * @see ServerPacket
 */
public record ServerUpdateTagsPacket(@NonNull Collection<TagRegistry> registries) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateTagsPacket server update tags packet}.
     *
     * @param registries the tag registries
     * @since 1.0
     */
    public ServerUpdateTagsPacket {
        registries = Set.copyOf(Objects.requireNonNull(registries, "registries"));
    }

    /**
     * Represents something that holds {@linkplain Tag tags} of
     * {@linkplain net.hypejet.jet.registry.MinecraftRegistry a Minecraft registry}.
     *
     * @param key a key of the registry
     * @param tags the tags
     * @since 1.0
     * @see net.hypejet.jet.registry.MinecraftRegistry
     * @see Tag
     */
    public record TagRegistry(@NonNull Key key, @NonNull Collection<Tag> tags) {
        /**
         * Constructs the {@linkplain TagRegistry tag registry}.
         *
         * @param key a key of the registry
         * @param tags the tags
         * @since 1.0
         */
        public TagRegistry {
            Objects.requireNonNull(key, "key");
            tags = Set.copyOf(Objects.requireNonNull(tags, "tags"));
        }
    }

    /**
     * Represents a tag of {@linkplain MinecraftRegistry registry} values.
     *
     * @param key the key of the tag
     * @param entries an array of indices of registry values that are associated with this tag
     * @since 1.0
     * @see MinecraftRegistry
     */
    public record Tag(@NonNull Key key, @NonNull ImmutableIntArray entries) {
        /**
         * Constructs the {@linkplain Tag tag}.
         *
         * @param key the key that the tag should have
         * @param entries an array of indices of registry values that should be associated with the constructed tag
         * @since 1.0
         */
        public Tag {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(entries, "entries");
        }
    }
}