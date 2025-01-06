package net.hypejet.jet.server.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.data.model.server.registry.registries.registry.DataRegistryEntry;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket.TagRegistry;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.registry.function.RegistryTagUpdateFunction;
import net.hypejet.jet.server.registry.tags.Tags;
import net.hypejet.jet.server.util.acquisition.BooleanMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.CollectionMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.NotNullObjectMappedAcquisition;
import net.hypejet.jet.server.util.order.ElementOrder;
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
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * Represents an implementation of {@linkplain MinecraftRegistry a Minecraft registry} using
 * {@linkplain ElementOrder an element order}.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @see ElementOrder
 */
public class JetMinecraftRegistry<V> extends ElementOrder<JetRegistryEntry<V>> implements MinecraftRegistry<V> {

    private static final IllegalArgumentException NOT_REGISTERED_EXCEPTION
            = new IllegalArgumentException("The registry entry specified has not been registered in the registry");

    private final JetMinecraftServer server;

    private final Key registryKey;
    private final Class<V> entryValueClass;

    private final Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap;
    private final Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap;

    private final HashMapAcquirable<JetRegistryEntry<V>, Tags> tags;

    /**
     * Constructs the {@linkplain JetSerializableMinecraftRegistry Minecraft registry}.
     *
     * @param registryKey a key of the registry
     * @param entryValueClass a class of values of entries of the registry
     * @param server a server, on which the registry is registered
     * @param entries a list of registry entries, which should be put into the registry, the order is preserved
     * @param enabledFeaturePacks feature packs, which are enabled on the server
     * @param entryToTagsMap tags of the registry entries specified
     * @since 1.0
     */
    public JetMinecraftRegistry(@NonNull Key registryKey, @NonNull Class<V> entryValueClass,
                                @NonNull JetMinecraftServer server, @NonNull List<JetRegistryEntry<V>> entries,
                                @NonNull Set<FeaturePack> enabledFeaturePacks,
                                @NonNull Map<JetRegistryEntry<V>, Tags> entryToTagsMap) {
        super(entries);
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

        for (JetRegistryEntry<V> entry : entries) {
            Key key = entry.key();
            V value = entry.value();

            PackInfo knownPackInfo = entry.knownPackInfo();
            if (knownPackInfo != null && !enabledFeaturePackInfos.contains(knownPackInfo)) continue;

            keyToRegistryEntryMap.put(entry.key(), new JetRegistryEntry<>(key, value, knownPackInfo));
            tags.put(entry, entryToTagsMap.get(entry));
        }

        this.keyToRegistryEntryMap = Map.copyOf(keyToRegistryEntryMap);

        Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap = new HashMap<>();
        for (Map.Entry<Key, JetRegistryEntry<V>> entry : keyToRegistryEntryMap.entrySet())
            registryEntryToKeyMap.put(entry.getValue(), entry.getKey());

        this.registryEntryToKeyMap = Map.copyOf(registryEntryToKeyMap);
        this.tags = new HashMapAcquirable<>(tags);
    }

    @Override
    public final @NonNull Key registryKey() {
        return this.registryKey;
    }

    @Override
    public final @NonNull Class<V> entryValueClass() {
        return this.entryValueClass;
    }

    @Override
    public final @Nullable JetRegistryEntry<V> get(@NonNull Key key) {
        return this.keyToRegistryEntryMap.get(NullabilityUtil.requireNonNull(key, "identifier"));
    }

    @Override
    public final boolean isRegistered(@NonNull RegistryEntry<V> entry) {
        return this.registryEntryToKeyMap.containsKey(validateEntry(entry));
    }

    @Override
    public final @NonNull Key keyOf(@NonNull RegistryEntry<V> entry) {
        Key key = this.registryEntryToKeyMap.get(validateEntry(entry));
        if (key == null)
            throw NOT_REGISTERED_EXCEPTION;
        return key;
    }

    @Override
    public final int identifierOf(@NonNull RegistryEntry<V> entry) {
        return this.identifierOf(validateEntry(entry));
    }

    @Override
    public final @NonNull List<? extends RegistryEntry<V>> entries() {
        return this.elements();
    }

    @Override
    public final @NonNull BooleanAcquisition hasTag(@NonNull RegistryEntry<V> entry, @NonNull Key tag) {
        NullabilityUtil.requireNonNull(tag, "tag");
        return new BooleanMappedAcquisition<>(
                this.tagsFor(entry),
                acquisition -> acquisition.collection().contains(tag)
        );
    }

    @Override
    public final @NonNull CollectionAcquisition<Key, ?> tagsFor(@NonNull RegistryEntry<V> entry) {
        JetRegistryEntry<V> validatedEntry = validateEntry(entry);
        return new CollectionMappedAcquisition<>(this.tags.acquireRead(), acquisition -> {
            Tags tags = acquisition.map().get(validatedEntry);
            if (tags == null)
                throw NOT_REGISTERED_EXCEPTION;
            return tags.tags();
        });
    }

    @Override
    public final void updateTags(@NonNull RegistryEntry<V> entry, @NonNull UnaryOperator<Collection<Key>> tagUnaryOperator) {
        JetRegistryEntry<V> validatedEntry = validateEntry(entry);

        try (MapAcquisition<JetRegistryEntry<V>, Tags, ?> tagMapAcquisition = this.tags.acquireWrite()) {
            Map<JetRegistryEntry<V>, Tags> tagMap = tagMapAcquisition.map();

            Tags tags = tagMap.get(validatedEntry);
            if (tags == null)
                throw NOT_REGISTERED_EXCEPTION;
            tagMap.put(validatedEntry, new Tags(tagUnaryOperator.apply(tags.tags())));

            try (CollectionAcquisition<JetPlayer, ?> playerAcquisition = this.server.players()) {
                Collection<JetPlayer> players = playerAcquisition.collection();
                if (players.isEmpty()) return;

                TagRegistry tagRegistry = this.createTagRegistry(tagMap);
                ServerUpdateTagsPacket updateTagsPacket = new ServerUpdateTagsPacket(Set.of(tagRegistry));

                for (JetPlayer player : players) {
                    // TODO: Do the session consumption in an another thread to avoid relying on client (HIGH PRIORITY)
                    SocketPlayerConnection connection = player.connection();
                    try (NotNullObjectAcquisition<Session> sessionAcquisition = connection.acquireSessionRead()) {
                        if (!(sessionAcquisition.get().sessionTask() instanceof RegistryTagUpdateFunction function))
                            continue;
                        function.updateTags(updateTagsPacket);
                    }
                }
            }
        }
    }

    /**
     * Creates {@linkplain NotNullObjectAcquisition a not-null object acquisition} of
     * {@linkplain TagRegistry tag registry} of tags attached to entries from this registry.
     *
     * @return the tag registry
     * @since 1.0
     */
    public final @NonNull NotNullObjectAcquisition<TagRegistry> createTagRegistry() {
        return new NotNullObjectMappedAcquisition<>(
                this.tags.acquireRead(),
                acquisition -> createTagRegistry(acquisition.map())
        );
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
     * Creates {@linkplain JetMinecraftRegistry a Minecraft registry}.
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

            Collection<Key> tagCollection = dataEntry.tags();
            if (tagCollection == null)
                tagCollection = Set.of();

            entries.add(registryEntry);
            entryToTagsMap.put(registryEntry, new Tags(tagCollection));
        }

        return new JetMinecraftRegistry<>(registryKey, entryValueClass, server, List.copyOf(entries),
                enabledFeaturePacks, Map.copyOf(entryToTagsMap));
    }

    /**
     * Gets {@linkplain List a list} of deserialized {@linkplain DataRegistryEntry data registry entries} from a json
     * resource file.
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
     * Represents a builder of {@linkplain ServerUpdateTagsPacket.Tag a tag of server update tags packet}.
     *
     * @since 1.0
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