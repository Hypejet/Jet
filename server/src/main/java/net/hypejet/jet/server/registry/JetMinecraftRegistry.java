package net.hypejet.jet.server.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.ImmutableIntArray;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * An implementation of {@linkplain MinecraftRegistry Minecraft registry}.
 *
 * @param <V> a type of values available in this registry
 * @since 1.0
 * @see MinecraftRegistry
 */
public final class JetMinecraftRegistry<V> implements MinecraftRegistry<V> {

    private final Key registryKey;
    private final BinaryTagCodec<V> valueCodec;

    private final Object2IntMap<Key> keyToIndexMap;
    private final List<RegistrationInfo<V>> registrationInfos;

    private final ReadWriteLock tagsLock;
    private final Multimap<Key, Key> tagToKeysMultimap = HashMultimap.create();
    private final Multimap<Key, Key> keyToTagsMultimap = HashMultimap.create();

    /**
     * Constructs the {@linkplain JetMinecraftRegistry Minecraft registry implementation}.
     *
     * @param registryKey the key that the registry should have
     * @param registrationInfos an info list of registrations that the registry should have, the order is preserved
     * @param tags a multimap associating keys of tags with keys of registry
     *             values that should be associated with these tags
     * @param tagsLock a read-write lock that should be acquired while working with tags
     *                 of values of the registry that is being constructed
     * @param valueCodec a binary tag codec that values of the registry should be written with, {@code null} if
     *                   values of the registry should not be able to be written to network
     * @since 1.0
     */
    public JetMinecraftRegistry(@NonNull Key registryKey, @NonNull List<RegistrationInfo<V>> registrationInfos,
                                @NonNull Multimap<Key, Key> tags, @NonNull ReadWriteLock tagsLock,
                                @Nullable BinaryTagCodec<V> valueCodec) {
        this.registryKey = Objects.requireNonNull(registryKey, "registry key");

        Objects.requireNonNull(registrationInfos, "registration infos");
        this.keyToIndexMap = createKeyToIndexMap(registrationInfos);
        this.registrationInfos = List.copyOf(registrationInfos);
        this.valueCodec = valueCodec;

        this.tagsLock = Objects.requireNonNull(tagsLock, "tags lock");
        this.updateTags(Objects.requireNonNull(tags, "tags"));
    }

    @Override
    public @Nullable V get(@NonNull Key key) {
        Objects.requireNonNull(key, "key");
        if (!this.keyToIndexMap.containsKey(key)) return null;
        return this.registrationInfos.get(this.keyToIndexMap.getInt(key)).value();
    }

    @Override
    public @NonNull Set<Key> keySet() {
        return this.keyToIndexMap.keySet();
    }

    @Override
    public @NonNull Set<Key> tagsFor(@NonNull Key key) {
        try {
            this.tagsLock.readLock().lock();
            return Set.copyOf(this.keyToTagsMultimap.get(this.ensureRegistered(key)));
        } finally {
            this.tagsLock.readLock().unlock();
        }
    }

    /**
     * Gets a registry index of a registry value associated with the specified {@linkplain Key key}.
     *
     * @param key the key of the registry value
     * @return the registry index
     * @throws IllegalArgumentException if no registry value is associated with the specified key
     * @since 1.0
     */
    public int indexOf(@NonNull Key key) {
        return this.keyToIndexMap.getInt(this.ensureRegistered(key));
    }

    /**
     * Gets {@linkplain List list} of {@linkplain RegistrationInfo registration infos} of all registrations
     * from this {@linkplain JetMinecraftRegistry registry} with preserved order.
     *
     * @return the registration-info list
     * @since 1.0
     */
    public @NonNull List<RegistrationInfo<V>> registrationInfos() {
        return this.registrationInfos;
    }

    /**
     * Gets a {@linkplain Key key} of this {@linkplain JetMinecraftRegistry registry}.
     *
     * @return the registry key
     * @since 1.0
     */
    public @NonNull Key registryKey() {
        return this.registryKey;
    }

    /**
     * Gets a {@linkplain BinaryTagCodec binary-tag codec} that should write
     * values of this {@linkplain JetMinecraftRegistry registry}.
     *
     * @return the binary-tag codec, {@code null} if values of this registry are not network-serializable
     * @since 1.0
     */
    public @Nullable BinaryTagCodec<V> valueCodec() {
        return this.valueCodec;
    }

    /**
     * Updates tags of this {@linkplain JetMinecraftRegistry registry}
     * with the specified {@linkplain Multimap multimap} associating {@linkplain Key keys} of tags
     * with {@linkplain Key keys} of registry values that should be associated with these tags.
     *
     * @param tagToKeysMultimap the tag multimap
     * @since 1.0
     */
    void updateTags(@NonNull Multimap<Key, Key> tagToKeysMultimap) {
        this.tagToKeysMultimap.clear();
        this.keyToTagsMultimap.clear();
        tagToKeysMultimap.forEach((tag, key) -> {
            this.ensureRegistered(key);
            this.tagToKeysMultimap.put(tag, key);
            this.keyToTagsMultimap.put(key, tag);
        });
    }

    /**
     * Creates a {@linkplain ServerUpdateTagsPacket.TagRegistry tag registry} containing tags of values
     * registered in this {@linkplain JetMinecraftRegistry registry}.
     *
     * @return the created tag registry
     * @since 1.0
     */
    ServerUpdateTagsPacket.@NonNull TagRegistry createTagRegistry() {
        Set<ServerUpdateTagsPacket.Tag> tags = new HashSet<>();
        for (Key tag : this.tagToKeysMultimap.keySet()) {
            ImmutableIntArray.Builder builder = ImmutableIntArray.builder();
            this.tagToKeysMultimap.get(tag).forEach(key -> builder.add(this.indexOf(key)));
            tags.add(new ServerUpdateTagsPacket.Tag(tag, builder.build()));
        }
        return new ServerUpdateTagsPacket.TagRegistry(this.registryKey, tags);
    }

    private @NonNull Key ensureRegistered(@NonNull Key key) {
        Objects.requireNonNull(key, "key");
        if (!this.keyToIndexMap.containsKey(key))
            throw new IllegalArgumentException("This registry does not contain a value for \"" + key + "\" key");
        return key;
    }

    private static <V> @NonNull Object2IntMap<Key> createKeyToIndexMap(
            @NonNull List<RegistrationInfo<V>> registrationInfos
    ) {
        Object2IntMap<Key> keyToIndexMap = new Object2IntOpenHashMap<>();

        for (int index = 0; index < registrationInfos.size(); index++) {
            RegistrationInfo<V> registrationInfo = registrationInfos.get(index);
            Key key = registrationInfo.key();

            if (keyToIndexMap.containsKey(key)) {
                throw new IllegalArgumentException(String.format(
                        "The registry already has a registration info for \"%s\" key",
                        key
                ));
            }

            keyToIndexMap.put(key, index);
        }

        return Object2IntMaps.unmodifiable(keyToIndexMap);
    }

    /**
     * An information about a registration that should be or was done in a {@linkplain JetMinecraftRegistry registry}.
     *
     * @param key the key that the registration info should be bound to
     * @param value a registry value that should be associated with the key
     * @param knownPack an information about a feature pack that can enable the registration without sending
     *                  the entire registration info to the client, {@code null} if there is no such a feature pack
     * @param <V> the type of value that should be associated with the key
     * @since 1.0
     */
    public record RegistrationInfo<V>(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack) {
        /**
         * Constructs the {@linkplain RegistrationInfo registration info}.
         *
         * @param key the key that the created registration info should be bound to
         * @param value a registry value that should be associated with the key
         * @param knownPack an information about a feature pack that should be able to enable the registration
         *                  (that the registration info is constructed for) without sending the encoded value
         *                  to the client, {@code null} if there is no such a feature pack
         * @since 1.0
         */
        public RegistrationInfo {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");
        }
    }
}