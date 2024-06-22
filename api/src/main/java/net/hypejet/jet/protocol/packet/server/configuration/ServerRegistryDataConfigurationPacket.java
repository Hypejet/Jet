package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent when a server wants
 * to send a custom data of a registry to a client.
 *
 * @param registry an identifier of the registry
 * @param entries entries of the registry
 * @since 1.0
 * @author Coedstech
 * @see Entry
 * @see ServerConfigurationPacket
 */
public record ServerRegistryDataConfigurationPacket(@NonNull Key registry, @NonNull Collection<Entry> entries)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerRegistryDataConfigurationPacket registry data configuration packet}.
     *
     * @param registry an identifier of the registry
     * @param entries entries of the registry
     * @since 1.0
     */
    public ServerRegistryDataConfigurationPacket {
        entries = List.copyOf(entries);
    }

    /**
     * Represents an entry of a registry, used by {@linkplain ServerRegistryDataConfigurationPacket registry data
     * configuration packet}.
     *
     * @param identifier an identifier of the entry
     * @param data a data of the entry
     * @see 1.0
     * @author Codestech
     * @see ServerRegistryDataConfigurationPacket
     */
    public record Entry(@NonNull Key identifier, @Nullable BinaryTag data) {}
}
