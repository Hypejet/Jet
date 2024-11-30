package net.hypejet.jet.network.packet.server.configuration;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet} informing client, which feature packs are enabled
 * on the server.
 *
 * @param featurePacks the feature packs
 * @since 1.0
 * @author Codestech
 */
public record ServerKnownPacksConfigurationPacket(@NonNull Collection<PackInfo> featurePacks) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerKnownPacksConfigurationPacket server known feature packs configuration packet}.
     *
     * @param featurePacks the feature packs
     * @since 1.0
     */
    public ServerKnownPacksConfigurationPacket {
        featurePacks = Set.copyOf(NullabilityUtil.requireNonNull(featurePacks, "feature packs"));
    }
}