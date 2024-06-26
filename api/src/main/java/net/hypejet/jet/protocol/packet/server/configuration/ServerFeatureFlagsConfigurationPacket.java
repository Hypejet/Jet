package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet} that enables features on a client,
 * which are usually experimental updates from Minecraft.
 *
 * @param featureFlags identifiers of the features to enable
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerFeatureFlagsConfigurationPacket(@NonNull Collection<Key> featureFlags)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerConfigurationPacket server configuration packet}.
     *
     * @param featureFlags identifiers of the features to enable
     * @since 1.0
     */
    public ServerFeatureFlagsConfigurationPacket {
        featureFlags = List.copyOf(featureFlags);
    }
}