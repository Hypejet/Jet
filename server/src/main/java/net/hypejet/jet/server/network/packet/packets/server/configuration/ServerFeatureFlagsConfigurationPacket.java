package net.hypejet.jet.server.network.packet.packets.server.configuration;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sets flags of features that should be enabled on
 * a client.
 *
 * @param featureFlags keys of the features to enable
 * @since 1.0
 * @see ServerFeatureFlagsConfigurationPacket
 */
public record ServerFeatureFlagsConfigurationPacket(@NonNull Collection<Key> featureFlags)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerFeatureFlagsConfigurationPacket server feature flags configuration packet}.
     *
     * @param featureFlags keys of the features to enable
     * @since 1.0
     */
    public ServerFeatureFlagsConfigurationPacket {
        featureFlags = List.copyOf(Objects.requireNonNull(featureFlags, "feature flags"));
    }
}