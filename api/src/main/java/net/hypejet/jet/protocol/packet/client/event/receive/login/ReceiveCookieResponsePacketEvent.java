package net.hypejet.jet.protocol.packet.client.event.receive.login;

import net.hypejet.jet.protocol.packet.client.event.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponsePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketReceiveEvent}, which is called when a {@linkplain ClientCookieResponsePacket} is being
 * received.
 *
 * @since 1.0
 * @author Window5
 * @see ClientCookieResponsePacket
 * @see PacketReceiveEvent
 */
public interface ReceiveCookieResponsePacketEvent extends PacketReceiveEvent {
    /**
     * Gets the {@linkplain ClientCookieResponsePacket client packet} that was received.
     *
     * @return the client packet
     * @since 1.0
     */
    @Override
    @NonNull
    ClientCookieResponsePacket packet();
}
