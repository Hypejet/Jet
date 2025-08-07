package net.hypejet.jet.server.network.packet.packets.client.common;

import java.util.Objects;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet} containing settings of a client.
 *
 * @param settings the settings
 * @since 1.0
 * @see ClientPacket
 */
public record ClientInformationPacket(Player.@NonNull Settings settings)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientInformationPacket client information packet}.
     *
     * @param settings the settings
     * @since 1.0
     */
    public ClientInformationPacket {
        Objects.requireNonNull(settings, "settings");
    }
}