package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet} informing a client, which data
 * feature packs are present on the server.
 *
 * @param featurePacks the pack information collection of the feature packs
 * @since 1.0
 * @author Codestech
 */
public record ServerKnownPacksConfigurationPacket(@NonNull Collection<PackInfo> featurePacks)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerKnownPacksConfigurationPacket known feature packs configuration packet}.
     *
     * @param featurePacks the feature packs
     * @since 1.0
     */
    public ServerKnownPacksConfigurationPacket {
        featurePacks = Set.copyOf(featurePacks);
    }
}