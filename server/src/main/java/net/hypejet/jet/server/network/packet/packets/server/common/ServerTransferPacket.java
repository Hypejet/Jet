package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which changes a server that the client is connected to.
 *
 * @param address an address of the new server
 * @param port a port of the new server
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public record ServerTransferPacket(@NonNull String address, int port) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerTransferPacket server transfer packet}.
     *
     * @param address an address of the new server
     * @param port a port of the new server
     * @since 1.0
     */
    public ServerTransferPacket {
        NullabilityUtil.requireNonNull(address, "address");
    }
}