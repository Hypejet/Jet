package net.hypejet.jet.server.registry;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.events.registry.RegistryInitializeEvent;
import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.registry.Registry;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an implementation of {@linkplain Registry registry}.
 *
 * @param <E> a type of entries of this registry
 * @since 1.0
 * @author Codesetech
 * @see Registry
 */
public final class JetRegistry<E> implements Registry<E> {

    private final Key registryIdentifier;
    private final Class<E> entryClass;

    private final Map<Key, E> identifierToEntryMap;
    private final IntObjectMap<E> numericIdentifierToEntryMap = new IntObjectHashMap<>();

    private final Map<E, Key> entryToIdentifierMap;
    private final Map<E, Integer> entryToNumericIdentifierMap;

    private final Map<E, DataPack> datapacks = Map.of(); // TODO: DataPack support

    private final ServerRegistryDataConfigurationPacket packet;

    /**
     * Constructs the {@linkplain JetRegistry registry}.
     *
     * @param identifier an identifier of the registry
     * @param entryClass a class of entries of the registry
     * @param server a server, on which the registry is registered
     * @param binaryTagCodec a binary tag codec, which reads and writes the registry entry
     * @since 1.0
     */
    public JetRegistry(@NonNull Key identifier, @NonNull Class<E> entryClass, @NonNull MinecraftServer server,
                       @NonNull BinaryTagCodec<E> binaryTagCodec) {
        Objects.requireNonNull(identifier, "The registry identifier must not be null");
        Objects.requireNonNull(identifier, "The entry map must not be null");

        this.registryIdentifier = identifier;
        this.entryClass = Objects.requireNonNull(entryClass, "The entry class must not be null");

        RegistryInitializeEvent<E> initializeEvent = new RegistryInitializeEvent<>(identifier, entryClass);
        server.eventNode().call(initializeEvent);
        this.identifierToEntryMap = Map.copyOf(initializeEvent.entryMap());

        int currentNumericIdentifier = 0;
        for (E value : this.identifierToEntryMap.values())
            this.numericIdentifierToEntryMap.put(currentNumericIdentifier++, value);

        Map<E, Key> entryToIdentifierMap = new HashMap<>();
        this.identifierToEntryMap.forEach((key, entry) -> entryToIdentifierMap.put(entry, key));
        this.entryToIdentifierMap = Map.copyOf(entryToIdentifierMap);

        Map<E, Integer> entryToNumericIdentifierMap = new HashMap<>();
        this.numericIdentifierToEntryMap.forEach((key, entry) -> entryToNumericIdentifierMap.put(entry, key));
        this.entryToNumericIdentifierMap = Map.copyOf(entryToNumericIdentifierMap);

        List<ServerRegistryDataConfigurationPacket.Entry> entries = new ArrayList<>();
        for (Map.Entry<Key, E> mapEntry : this.identifierToEntryMap.entrySet()) {
            E entry = mapEntry.getValue();
            entries.add(this.entryToNumericIdentifierMap.get(entry), new ServerRegistryDataConfigurationPacket.Entry(
                    mapEntry.getKey(), binaryTagCodec.write(entry)
            ));
        }

        this.packet = new ServerRegistryDataConfigurationPacket(this.registryIdentifier, entries);
    }

    @Override
    public @NonNull Key registryIdentifier() {
        return this.registryIdentifier;
    }

    @Override
    public @NonNull Class<E> entryClass() {
        return this.entryClass;
    }

    @Override
    public @Nullable E get(@NonNull Key identifier) {
        Objects.requireNonNull(identifier, "The identifier must not be null");
        return this.identifierToEntryMap.get(identifier);
    }

    @Override
    public @Nullable E get(int numericIdentifier) {
        return this.numericIdentifierToEntryMap.get(numericIdentifier);
    }

    @Override
    public boolean isRegistered(@NonNull E entry) {
        Objects.requireNonNull(entry, "The entry must not be null");
        return this.identifierToEntryMap.containsValue(entry);
    }

    @Override
    public @NotNull Key identifierOf(@NonNull E entry) {
        Objects.requireNonNull(entry, "The entry must not be null");
        return Objects.requireNonNull(this.entryToIdentifierMap.get(entry), String.format(
                "The registry entry %s was not registered in this registry", entry
        ));
    }

    @Override
    public int numericIdentifierOf(@NonNull E entry) {
        Objects.requireNonNull(entry, "The entry must not be null");
        return Objects.requireNonNull(this.entryToNumericIdentifierMap.get(entry), String.format(
                "The registry entry %s was not registered in this registry", entry
        ));
    }

    @Override
    public @NonNull Collection<E> entries() {
        return Set.copyOf(this.identifierToEntryMap.values());
    }

    @Override
    public @NonNull List<E> sortedEntries() {
        List<E> sortedEntries = new ArrayList<>(this.numericIdentifierToEntryMap.values());
        sortedEntries.sort(Comparator.comparingInt(this::numericIdentifierOf));
        return List.copyOf(sortedEntries);
    }

    @Override
    public @Nullable DataPack datapack(@NonNull E entry) {
        return this.datapacks.get(Objects.requireNonNull(entry, "The entry must not be null"));
    }

    /**
     * Gets a {@linkplain ServerRegistryDataConfigurationPacket registry data configuration packet}.
     *
     * @return the packet
     * @since 1.0
     */
    public @NonNull ServerRegistryDataConfigurationPacket getPacket() {
        return this.packet;
    }
}