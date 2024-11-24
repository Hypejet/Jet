package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * to inform server about feature packs that are present on the client.
 *
 * @param featurePacks the pack information collection of feature packs
 * @since 1.0
 * @author Codestech
 * @see PackInfo
 * @see ClientConfigurationPacket
 */
public record ClientKnownPacksConfigurationPacket(@NonNull Collection<PackInfo> featurePacks)
        implements ClientConfigurationPacket {
    /**
     * Constructs the {@linkplain ClientKnownPacksConfigurationPacket known feature packs configuration packet}.
     *
     * @param featurePacks the feature packs
     * @since 1.0
     */
    public ClientKnownPacksConfigurationPacket {
        featurePacks = Set.copyOf(featurePacks);
    }
}