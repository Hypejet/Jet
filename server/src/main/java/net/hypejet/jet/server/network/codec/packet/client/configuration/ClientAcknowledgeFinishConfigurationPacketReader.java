package net.hypejet.jet.server.network.codec.packet.client.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientAcknowledgeFinishConfigurationPacket a client acknowledge finish configuration packet}.
 *
 * @since 1.0
 * @see ClientAcknowledgeFinishConfigurationPacket
 * @see NetworkReader
 */
public final class ClientAcknowledgeFinishConfigurationPacketReader
        implements NetworkReader<ClientAcknowledgeFinishConfigurationPacket> {
    /**
     * An instance of the {@linkplain ClientAcknowledgeFinishConfigurationPacketReader client acknowledge finish
     * configuration packet reader}.
     *
     * @since 1.0
     */
    public static final ClientAcknowledgeFinishConfigurationPacketReader
            INSTANCE = new ClientAcknowledgeFinishConfigurationPacketReader();

    private static final ClientAcknowledgeFinishConfigurationPacket
            PACKET = new ClientAcknowledgeFinishConfigurationPacket();

    private ClientAcknowledgeFinishConfigurationPacketReader() {}

    @Override
    public @NonNull ClientAcknowledgeFinishConfigurationPacket read(@NonNull ByteBuf buf,
                                                                    @NonNull JetRegistryManager registryManager) {
        return PACKET;
    }
}