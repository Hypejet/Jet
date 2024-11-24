package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.PlayTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientChatCommandPlayPacket a chat command play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChatCommandPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientChatCommandPlayPacketHandler implements ClientPacketHandler<ClientChatCommandPlayPacket> {
    @Override
    public @NonNull ClientChatCommandPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChatCommandPlayPacket(StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void handle(@NonNull ClientChatCommandPlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof PlayTask playTask))
            throw new IllegalArgumentException("The session task must be a play task");
        JetPlayer player = playTask.player();
        player.server().commandManager().execute(packet.commandString(), player);
    }
}