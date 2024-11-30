package net.hypejet.jet.network.packet.server.configuration;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents {@linkplain ServerPacket a server packet}, which initializes a registry on a client.
 *
 * @param registry an identifier of the registry
 * @param entries entries of the registry
 * @since 1.0
 * @author Coedstech
 * @see Entry
 * @see ServerPacket
 */
public record ServerRegistryDataConfigurationPacket(@NonNull Key registry, @NonNull Collection<Entry> entries)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerRegistryDataConfigurationPacket server registry data configuration packet}.
     *
     * @param registry an identifier of the registry
     * @param entries entries of the registry
     * @since 1.0
     */
    public ServerRegistryDataConfigurationPacket {
        entries = List.copyOf(NullabilityUtil.requireNonNull(entries, "entries"));
    }

    /**
     * Represents an entry of a Minecraft registry.
     *
     * @param key a key of the entry
     * @param data data of the entry, {@code null} if the client knows the value
     * @since 1.0
     * @author Codestech
     * @see ServerRegistryDataConfigurationPacket
     */
    public record Entry(@NonNull Key key, @Nullable BinaryTag data) {
        /**
         * Constructs the {@linkplain Entry entry}.
         *
         * @param key a key of the entry
         * @param data data of the entry, {@code null} if the client knows the value
         * @since 1.0
         */
        public Entry {
            NullabilityUtil.requireNonNull(key, "key");
        }
    }
}