package net.hypejet.jet.event.events.packet;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
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
public final class PacketReceiveEvent extends PacketEvent {

    private final ClientPacket packet;

    /**
     * Constructs a {@linkplain PacketReceiveEvent packet receive event}.
     *
     * @param packet the packet that is being received
     * @since 1.0
     */
    public PacketReceiveEvent(@NonNull ClientPacket packet) {
        this.packet = packet;
    }

    /**
     * Gets the packet that is being received.
     *
     * @return the packet
     * @since 1.0
     */
    @Override
    public @NonNull ClientPacket packet() {
        return this.packet;
    }

    @Override
    public String toString() {
        return "PacketReceiveEvent[packet=" + this.packet + "]";
    }
}