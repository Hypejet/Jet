package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientOnGroundPlayPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientOnGroundPlayPacket a on ground play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientOnGroundPlayPacket
 */
public final class ClientOnGroundPlayPacketHandler implements ClientPacketHandler<ClientOnGroundPlayPacket> {
    @Override
    public @NonNull ClientOnGroundPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientOnGroundPlayPacket(buf.readBoolean());
    }

    @Override
    public void handle(@NonNull ClientOnGroundPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}