package net.hypejet.jet.protocol.packet.client.event;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.event.PacketEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketEvent}, which is called when a {@linkplain ClientPacket client packet} is being
 * received.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 * @see PacketEvent
 */
public non-sealed interface PacketReceiveEvent extends PacketEvent {
    /**
     * Gets a {@linkplain ClientPacket client packet} that was received.
     *
     * @return the client packet
     * @since 1.0
     */
    @Override
    @NonNull ClientPacket packet();
}