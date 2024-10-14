package net.hypejet.jet.server.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.registry.DataRegistryEntry;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of a {@linkplain MinecraftRegistry Minecraft registry}.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @author Codestech
 */
public class JetMinecraftRegistry<V> implements MinecraftRegistry<V> {

    private final Key registryKey;
    private final Class<V> entryValueClass;

    private final Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap;
    private final Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap;

    private final List<JetRegistryEntry<V>> sortedEntries;
    private final Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap;

    private final Map<JetRegistryEntry<V>, JetTagSpecification> entryToTagSpecificationMap;

    /**
     * Constructs the {@linkplain JetSerializableMinecraftRegistry Minecraft registry}.
     *
     * @param registryKey a key of the registry
     * @param entryValueClass a class of values of entries of the registry
     * @param server a server, on which the registry is registered
     * @param entries a collection of registry entries, which should be put into the registry
     * @param enabledFeaturePacks feature packs, which are enabled on the server
     * @param entryTagSpecification tag specifications of the registry entries specified
     * @since 1.0
     */
    protected JetMinecraftRegistry(@NonNull Key registryKey, @NonNull Class<V> entryValueClass,
                                @NonNull JetMinecraftServer server, @NonNull List<JetRegistryEntry<V>> entries,
                                @NonNull Set<FeaturePack> enabledFeaturePacks,
                                @NonNull Map<JetRegistryEntry<V>, JetTagSpecification> entryTagSpecification) {
        NullabilityUtil.requireNonNull(server, "server");
        NullabilityUtil.requireNonNull(entries, "entries");
        NullabilityUtil.requireNonNull(enabledFeaturePacks, "enabled feature packs");
        NullabilityUtil.requireNonNull(entryTagSpecification, "entry tag specification");

        this.registryKey = NullabilityUtil.requireNonNull(registryKey, "key");
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

    @Override
    public @NonNull Key registryKey() {
        return this.registryKey;
    }

    @Override
    public @NonNull Class<V> entryValueClass() {
        return this.entryValueClass;
    }

    @Override
    public @Nullable JetRegistryEntry<V> get(@NonNull Key identifier) {
        return this.keyToRegistryEntryMap.get(NullabilityUtil.requireNonNull(identifier, "identifier"));
    }

    @Override
    public @Nullable RegistryEntry<V> get(int numericIdentifier) {
        return this.sortedEntries.get(numericIdentifier);
    }

    @Override
    public boolean isRegistered(@NonNull RegistryEntry<V> entry) {
        return this.registryEntryToKeyMap.containsKey(validateEntry(entry));
    }

    @Override
    public @NonNull Key keyOf(@NonNull RegistryEntry<V> entry) {
        return Objects.requireNonNull(this.registryEntryToKeyMap.get(validateEntry(entry)),
                String.format("The registry entry %s was not registered in this registry", entry));
    }

    @Override
    public int identifierOf(@NonNull RegistryEntry<V> entry) {
        return this.registryEntryToIdentifierMap.get(validateEntry(entry));
    }

    @Override
    public @NonNull List<JetRegistryEntry<V>> entries() {
        return this.sortedEntries;
    }

    @Override
    public boolean hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag) {
        return this.tagsFor(entry).contains(NullabilityUtil.requireNonNull(tag, "tag"));
    }

    @Override
    public @NonNull Collection<Key> tagsFor(@NonNull RegistryEntry<V> entry) {
        return this.entryToTagSpecificationMap.get(validateEntry(entry)).tags();
    }

    /**
     * Creates a {@linkplain ServerUpdateTagsConfigurationPacket.TagRegistry tag registry} using tags attached
     * to entries from this registry.
     *
     * @return the tag registry
     * @since 1.0
     */
    public ServerUpdateTagsConfigurationPacket.@NonNull TagRegistry createTagRegistry() {
        Map<Key, TagBuilder> tagMap = new HashMap<>();
        for (JetRegistryEntry<V> entry : this.entries())
            for (Key tagKey : this.tagsFor(entry))
                tagMap.computeIfAbsent(tagKey, key -> new TagBuilder()).add(this.identifierOf(entry));

        Collection<ServerUpdateTagsConfigurationPacket.Tag> tags = new HashSet<>();
        tagMap.forEach((key, builder) -> tags.add(builder.toPacketTag(key)));
        return new ServerUpdateTagsConfigurationPacket.TagRegistry(this.registryKey, Set.copyOf(tags));
    }
    /**
     * Creates a {@linkplain JetMinecraftRegistry Minecraft registry}.
     *
     * @param registryKey a key of the registry
     * @param entryValueClass a class of values of registry entries
     * @param server a server that should own registry
     * @param enabledFeaturePacks a set of feature packs, which are enabled on the server
     * @param gson a gson, which deserializes the built-in registry entries from a resource file
     * @param resourceFileName a name of the resource file
     * @return the serializable Minecraft registry
     * @param <V> a type of values of the registry entries
     * @since 1.0
     */
    public static <V> @NonNull JetMinecraftRegistry<V> create(
            @NonNull Key registryKey, @NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
            @NonNull Set<FeaturePack> enabledFeaturePacks, @NonNull Gson gson, @NonNull String resourceFileName
    ) {
        List<JetRegistryEntry<V>> entries = new ArrayList<>();
        Map<JetRegistryEntry<V>, JetTagSpecification> entryToTagSpecificationMap = new HashMap<>();

        for (DataRegistryEntry<?> dataEntry : JetMinecraftRegistry.entries(gson, resourceFileName)) {
            if (!entryValueClass.isAssignableFrom(dataEntry.value().getClass()))
                throw new IllegalArgumentException("The data registry entry has incompatible value");

            JetRegistryEntry<V> registryEntry = new JetRegistryEntry<>(dataEntry.key(),
                    entryValueClass.cast(dataEntry.value()), dataEntry.knownPackInfo());

            JetTagSpecification specification = null;
            Collection<Key> tags = dataEntry.tags();

            if (tags != null)
                specification = new JetTagSpecification(tags);

            entries.add(registryEntry);
            entryToTagSpecificationMap.put(registryEntry, specification);
        }

        return new JetMinecraftRegistry<>(registryKey, entryValueClass, server, List.copyOf(entries),
                enabledFeaturePacks, Map.copyOf(entryToTagSpecificationMap));
    }

    /**
     * Gets a {@linkplain List list} of deserialized {@linkplain DataRegistryEntry data registry entries}
     * from a json resource file.
     *
     * @param gson a gson, which should deserialize the data registry entries
     * @param resourceFileName a name of the json resource file
     * @return the list
     * @since 1.0
     */
    public static @NonNull List<DataRegistryEntry<?>> entries(@NonNull Gson gson, @NonNull String resourceFileName) {
        InputStream stream = JetRegistryManager.class.getClassLoader().getResourceAsStream(resourceFileName);
        if (stream == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a resource file with name of \"%s\"", resourceFileName
            ));
        }

        List<DataRegistryEntry<?>> entries = new ArrayList<>();

        try {
            JsonArray jsonArray = gson.fromJson(new String(stream.readAllBytes()), JsonArray.class);
            for (JsonElement element : jsonArray)
                entries.add(gson.fromJson(element, DataRegistryEntry.class));
            stream.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return List.copyOf(entries);
    }

    private static <V> @NonNull JetRegistryEntry<V> validateEntry(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V> castEntry))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return castEntry;
    }

    /**
     * Represents a builder of a {@linkplain ServerUpdateTagsConfigurationPacket.Tag tag}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerUpdateTagsConfigurationPacket.Tag
     */
    private static final class TagBuilder {

        private final List<Integer> identifiers = new ArrayList<>();

        /**
         * Adds an identifier of a registry entry that has this tag.
         *
         * @param identifier the identifier
         * @since 1.0
         */
        private void add(int identifier) {
            this.identifiers.add(identifier);
        }

        /**
         * Builds the {@linkplain ServerUpdateTagsConfigurationPacket.Tag tag}.
         *
         * @param tagKey a key of the tag
         * @return the tag
         * @since 1.0
         */
        private ServerUpdateTagsConfigurationPacket.@NonNull Tag toPacketTag(@NonNull Key tagKey) {
            int[] array = new int[this.identifiers.size()];
            for (int index = 0; index < this.identifiers.size(); index++)
                array[index] = this.identifiers.get(index);
            return new ServerUpdateTagsConfigurationPacket.Tag(tagKey, array);
        }
    }
}