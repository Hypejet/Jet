package net.hypejet.jet.event.events.packet;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketEvent packet event}, which is called when a {@linkplain ServerPacket server packet}
 * is being sent.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see PacketEvent
 */
public final class PacketSendEvent extends PacketEvent {

    private final ServerPacket packet;

    /**
     * Constructs a {@linkplain PacketSendEvent packet send event}.
     *
     * @param packet the packet that is being sent
     * @since 1.0
     */
    public PacketSendEvent(@NonNull ServerPacket packet) {
        this.packet = packet;
    }

    /**
     * Gets the {@linkplain ServerPacket server packet} that is being sent.
     *
     * @return the server packet
     * @since 1.0
     */
    @Override
    public @NonNull ServerPacket packet() {
        return this.packet;
    }

    @Override
    public String toString() {
        return "PacketSendEvent[packet=" + this.packet + "]";
    }
}