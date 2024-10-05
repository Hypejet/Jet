package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.pack.info.PackInfo;
import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * to inform server about data packs that are present on the client.
 *
 * @param dataPacks the pack information collection of data packs
 * @since 1.0
 * @author Codestech
 * @see DataPack
 * @see ClientConfigurationPacket
 */
public record ClientKnownPacksConfigurationPacket(@NonNull Collection<PackInfo> dataPacks)
        implements ClientConfigurationPacket {
    /**
     * Constructs the {@linkplain ClientKnownPacksConfigurationPacket known data packs configuration packet}.
     *
     * @param dataPacks the data dataPacks
     * @since 1.0
     */
    public ClientKnownPacksConfigurationPacket {
        dataPacks = Set.copyOf(dataPacks);
    }
}