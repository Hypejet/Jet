package net.hypejet.jet.server.registry;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * An implementation of {@linkplain MinecraftRegistry Minecraft registry}.
 *
 * @param <V> a type of entry values of this registry
 * @since 1.0
 * @see MinecraftRegistry
 */
public final class JetMinecraftRegistry<V> implements MinecraftRegistry<V> {

    private final Map<Key, JetRegistryEntry<V>> keyToEntryMap;
    private final Object2IntMap<JetRegistryEntry<V>> entryToIdentifierMap;
    private final List<JetRegistryEntry<V>> entries;
    private final NetworkableData<V> networkableData;

    private final Map<JetRegistryEntry<V>, Set<Key>> tags = new HashMap<>();

    /**
     * Constructs the {@linkplain JetMinecraftRegistry Minecraft registry implementation}.
     *
     * @param entries a list of registry entries that the registry should have, the order is preserved
     * @param tags a map associating registry entries with keys of tags that these registry entries should have
     * @param networkableData an additional data that the registry should have for network writing
     *                        purposes, {@code null} if values of the registry should not be able
     *                        to be written to network
     * @since 1.0
     */
    public JetMinecraftRegistry(@NonNull List<JetRegistryEntry<V>> entries,
                                @NonNull Map<JetRegistryEntry<V>, Set<Key>> tags,
                                @Nullable NetworkableData<V> networkableData) {
        Objects.requireNonNull(entries, "entries");
        Objects.requireNonNull(tags, "tags");

        Map<Key, JetRegistryEntry<V>> keyToEntryMap = new HashMap<>();
        Object2IntMap<JetRegistryEntry<V>> entryToIdentifierMap = new Object2IntOpenHashMap<>();

        for (int index = 0; index < entries.size(); index++) {
            JetRegistryEntry<V> entry = entries.get(index);
            keyToEntryMap.put(entry.key(), entry);
            entryToIdentifierMap.put(entry, index);
        }

        this.keyToEntryMap = Map.copyOf(keyToEntryMap);
        this.entryToIdentifierMap = Object2IntMaps.unmodifiable(entryToIdentifierMap);
        this.entries = List.copyOf(entries);
        this.networkableData = networkableData;

        this.tags.putAll(tags);
        this.tags.replaceAll((entry, keys) -> Set.copyOf(keys));
    }

    @Override
    public @Nullable JetRegistryEntry<V> get(@NonNull Key key) {
        return this.keyToEntryMap.get(key);
    }

    @Override
    public @NonNull List<JetRegistryEntry<V>> entries() {
        return this.entries;
    }

    @Override
    public boolean hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag) {
        return this.tagsFor(entry).contains(tag);
    }

    @Override
    public @NonNull Set<Key> tagsFor(@NonNull RegistryEntry<V> entry) {
        return this.tags.get(this.ensureRegistered(entry));
    }

    @Override
    public void updateTags(@NonNull RegistryEntry<V> entry,
                                 @NonNull UnaryOperator<Set<Key>> tagUnaryOperator) {
        this.tags.compute(this.ensureRegistered(entry), (ignored, tagSet) -> {
            Set<Key> updatedTagSet = tagUnaryOperator.apply(tagSet == null ? Set.of() : Set.copyOf(tagSet));
            if (updatedTagSet.isEmpty()) return null;
            return Set.copyOf(updatedTagSet);
        });
    }

    /**
     * Gets an entry-list index of the specified {@linkplain JetRegistryEntry registry entry}.
     *
     * @param entry the registry entry
     * @return the entry-list index
     * @throws IllegalArgumentException if this registry does not contain the specified registry entry
     * @since 1.0
     */
    public int indexOf(@NonNull JetRegistryEntry<V> entry) {
        this.ensureRegistered(entry);
        return this.entryToIdentifierMap.getInt(entry);
    }

    /**
     * Gets a {@linkplain Map map} associating {@linkplain JetRegistryEntry registry entries}
     * with keys of tags that these registry entries should have.
     *
     * @return the tag  map
     * @since 1.0
     */
    public @NonNull Map<JetRegistryEntry<V>, Set<Key>> tags() {
        return Map.copyOf(this.tags);
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

    private @NonNull JetRegistryEntry<V> ensureRegistered(@NonNull RegistryEntry<V> entry) {
        if (!(entry instanceof JetRegistryEntry<V> validatedEntry))
            throw new IllegalArgumentException("The specified registry entry is not a valid registry entry");
        if (!this.entryToIdentifierMap.containsKey(entry))
            throw new IllegalArgumentException("This registry does not contain \"" + entry.key() + "\" entry");
        return validatedEntry;
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
}