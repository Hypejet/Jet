package net.hypejet.jet.event.events.packet;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

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

    private ServerPacket packet;

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
    public @NonNull ServerPacket getPacket() {
        return this.packet;
    }

    /**
     * Replaces the {@linkplain ServerPacket server packet}, which is being sent.
     *
     * @param packet the new packet
     * @since 1.0
     */
    public void setPacket(@NonNull ServerPacket packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "PacketSendEvent{" +
                "packet=" + this.packet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PacketSendEvent event)) return false;
        return Objects.equals(this.packet, event.packet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.packet);
    }
}