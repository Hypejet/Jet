package net.hypejet.jet.event.events.packet;

import net.hypejet.jet.event.events.CancellableEvent;
import net.hypejet.jet.protocol.packet.Packet;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event, which is called when a packet is being sent or received.
 *
 * @since 1.0
 * @author Window5
 * @author Codestech
 */
public sealed abstract class PacketEvent extends CancellableEvent permits PacketReceiveEvent, PacketSendEvent {
    /**
     * Gets the packet that was sent or received.
     *
     * @return the packet
     * @since 1.0
     */
    public abstract @NonNull Packet packet();
}