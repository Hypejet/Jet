package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientAcknowledgeMessagePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientAcknowledgeMessagePlayPacket a acknowledge message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeMessagePlayPacket
 * @see NetworkReader
 */
public final class ClientAcknowledgeMessagePlayPacketReader
        implements NetworkReader<ClientAcknowledgeMessagePlayPacket> {
    @Override
    public @NonNull ClientAcknowledgeMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientAcknowledgeMessagePlayPacket(VarIntNetworkCodec.INSTANCE.read(buf));
    }
}