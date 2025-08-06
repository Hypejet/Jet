package net.hypejet.jet.server.registry;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * An implementation of {@linkplain MinecraftRegistry Minecraft registry}.
 *
 * @param <V> a type of values available in this registry
 * @since 1.0
 * @see MinecraftRegistry
 */
public final class JetMinecraftRegistry<V> implements MinecraftRegistry<V> {

    private final Object2IntMap<Key> keyToIndexMap;
    private final Map<Key, Set<Key>> tags = new HashMap<>();

    private final List<RegistrationInfo<V>> registrationInfos;
    private final NetworkableData<V> networkableData;

    /**
     * Constructs the {@linkplain JetMinecraftRegistry Minecraft registry implementation}.
     *
     * @param registrationInfos an info list of registrations that the registry should have, the order is preserved
     * @param tags a map associating keys of registry values with keys of tags that these registry values should have
     * @param networkableData an additional data that the registry should have for network writing
     *                        purposes, {@code null} if values of the registry should not be able
     *                        to be written to network
     * @since 1.0
     */
    public JetMinecraftRegistry(@NonNull List<RegistrationInfo<V>> registrationInfos, @NonNull Map<Key, Set<Key>> tags,
                                @Nullable NetworkableData<V> networkableData) {
        Objects.requireNonNull(registrationInfos, "registration infos");
        Objects.requireNonNull(tags, "tags");

        this.keyToIndexMap = createKeyToIndexMap(registrationInfos);
        this.registrationInfos = List.copyOf(registrationInfos);
        this.networkableData = networkableData;

        this.tags.putAll(tags);
        this.tags.replaceAll((key, tagKeys) -> Set.copyOf(tagKeys));
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
        return this.tags.get(this.ensureRegistered(key));
    }

    @Override
    public void updateTags(@NonNull Key key, @NonNull UnaryOperator<Set<Key>> tagUnaryOperator) {
        Objects.requireNonNull(tagUnaryOperator, "tag unary operator");
        this.tags.compute(this.ensureRegistered(key), (ignored, tagSet) -> {
            Set<Key> updatedTagSet = tagUnaryOperator.apply(tagSet == null ? Set.of() : Set.copyOf(tagSet));
            if (updatedTagSet.isEmpty()) return null;
            return Set.copyOf(updatedTagSet);
        });
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
        return registrationInfos;
    }

    /**
     * Gets an additional data that this registry should have for network writing purposes.
     *
     * @return the additional data, {@code null} if values of this registry are not network-serializable
     * @since 1.0
     */
    public @Nullable NetworkableData<V> networkableData() {
        return this.networkableData;
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
     * An additional data of a {@linkplain JetMinecraftRegistry registry}
     * available when its can be written to a network.
     *
     * @param valueCodec a binary tag codec to write the registry values with
     * @param registryKey a key of the registry that this data was created for
     * @param <V> the value type of the registry
     * @since 1.0
     */
    public record NetworkableData<V>(@NonNull BinaryTagCodec<V> valueCodec, @NonNull Key registryKey) {
        /**
         * Constructs the {@linkplain NetworkableData networkable registry data}.
         *
         * @param valueCodec a binary tag codec to write the registry values with
         * @param registryKey a key of the registry that the data is constructed for
         * @since 1.0
         */
        public NetworkableData {
            Objects.requireNonNull(valueCodec, "value codec");
            Objects.requireNonNull(registryKey, "registry key");
        }
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