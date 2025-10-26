package net.hypejet.jet.server.network.codec.packet.client.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientServerListRequestStatusPacket a server list request status packet}.
 *
 * @since 1.0
 * @see ClientServerListRequestStatusPacket
 * @see NetworkReader
 */
public final class ClientServerListRequestStatusPacketReader
        implements NetworkReader<ClientServerListRequestStatusPacket> {
    /**
     * An instance of the {@linkplain ClientServerListRequestStatusPacketReader client server list request status
     * packet reader}.
     *
     * @since 1.0
     */
    public static final ClientServerListRequestStatusPacketReader
            INSTANCE = new ClientServerListRequestStatusPacketReader();

    private ClientServerListRequestStatusPacketReader() {}

    @Override
    public @NonNull ClientServerListRequestStatusPacket read(@NonNull ByteBuf buf,
                                                             @NonNull JetRegistryManager registryManager) {
        return new ClientServerListRequestStatusPacket();
    }
}