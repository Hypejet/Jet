package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet} containing settings of a client.
 *
 * @param settings the settings
 * @since 1.0
 * @author Codestech
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
        NullabilityUtil.requireNonNull(settings, "settings");
    }
}