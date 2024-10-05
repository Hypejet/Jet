package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.registry.RegistryEntryData;
import net.hypejet.jet.data.model.utils.NullabilityUtil;
import net.hypejet.jet.event.events.registry.RegistryInitializeEvent;
import net.hypejet.jet.registry.Registry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain Registry registry}.
 *
 * @param <V> a type of values of entries of this registry
 * @since 1.0
 * @author Codesetech
 * @see Registry
 */
public final class JetRegistry<V> implements Registry<V> {

    private final Key registryIdentifier;
    private final Class<V> entryValueClass;

    private final Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap;
    private final Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap;

    private final Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap;
    private final List<JetRegistryEntry<V>> sortedEntries;

    private final BinaryTagCodec<V> binaryTagCodec;

    /**
     * Constructs the {@linkplain JetRegistry registry}.
     *
     * @param identifier an identifier of the registry
     * @param entryValueClass a class of values of entries of the registry
     * @param server a server, on which the registry is registered
     * @param binaryTagCodec a binary tag codec, which reads and writes the registry entry
     * @param builtInEntryData a collection of registry entry data, which should be enabled by default
     * @since 1.0
     */
    public JetRegistry(@NonNull Key identifier, @NonNull Class<V> entryValueClass,
                       @NonNull JetMinecraftServer server, @NonNull BinaryTagCodec<V> binaryTagCodec,
                       @NonNull Collection<RegistryEntryData<V>> builtInEntryData) {
        NullabilityUtil.requireNonNull(server, "server");
        NullabilityUtil.requireNonNull(builtInEntryData, "built-in entry data");
        
        this.registryIdentifier = NullabilityUtil.requireNonNull(identifier, "identifier");
        this.entryValueClass = NullabilityUtil.requireNonNull(entryValueClass, "entry value class");
        this.binaryTagCodec =  NullabilityUtil.requireNonNull(binaryTagCodec, "binary tag codec");

        RegistryInitializeEvent<V> initializeEvent = new RegistryInitializeEvent<>(identifier, entryValueClass);
        server.eventNode().call(initializeEvent);
        
        Map<Key, JetRegistryEntry<V>> keyToRegistryEntryMap = new HashMap<>();
        for (Map.Entry<Key, V> entry : initializeEvent.entryMap().entrySet()) {
            Key key = entry.getKey();
            V value = entry.getValue();
            keyToRegistryEntryMap.put(entry.getKey(), new JetRegistryEntry<>(key, value, null));
        }

        Set<DataPack> enabledDataPacks = server.configuration().enabledPacks();
        for (RegistryEntryData<V> entry : builtInEntryData) {
            Key key = entry.key();
            V value = entry.value();

            DataPack dataPack = entry.knownPack();
            if (!enabledDataPacks.contains(dataPack)) continue;

            keyToRegistryEntryMap.put(entry.key(), new JetRegistryEntry<>(key, value, dataPack));
        }

        this.keyToRegistryEntryMap = Map.copyOf(keyToRegistryEntryMap);

        List<JetRegistryEntry<V>> sortedEntries = new ArrayList<>(keyToRegistryEntryMap.values());
        this.sortedEntries = List.copyOf(sortedEntries);

        Map<JetRegistryEntry<V>, Integer> registryEntryToIdentifierMap = new HashMap<>();
        for (int index = 0; index < sortedEntries.size(); index++)
            registryEntryToIdentifierMap.put(sortedEntries.get(index), index);
        this.registryEntryToIdentifierMap = Map.copyOf(registryEntryToIdentifierMap);

        Map<JetRegistryEntry<V>, Key> registryEntryToKeyMap = new HashMap<>();
        for (Map.Entry<Key, JetRegistryEntry<V>> entry : keyToRegistryEntryMap.entrySet())
            registryEntryToKeyMap.put(entry.getValue(), entry.getKey());
        this.registryEntryToKeyMap = Map.copyOf(registryEntryToKeyMap);
    }

    @Override
    public @NonNull Key registryIdentifier() {
        return this.registryIdentifier;
    }

    @Override
    public @NonNull Class<V> entryValueClass() {
        return this.entryValueClass;
    }

    @Override
    public @Nullable JetRegistryEntry<V> get(@NonNull Key identifier) {
        NullabilityUtil.requireNonNull(identifier, "identifier");
        return this.keyToRegistryEntryMap.get(identifier);
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
    public @NonNull Key keyOf(@NonNull RegistryEntry<V> entry) {
        NullabilityUtil.requireNonNull(entry, "entry");
        if (!(entry instanceof JetRegistryEntry<V>))
            throw new IllegalArgumentException("The entry specified is not a valid registry entry");
        return Objects.requireNonNull(this.registryEntryToKeyMap.get(entry),
                String.format("The registry entry %s was not registered in this registry", entry));
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
}