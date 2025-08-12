package net.hypejet.jet.server.network.packet.packets.server.configuration;

import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} informing the client which feature packs are enabled on the server.
 *
 * @param knownPacks a list of known packs representing the enabled feature packs
 * @since 1.0
 */
// TODO: Add an information about the known-pack list ordering
public record ServerKnownPacksConfigurationPacket(@NonNull List<KnownPack> knownPacks) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerKnownPacksConfigurationPacket server known feature packs configuration packet}.
     *
     * @param knownPacks a list of known packs representing the enabled feature packs
     * @since 1.0
     */
    public ServerKnownPacksConfigurationPacket {
        knownPacks = List.copyOf(Objects.requireNonNull(knownPacks, "known packs"));
    }
}