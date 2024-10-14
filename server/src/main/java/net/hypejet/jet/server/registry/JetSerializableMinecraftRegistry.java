package net.hypejet.jet.server.registry;

import com.google.gson.Gson;
import net.hypejet.jet.data.model.api.registry.DataRegistryEntry;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.event.events.registry.RegistryInitializeEvent;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a {@linkplain JetMinecraftRegistry Minecraft registry}, which can be serialized to Minecraft
 * network protocol.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @author Codesetech
 * @see MinecraftRegistry
 */
public final class JetSerializableMinecraftRegistry<V> extends JetMinecraftRegistry<V> {

    private final BinaryTagCodec<V> binaryTagCodec;

    private JetSerializableMinecraftRegistry(
            @NonNull Key registryKey, @NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
            @NonNull BinaryTagCodec<V> binaryTagCodec, @NonNull Set<FeaturePack> enabledFeaturePacks,
            @NonNull List<JetRegistryEntry<V>> entries,
            @NonNull Map<JetRegistryEntry<V>, JetTagSpecification> entryToTagSpecificationMap
    ) {
        super(registryKey, entryValueClass, server, entries, enabledFeaturePacks, entryToTagSpecificationMap);
        this.binaryTagCodec = NullabilityUtil.requireNonNull(binaryTagCodec, "binary tag codec");
    }

    /**
     * Gets a {@linkplain BinaryTagCodec binary tag codec}, which deserializes and serializes entries
     * of this registry.
     *
     * @return the binary tag codec
     * @since 1.0
     */
    public @NonNull BinaryTagCodec<V> binaryTagCodec() {
        return this.binaryTagCodec;
    }

    /**
     * Creates a {@linkplain JetSerializableMinecraftRegistry serializable Minecraft registry}.
     *
     * @param registryKey a key of the registry
     * @param entryValueClass a class of values of registry entries
     * @param server a server that should own registry
     * @param binaryTagCodec a binary tag codec, which deserializes and serializes values of the entries to network
     * @param enabledFeaturePacks a set of feature packs, which are enabled on the server
     * @param gson a gson, which deserializes the built-in registry entries from a resource file
     * @param resourceFileName a name of the resource file
     * @return the serializable Minecraft registry
     * @param <V> a type of values of the registry entries
     * @since 1.0
     */
    public static <V> @NonNull JetSerializableMinecraftRegistry<V> create(
            @NonNull Key registryKey, @NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
            @NonNull BinaryTagCodec<V> binaryTagCodec, @NonNull Set<FeaturePack> enabledFeaturePacks,
            @NonNull Gson gson, @NonNull String resourceFileName
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

        RegistryInitializeEvent<V> initializeEvent = new RegistryInitializeEvent<>(registryKey, entryValueClass);
        server.eventNode().call(initializeEvent);

        initializeEvent.entryMap().forEach((key, value) -> entries.add(
                new JetRegistryEntry<>(key, value, null)
        ));

        return new JetSerializableMinecraftRegistry<>(registryKey, entryValueClass, server, binaryTagCodec,
                enabledFeaturePacks, List.copyOf(entries), Map.copyOf(entryToTagSpecificationMap));
    }
}