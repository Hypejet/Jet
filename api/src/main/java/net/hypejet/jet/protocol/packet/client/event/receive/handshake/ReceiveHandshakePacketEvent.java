package net.hypejet.jet.protocol.packet.client.event.receive.handshake;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.client.event.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.event.PacketEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketReceiveEvent}, which is called when a {@linkplain ClientHandshakePacket} is being
 * received.
 *
 * @since 1.0
 * @author Window5
 * @see ClientHandshakePacket
 * @see PacketReceiveEvent
 */
public interface ReceiveHandshakePacketEvent extends PacketReceiveEvent {
    /**
     * Gets the {@linkplain ClientHandshakePacket client packet} that was received.
     *
     * @return the client packet
     * @since 1.0
     */
    @Override
    @NonNull
    ClientHandshakePacket packet();
}
