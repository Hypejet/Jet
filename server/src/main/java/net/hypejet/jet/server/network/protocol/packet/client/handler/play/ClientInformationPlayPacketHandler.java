package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientInformationPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.settings.PlayerSettingsCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.PlayTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handler
 * {@linkplain ClientInformationPlayPacket a client information play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientInformationPlayPacketHandler implements ClientPacketHandler<ClientInformationPlayPacket> {
    @Override
    public @NonNull ClientInformationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientInformationPlayPacket(PlayerSettingsCodec.instance().read(buf));
    }

    @Override
    public void handle(@NonNull ClientInformationPlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof PlayTask playTask))
            throw new IllegalArgumentException("The session task must be a play task");
        playTask.player().settings(packet.settings());
    }
}