package net.hypejet.jet.server.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.map.MutableMapAcquisition;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.data.model.server.registry.registries.registry.DataRegistryEntry;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket.TagRegistry;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.acquisition.map.HashMapAcquirable;
import net.hypejet.jet.server.acquisition.mapped.MappedAcquisition;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.registry.session.RegistryTagUpdateFunction;
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
import java.util.function.UnaryOperator;

/**
 * Represents an implementation of a {@linkplain MinecraftRegistry Minecraft registry}.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @author Codestech
 */
public class JetMinecraftRegistry<V> implements MinecraftRegistry<V> {

    private final JetMinecraftServer server;

    private final Key registryKey;
    private final Class<V> entryValueClass;

    private final Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap;
    private final Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap;

    private final List<JetRegistryEntry<V>> sortedEntries;
    private final Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap;

    private final HashMapAcquirable<JetRegistryEntry<V>, Tags> tags;

    /**
     * Constructs the {@linkplain JetSerializableMinecraftRegistry Minecraft registry}.
     *
     * @param registryKey a key of the registry
     * @param entryValueClass a class of values of entries of the registry
     * @param server a server, on which the registry is registered
     * @param entries a collection of registry entries, which should be put into the registry
     * @param enabledFeaturePacks feature packs, which are enabled on the server
     * @param entryToTagsMap tags of the registry entries specified
     * @since 1.0
     */
    protected JetMinecraftRegistry(@NonNull Key registryKey, @NonNull Class<V> entryValueClass,
                                   @NonNull JetMinecraftServer server, @NonNull List<JetRegistryEntry<V>> entries,
                                   @NonNull Set<FeaturePack> enabledFeaturePacks,
                                   @NonNull Map<JetRegistryEntry<V>, Tags> entryToTagsMap) {
        NullabilityUtil.requireNonNull(entries, "entries");
        NullabilityUtil.requireNonNull(enabledFeaturePacks, "enabled feature packs");
        NullabilityUtil.requireNonNull(entryToTagsMap, "entry to tags map");

        this.server = NullabilityUtil.requireNonNull(server, "server");
        this.registryKey = NullabilityUtil.requireNonNull(registryKey, "key");
        this.entryValueClass = NullabilityUtil.requireNonNull(entryValueClass, "entry value class");

        Set<PackInfo> enabledFeaturePackInfos = new HashSet<>();
        for (FeaturePack enabledDataPack : enabledFeaturePacks)
            enabledFeaturePackInfos.add(enabledDataPack.info());

        Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap = new HashMap<>();
        Map<JetRegistryEntry<V>, Tags> tags = new HashMap<>();

        List<JetRegistryEntry<V>> sortedEntries = new ArrayList<>();

        for (JetRegistryEntry<V> entry : entries) {
            Key key = entry.key();
            V value = entry.value();

            PackInfo knownPackInfo = entry.knownPackInfo();
            if (knownPackInfo != null && !enabledFeaturePackInfos.contains(knownPackInfo)) continue;

            keyToRegistryEntryMap.put(entry.key(), new JetRegistryEntry<>(key, value, knownPackInfo));

            tags.put(entry, entryToTagsMap.get(entry));
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
        this.tags = new HashMapAcquirable<>(tags);
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
       Integer identifier = this.registryEntryToIdentifierMap.get(validateEntry(entry));
       if (identifier == null)
           throw new IllegalArgumentException("Could not find an identifier for the registry entry specified");
       return identifier;
    }

    @Override
    public @NonNull List<JetRegistryEntry<V>> entries() {
        return this.sortedEntries;
    }

    @Override
    public @NonNull Acquisition<Boolean> hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag) {
        NullabilityUtil.requireNonNull(tag, "tag");
        return new MappedAcquisition<>(this.tagsFor(entry), tags -> tags.contains(tag));
    }

    @Override
    public @NonNull Acquisition<Collection<Key>> tagsFor(@NonNull RegistryEntry<V> entry) {
        JetRegistryEntry<V> validatedEntry = validateEntry(entry);
        Acquisition<Map<JetRegistryEntry<V>, Tags>> acquisition = this.tags.acquire();

        try {
            Map<JetRegistryEntry<V>, Tags> map = acquisition.get();
            Tags tags = map.get(validatedEntry);

            if (tags == null)
                throw new IllegalArgumentException("Could not find a tags for an entry specified");
            // TODO: A better acquisition implementation for this case?
            return new MappedAcquisition<>(acquisition, ignoredMap -> tags.tags());
        } catch (Throwable throwable) {
            acquisition.close(); // Unlock the acquisition if an error occurs
            throw throwable; // The throwable was not handled completely
        }
    }

    @Override
    public void updateTags(@NonNull RegistryEntry<V> entry, @NonNull UnaryOperator<Collection<Key>> tagUnaryOperator) {
        JetRegistryEntry<V> validatedEntry = validateEntry(entry);

        try (MutableMapAcquisition<JetRegistryEntry<V>, Tags, ?> tagMapAcquisition = this.tags.acquireMutable()) {
            Map<JetRegistryEntry<V>, Tags> tagMap = tagMapAcquisition.get();
            Tags tags = tagMap.get(validatedEntry);

            if (tags == null)
                throw new IllegalArgumentException("Could not find tags for a registry entry specified");
            tagMapAcquisition.put(validatedEntry, new Tags(tagUnaryOperator.apply(tags.tags())));

            TagRegistry tagRegistry = this.createTagRegistry(tagMap);
            Set<TagRegistry> tagRegistrySet = Set.of(tagRegistry);

            try (Acquisition<? extends Collection<JetPlayer>> playerAcquisition = this.server.players()) {
                for (JetPlayer player : playerAcquisition.get()) {
                    // TODO: Do the session consumption in an another thread to avoid relying on client (HIGH PRIORITY)
                    SocketPlayerConnection connection = player.connection();
                    try (Acquisition<Session> sessionAcquisition = connection.session().acquire()) {
                        if (!(sessionAcquisition.get().sessionTask() instanceof RegistryTagUpdateFunction function))
                            continue;
                        function.updateTags(() -> player.sendPacket(new ServerUpdateTagsPacket(tagRegistrySet)));
                    }
                }
            }
        }
    }

    /**
     * Creates a {@linkplain TagRegistry tag registry} using tags attached
     * to entries from this registry.
     *
     * @return the tag registry
     * @since 1.0
     */
    public @NonNull Acquisition<TagRegistry> createTagRegistry() {
        return new MappedAcquisition<>(this.tags.acquire(), this::createTagRegistry);
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
        Map<JetRegistryEntry<V>, Tags> entryToTagsMap = new HashMap<>();

        for (DataRegistryEntry<?> dataEntry : JetMinecraftRegistry.entries(gson, resourceFileName)) {
            if (!entryValueClass.isAssignableFrom(dataEntry.value().getClass()))
                throw new IllegalArgumentException("The data registry entry has incompatible value");

            JetRegistryEntry<V> registryEntry = new JetRegistryEntry<>(dataEntry.key(),
                    entryValueClass.cast(dataEntry.value()), dataEntry.knownPackInfo());

            Tags tags = null;
            Collection<Key> tagCollection = dataEntry.tags();

            if (tagCollection != null)
                tags = new Tags(tagCollection);

            entries.add(registryEntry);
            entryToTagsMap.put(registryEntry, tags);
        }

        return new JetMinecraftRegistry<>(registryKey, entryValueClass, server, List.copyOf(entries),
                enabledFeaturePacks, Map.copyOf(entryToTagsMap));
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

    private @NonNull TagRegistry createTagRegistry(@NonNull Map<JetRegistryEntry<V>, Tags> tags) {
        Map<Key, TagBuilder> tagMap = new HashMap<>();
        for (Map.Entry<JetRegistryEntry<V>, Tags> mapEntry : tags.entrySet()) {
            JetRegistryEntry<V> entry = mapEntry.getKey();
            for (Key tag : mapEntry.getValue().tags())
                tagMap.computeIfAbsent(tag, ignoredTagKey -> new TagBuilder()).add(this.identifierOf(entry));
        }

        Collection<ServerUpdateTagsPacket.Tag> packetTags = new HashSet<>();
        tagMap.forEach((key, builder) -> packetTags.add(builder.toPacketTag(key)));
        return new TagRegistry(this.registryKey, Set.copyOf(packetTags));
    }

    /**
     * Represents a builder of a {@linkplain ServerUpdateTagsPacket.Tag tag}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerUpdateTagsPacket.Tag
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
         * Builds the {@linkplain ServerUpdateTagsPacket.Tag tag}.
         *
         * @param tagKey a key of the tag
         * @return the tag
         * @since 1.0
         */
        private ServerUpdateTagsPacket.@NonNull Tag toPacketTag(@NonNull Key tagKey) {
            int[] array = new int[this.identifiers.size()];
            for (int index = 0; index < this.identifiers.size(); index++)
                array[index] = this.identifiers.get(index);
            return new ServerUpdateTagsPacket.Tag(tagKey, array);
        }
    }
}