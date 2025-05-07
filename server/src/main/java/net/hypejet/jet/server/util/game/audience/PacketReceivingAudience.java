package net.hypejet.jet.server.util.game.audience;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Audience an audience}, which can receive {@linkplain ServerPacket server packets}.
 *
 * @since 1.0
 * @see Audience
 */
@FunctionalInterface
public interface PacketReceivingAudience extends Audience {
    /**
     * Sends {@linkplain ServerPacket a server packet} to this audience.
     *
     * @param packet the server packet
     * @since 1.0
     */
    void sendPacket(@NonNull ServerPacket packet);
}