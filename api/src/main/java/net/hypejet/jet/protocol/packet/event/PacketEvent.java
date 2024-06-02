package net.hypejet.jet.protocol.packet.event;

import net.hypejet.jet.protocol.packet.Packet;
import net.hypejet.jet.protocol.packet.client.event.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.server.event.PacketSendEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event, which is called when a packet is being sent or received.
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 */
public sealed interface PacketEvent permits PacketReceiveEvent, PacketSendEvent {
    /**
     * Gets a packet that was sent or received.
     *
     * @return the packet
     * @since 1.0
     */
    @NonNull
    Packet packet();
}