package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientAcknowledgeMessagePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientAcknowledgeMessagePlayPacket a acknowledge message play packet}.
 *
 * @since 1.0
 * @see ClientAcknowledgeMessagePlayPacket
 * @see NetworkReader
 */
public final class ClientAcknowledgeMessagePlayPacketReader
        implements NetworkReader<ClientAcknowledgeMessagePlayPacket> {

    /**
     * An instance of the {@linkplain ClientAcknowledgeMessagePlayPacketReader client acknowledge message play packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientAcknowledgeMessagePlayPacketReader
            INSTANCE = new ClientAcknowledgeMessagePlayPacketReader();

    private ClientAcknowledgeMessagePlayPacketReader() {}

    @Override
    public @NonNull ClientAcknowledgeMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientAcknowledgeMessagePlayPacket(VarIntNetworkCodec.INSTANCE.read(buf));
    }
}