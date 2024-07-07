package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet} informing a client, which data
 * dataPacks are present on the server.
 *
 * @param dataPacks the data packs
 * @since 1.0
 * @author Codestech
 */
public record ServerKnownPacksConfigurationPacket(@NonNull Collection<DataPack> dataPacks)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerKnownPacksConfigurationPacket known packs configuration packet}.
     *
     * @param dataPacks the data packs
     * @since 1.0
     */
    public ServerKnownPacksConfigurationPacket {
        dataPacks = List.copyOf(dataPacks);
    }
}