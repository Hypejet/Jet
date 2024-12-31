package net.hypejet.jet.server.registry;

import com.google.gson.Gson;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.data.model.server.registry.registries.registry.DataRegistryEntry;
import net.hypejet.jet.event.events.registry.RegistryInitializeEvent;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.registry.tags.Tags;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents {@linkplain JetMinecraftRegistry a Minecraft registry}, which can be serialized to Minecraft network
 * protocol.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @see MinecraftRegistry
 */
public final class JetSerializableMinecraftRegistry<V> extends JetMinecraftRegistry<V> {

    private final Writer<V, ? extends BinaryTag> binaryTagWriter;

    private JetSerializableMinecraftRegistry(
            @NonNull Key registryKey, @NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
            @NonNull Writer<V, ? extends BinaryTag> binaryTagWriter, @NonNull Set<FeaturePack> enabledFeaturePacks,
            @NonNull List<JetRegistryEntry<V>> entries, @NonNull Map<JetRegistryEntry<V>, Tags> entryToTagsMap
    ) {
        super(registryKey, entryValueClass, server, entries, enabledFeaturePacks, entryToTagsMap);
        this.binaryTagWriter = NullabilityUtil.requireNonNull(binaryTagWriter, "binary tag writer");
    }

    /**
     * Gets {@linkplain Writer a writer}, which serializes entries of this registry into
     * {@linkplain BinaryTag a binary tag}.
     *
     * @return the writer
     * @since 1.0
     */
    public @NonNull Writer<V, ? extends BinaryTag> binaryTagWriter() {
        return this.binaryTagWriter;
    }

    /**
     * Creates {@linkplain JetSerializableMinecraftRegistry a serializable Minecraft registry}.
     *
     * @param registryKey a key of the registry
     * @param entryValueClass a class of values of registry entries
     * @param server a server that should own registry
     * @param binaryTagWriter a writer, which serializes values of the entries to a binary tag used by network
     * @param enabledFeaturePacks a set of feature packs, which are enabled on the server
     * @param gson a gson, which deserializes the built-in registry entries from a resource file
     * @param resourceFileName a name of the resource file
     * @return the serializable Minecraft registry
     * @param <V> a type of values of the registry entries
     * @since 1.0
     */
    public static <V> @NonNull JetSerializableMinecraftRegistry<V> create(
            @NonNull Key registryKey, @NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
            @NonNull Writer<V, ? extends BinaryTag> binaryTagWriter, @NonNull Set<FeaturePack> enabledFeaturePacks,
            @NonNull Gson gson, @NonNull String resourceFileName
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

        RegistryInitializeEvent<V> initializeEvent = new RegistryInitializeEvent<>(registryKey, entryValueClass);
        server.eventNode().call(initializeEvent);

        initializeEvent.entryMap().forEach((key, value) -> entries.add(
                new JetRegistryEntry<>(key, value, null)
        ));

        return new JetSerializableMinecraftRegistry<>(registryKey, entryValueClass, server, binaryTagWriter,
                enabledFeaturePacks, List.copyOf(entries), Map.copyOf(entryToTagsMap));
    }
}