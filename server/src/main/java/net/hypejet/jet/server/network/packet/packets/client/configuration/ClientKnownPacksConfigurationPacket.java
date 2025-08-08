package net.hypejet.jet.server.network.packet.packets.client.configuration;

import java.util.List;
import java.util.Objects;

import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet} containing information about feature packs
 * that are present on both the server and the client sending the packet.
 *
 * @param knownPacks a list of known packs representing the feature packs
 * @since 1.0
 * @see KnownPack
 * @see ClientPacket
 */
// TODO: Add an information about the known-pack list ordering
public record ClientKnownPacksConfigurationPacket(@NonNull List<KnownPack> knownPacks) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientKnownPacksConfigurationPacket client known feature packs configuration packet}.
     *
     * @param knownPacks a list of known packs representing the feature packs
     * @since 1.0
     */
    public ClientKnownPacksConfigurationPacket {
        knownPacks = List.copyOf(Objects.requireNonNull(knownPacks, "known packs"));
    }
}