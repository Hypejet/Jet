package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.registry.DataRegistryEntry;
import net.hypejet.jet.data.model.utils.NullabilityUtil;
import net.hypejet.jet.event.events.registry.RegistryInitializeEvent;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
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
 * Represents a {@linkplain JetRegistry}, which is an implementation of a {@linkplain MinecraftRegistry Minecraft
 * registry} and can be serialized via Minecraft network protocol.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @author Codesetech
 * @see MinecraftRegistry
 */
public final class JetSerializableMinecraftRegistry<V> extends JetRegistry<V> implements MinecraftRegistry<V> {

    private final Key registryIdentifier;

    private final Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap;
    private final List<JetRegistryEntry<V>> sortedEntries;

    private final BinaryTagCodec<V> binaryTagCodec;

    /**
     * Constructs the {@linkplain JetSerializableMinecraftRegistry serializable Minecraft registry}.
     *
     * @param identifier an identifier of the registry
     * @param entryValueClass a class of values of entries of the registry
     * @param server a server, on which the registry is registered
     * @param binaryTagCodec a binary tag codec, which reads and writes the registry entry
     * @param builtInEntries a collection of registry entry data, which should be enabled by default
     * @param enabledDataPacks data packs, which are enabled on the server
     * @since 1.0
     */
    public JetSerializableMinecraftRegistry(@NonNull Key identifier, @NonNull Class<V> entryValueClass,
                                            @NonNull JetMinecraftServer server,
                                            @NonNull BinaryTagCodec<V> binaryTagCodec,
                                            @NonNull Collection<DataRegistryEntry<V>> builtInEntries,
                                            @NonNull Set<DataPack> enabledDataPacks) {
        super(entryValueClass, server,
                prepareValues(identifier, entryValueClass, server, builtInEntries),
                enabledDataPacks);

        this.registryIdentifier = identifier;
        this.binaryTagCodec = NullabilityUtil.requireNonNull(binaryTagCodec, "binary tag codec");

        List<JetRegistryEntry<V>> sortedEntries = new ArrayList<>(this.keyToRegistryEntryMap().values());
        this.sortedEntries = List.copyOf(sortedEntries);

        Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap = new HashMap<>();
        for (int index = 0; index < sortedEntries.size(); index++)
            registryEntryToIdentifierMap.put(sortedEntries.get(index), index);
        this.registryEntryToIdentifierMap = Map.copyOf(registryEntryToIdentifierMap);

        this.sortedEntries.forEach(System.out::println);
    }

    @Override
    public @NonNull Key registryIdentifier() {
        return this.registryIdentifier;
    }

    @Override
    public @Nullable JetRegistryEntry<V> get(int numericIdentifier) {
        return this.sortedEntries.get(numericIdentifier);
    }

    @Override
    public boolean isRegistered(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return this.sortedEntries.contains(entry);
    }

    @Override
    public int identifierOf(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return Objects.requireNonNull(this.registryEntryToIdentifierMap.get(entry),
                String.format("The registry entry %s was not registered in this registry", entry));
    }

    @Override
    public @NonNull List<RegistryEntry<V>> entries() {
        return List.copyOf(this.sortedEntries);
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

    private static <V> @NonNull Collection<DataRegistryEntry<V>> prepareValues(
            @NonNull Key identifier, @NonNull Class<V> entryValueClass, @NonNull JetMinecraftServer server,
            @NonNull Collection<DataRegistryEntry<V>> builtInEntries
    ) {
        NullabilityUtil.requireNonNull(identifier, "identifier");
        NullabilityUtil.requireNonNull(builtInEntries, "built-in entries");

        Set<DataRegistryEntry<V>> entries = new HashSet<>(builtInEntries);
        RegistryInitializeEvent<V> initializeEvent = new RegistryInitializeEvent<>(identifier, entryValueClass);

        server.eventNode().call(initializeEvent);
        return Set.copyOf(entries);
    }
}