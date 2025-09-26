package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPingRequestPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPingRequestPacket a client ping request packet}.
 *
 * @since 1.0
 * @see ClientPingRequestPacket
 * @see NetworkReader
 */
public final class ClientPingRequestPacketReader implements NetworkReader<ClientPingRequestPacket> {
    /**
     * An instance of the {@linkplain ClientPingRequestPacketReader client pint request packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPingRequestPacketReader INSTANCE = new ClientPingRequestPacketReader();

    private ClientPingRequestPacketReader() {}

    @Override
    public @NonNull ClientPingRequestPacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new ClientPingRequestPacket(buf.readLong());
    }
}
