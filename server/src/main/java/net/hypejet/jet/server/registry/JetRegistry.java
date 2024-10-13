package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a registry, whose values are identified with a {@linkplain Key key}.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @author Codestech
 */
public class JetRegistry<V> {

    private final Class<V> entryValueClass;

    private final Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap;
    private final Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap;

    private final List<JetRegistryEntry<V>> sortedEntries;
    private final Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap;

    private final Map<JetRegistryEntry<V>, JetTagSpecification> entryToTagSpecificationMap;

    /**
     * Constructs the {@linkplain JetSerializableMinecraftRegistry Minecraft registry}.
     *
     * @param entryValueClass a class of values of entries of the registry
     * @param server a server, on which the registry is registered
     * @param entries a collection of registry entries, which should be put into the registry
     * @param enabledFeaturePacks feature packs, which are enabled on the server
     * @since 1.0
     */
    public JetRegistry(@NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
                       @NonNull List<JetRegistryEntry<V>> entries, @NonNull Set<FeaturePack> enabledFeaturePacks,
                       @NonNull Map<JetRegistryEntry<V>, JetTagSpecification> entryTagSpecification) {
        NullabilityUtil.requireNonNull(server, "server");
        NullabilityUtil.requireNonNull(entries, "entries");

        this.entryValueClass = NullabilityUtil.requireNonNull(entryValueClass, "entry value class");

        Set<PackInfo> enabledFeaturePackInfos = new HashSet<>();
        for (FeaturePack enabledDataPack : enabledFeaturePacks)
            enabledFeaturePackInfos.add(enabledDataPack.info());

        Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap = new HashMap<>();
        Map<JetRegistryEntry<V>, JetTagSpecification> entryToTagSpecificationMap = new HashMap<>();

        List<JetRegistryEntry<V>> sortedEntries = new ArrayList<>();

        for (JetRegistryEntry<V> entry : entries) {
            Key key = entry.key();
            V value = entry.value();

            PackInfo knownPackInfo = entry.knownPackInfo();
            if (knownPackInfo != null && !enabledFeaturePackInfos.contains(knownPackInfo)) continue;

            keyToRegistryEntryMap.put(entry.key(), new JetRegistryEntry<>(key, value, knownPackInfo));

            JetTagSpecification tagSpecification = entryTagSpecification.get(entry);
            entryToTagSpecificationMap.put(entry, tagSpecification);
            sortedEntries.add(entry);
        }

        this.keyToRegistryEntryMap = Map.copyOf(keyToRegistryEntryMap);
        this.sortedEntries = List.copyOf(sortedEntries);

        Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap = new HashMap<>();
        for (int index = 0; index < sortedEntries.size(); index++)
            registryEntryToIdentifierMap.put(sortedEntries.get(index), index);
        this.registryEntryToIdentifierMap = Map.copyOf(registryEntryToIdentifierMap);

        Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap = new HashMap<>();
        for (Map.Entry<Key, JetRegistryEntry<V>> entry : keyToRegistryEntryMap.entrySet())
            registryEntryToKeyMap.put(entry.getValue(), entry.getKey());

        this.registryEntryToKeyMap = Map.copyOf(registryEntryToKeyMap);
        this.entryToTagSpecificationMap = Map.copyOf(entryToTagSpecificationMap);
    }

    /**
     * Gets a {@linkplain Class class} of values of entries of this {@linkplain JetRegistry registry}.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<V> entryValueClass() {
        return this.entryValueClass;
    }

    /**
     * Gets a registry entry by an identifier of it.
     *
     * @param identifier the identifier
     * @return the registry entry
     * @since 1.0
     */
    public @Nullable JetRegistryEntry<V> get(@NonNull Key identifier) {
        NullabilityUtil.requireNonNull(identifier, "identifier");
        return this.keyToRegistryEntryMap.get(identifier);
    }

    /**
     * Gets a registry entry by a numeric identifier of it.
     *
     * @param numericIdentifier the numeric identifier
     * @return the registry entry
     * @since 1.0
     */
    public @Nullable RegistryEntry<V> get(int numericIdentifier) {
        return this.sortedEntries.get(numericIdentifier);
    }

    /**
     * Gets whether a registry entry specified was registered in this {@linkplain JetRegistry registry}.
     *
     * @param entry the registry entry
     * @return {@code true} if the registry entry was registered in this registry, {@code false} otherwise
     * @since 1.0
     */
    public boolean isRegistered(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return this.registryEntryToKeyMap.containsKey(entry);
    }

    /**
     * Gets a {@linkplain Key key} of a registry entry registered in this {@linkplain JetRegistry registry}.
     *
     * @param entry the registry entry
     * @return the identifier
     * @since 1.0
     */
    public @NonNull Key keyOf(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return Objects.requireNonNull(this.registryEntryToKeyMap.get(entry),
                String.format("The registry entry %s was not registered in this registry", entry));
    }

    /**
     * Gets a numeric identifier of a registry entry registered in this {@link MinecraftRegistry registry}.
     *
     * @param entry the registry entry
     * @return a numeric identifier of the registry entry
     * @since 1.0
     */
    public int identifierOf(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return this.registryEntryToIdentifierMap.get(entry);
    }

    /**
     * Gets a {@linkplain List list} of all entries registered in this {@linkplain MinecraftRegistry registry}.
     *
     * <p>The entries are sorted by their numeric identifier.</p>
     *
     * @return the list
     * @since 1.0
     */
    public @NonNull List<JetRegistryEntry<V>> entries() {
        return this.sortedEntries;
    }

    /**
     * Gets a {@linkplain Map map}, which maps {@linkplain Key keys} to {@linkplain JetRegistryEntry registry entries}.
     *
     * @return the map
     * @since 1.0
     */
    public @NonNull Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap() {
        return Map.copyOf(this.keyToRegistryEntryMap);
    }

    /**
     * Gets whether a {@linkplain RegistryEntry registry entry} specified has a tag specified attached.
     *
     * @param entry the registry entry
     * @param tag the tag
     * @return {@code true} if the registry entry specified has a tag specified attached, {@code false} otherwise
     * @since 1.0
     */
    public boolean hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag) {
        return this.tagsFor(entry).contains(NullabilityUtil.requireNonNull(tag, "tag"));
    }

    /**
     * Gets a {@linkplain Collection collection} of tags attached to a {@linkplain RegistryEntry registry entry}
     * specified.
     *
     * @param entry the registry entry
     * @return the collection
     * @since 1.0
     */
    public @NonNull Collection<Key> tagsFor(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return this.entryToTagSpecificationMap.get(entry).tags();
    }
}