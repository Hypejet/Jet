package net.hypejet.jet.server.keepalive;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a supplier of a {@linkplain ServerPacket server packet} requesting a keep alive response from a client.
 *
 * @since 1.0
 * @author Codestech
 * @see KeepAliveHandler
 */
@FunctionalInterface
public interface KeepAlivePacketSupplier {
    /**
     * Creates the keep alive packet.
     *
     * @param keepAliveId an identifier of the keep alive packet
     * @return the keep alive packet
     * @since 1.0
     */
    @NonNull ServerPacket createKeepAlivePacket(long keepAliveId);
}