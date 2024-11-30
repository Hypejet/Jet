package net.hypejet.jet.network.packet.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet}, which performs an update
 * of {@linkplain TagRegistry tag registries} on client to synchronize with the server.
 *
 * @param registries the tag registries
 * @since 1.0
 * @author Codestech
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
        registries = Set.copyOf(NullabilityUtil.requireNonNull(registries, "registries"));
    }

    /**
     * Represents something that hold {@linkplain Tag tags} of
     * {@linkplain net.hypejet.jet.registry.MinecraftRegistry a Minecraft registry}.
     *
     * @param identifier an key of the registry
     * @param tags the tags
     * @since 1.0
     * @see net.hypejet.jet.registry.MinecraftRegistry
     * @see Tag
     */
    public record TagRegistry(@NonNull Key identifier, @NonNull Collection<Tag> tags) {
        /**
         * Constructs the {@linkplain TagRegistry tag registry}.
         *
         * @param identifier an key of the registry
         * @param tags the tags
         * @since 1.0
         */
        public TagRegistry {
            tags = Set.copyOf(NullabilityUtil.requireNonNull(tags, "tags"));
        }
    }

    /**
     * Represents a tag of {@linkplain net.hypejet.jet.registry.RegistryEntry registry entries}.
     *
     * @param key a key of the tag
     * @param entries numeric identifiers of the registry entries
     * @since 1.0
     * @see net.hypejet.jet.registry.RegistryEntry
     */
    public record Tag(@NonNull Key key, @NonNull UnmodifiableIntegerArray entries) {
        /**
         * Constructs the {@linkplain Tag tag}.
         *
         * @param key a key of the tag
         * @param entries numeric IDs of data with type that this tag supports (block, items, etc.)
         * @since 1.0
         */
        public Tag(@NonNull Key key, int @NonNull [] entries) {
            this(key, new UnmodifiableIntegerArray(entries));
        }

        /**
         * Constructs the {@linkplain Tag tag}.
         *
         * @param key a key of the tag
         * @param entries numeric IDs of data with type that this tag supports (block, items, etc.)
         * @since 1.0
         */
        public Tag {
            NullabilityUtil.requireNonNull(key, "key");
            NullabilityUtil.requireNonNull(entries, "entries");
        }
    }
}