package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientAcknowledgeMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientAcknowledgeMessagePlayPacket a acknowledge message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeMessagePlayPacket
 * @see ClientPacketHandler
 */
public final class ClientAcknowledgeMessagePlayPacketHandler
        implements ClientPacketHandler<ClientAcknowledgeMessagePlayPacket> {
    @Override
    public @NonNull ClientAcknowledgeMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientAcknowledgeMessagePlayPacket(VarIntNetworkCodec.INSTANCE.read(buf));
    }

    @Override
    public void handle(@NonNull ClientAcknowledgeMessagePlayPacket packet,
                       @NonNull SessionTask sessionTask) {
        // TODO
    }
}