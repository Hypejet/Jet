package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientEndTickPlayPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientEndTickPlayPacket a client end tick play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEndTickPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientEndTickPlayPacketHandler implements ClientPacketHandler<ClientEndTickPlayPacket> {
    @Override
    public @NonNull ClientEndTickPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientEndTickPlayPacket();
    }

    @Override
    public void handle(@NonNull ClientEndTickPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}