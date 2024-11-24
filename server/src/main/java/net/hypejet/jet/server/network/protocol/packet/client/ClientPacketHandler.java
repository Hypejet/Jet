package net.hypejet.jet.server.network.protocol.packet.client;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads and handles packets.
 *
 * @param <P> a type of the packet
 * @since 1.0
 * @author Codestech
 * @see NetworkReader
 */
public interface ClientPacketHandler<P extends ClientPacket> extends NetworkReader<P> {
    /**
     * Handles the packet.
     *
     * @param packet the packet
     * @param sessionTask a session task, during which the packet is handled
     * @since 1.0
     */
    void handle(@NonNull P packet, @NonNull SessionTask sessionTask);
}