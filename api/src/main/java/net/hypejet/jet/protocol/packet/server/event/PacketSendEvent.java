package net.hypejet.jet.protocol.packet.server.event;

import net.hypejet.jet.protocol.packet.event.PacketEvent;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketEvent packet event}, which is called when a {@linkplain ServerPacket server packet}
 * is received.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see PacketEvent
 */
public non-sealed interface PacketSendEvent extends PacketEvent {
    /**
     * Gets a {@linkplain ServerPacket server packet} that was sent.
     *
     * @return the server packet
     * @since 1.0
     */
    @Override
    @NonNull ServerPacket packet();
}