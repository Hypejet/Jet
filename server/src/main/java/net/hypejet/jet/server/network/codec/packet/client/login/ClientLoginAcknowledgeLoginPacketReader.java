package net.hypejet.jet.server.network.codec.packet.client.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientLoginAcknowledgeLoginPacket a login acknowledge login packet}.
 *
 * @since 1.0
 * @see ClientLoginAcknowledgeLoginPacket
 * @see NetworkReader
 */
public final class ClientLoginAcknowledgeLoginPacketReader
        implements NetworkReader<ClientLoginAcknowledgeLoginPacket> {
    /**
     * An instance of the {@linkplain ClientLoginAcknowledgeLoginPacketReader client login acknowledge login packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientLoginAcknowledgeLoginPacketReader
            INSTANCE = new ClientLoginAcknowledgeLoginPacketReader();

    private ClientLoginAcknowledgeLoginPacketReader() {}

    @Override
    public @NonNull ClientLoginAcknowledgeLoginPacket read(@NonNull ByteBuf buf,
                                                           @NonNull JetRegistryManager registryManager) {
        return new ClientLoginAcknowledgeLoginPacket();
    }
}