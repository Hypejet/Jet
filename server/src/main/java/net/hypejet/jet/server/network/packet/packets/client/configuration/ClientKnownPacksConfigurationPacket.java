package net.hypejet.jet.server.network.packet.packets.client.configuration;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which sends which feature packs that are present on the server
 * are also present on a client.
 *
 * @param featurePacks the feature packs
 * @since 1.0
 * @see PackInfo
 * @see ClientPacket
 */
public record ClientKnownPacksConfigurationPacket(@NonNull Collection<PackInfo> featurePacks) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientKnownPacksConfigurationPacket client known feature packs configuration packet}.
     *
     * @param featurePacks the feature packs
     * @since 1.0
     */
    public ClientKnownPacksConfigurationPacket {
        featurePacks = Set.copyOf(NullabilityUtil.requireNonNull(featurePacks, "feature packs"));
    }
}