package net.hypejet.jet.server.network.codec.packet.client.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientAcknowledgeFinishConfigurationPacket a client acknowledge finish configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeFinishConfigurationPacket
 * @see NetworkReader
 */
public final class ClientAcknowledgeFinishConfigurationPacketReader
        implements NetworkReader<ClientAcknowledgeFinishConfigurationPacket> {

    private static final ClientAcknowledgeFinishConfigurationPacket
            PACKET = new ClientAcknowledgeFinishConfigurationPacket();

    @Override
    public @NonNull ClientAcknowledgeFinishConfigurationPacket read(@NonNull ByteBuf buf) {
        return PACKET;
    }
}