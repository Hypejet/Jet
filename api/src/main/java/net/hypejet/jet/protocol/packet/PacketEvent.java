package net.hypejet.jet.protocol.packet;

/**
 * Represents any event that has a packet.
 *
 * @since 1.0
 * @author Window
 */
public class PacketEvent {
    public final Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }
}