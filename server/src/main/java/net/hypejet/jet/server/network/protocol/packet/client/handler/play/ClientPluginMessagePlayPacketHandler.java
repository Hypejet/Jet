package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientPluginMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.PlayTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientPluginMessagePlayPacket a plugin message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessagePlayPacket
 * @see ClientPacketHandler
 */
public final class ClientPluginMessagePlayPacketHandler implements ClientPacketHandler<ClientPluginMessagePlayPacket> {
    @Override
    public @NonNull ClientPluginMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessagePlayPacket(
                PackedKeyNetworkCodec.INSTANCE.read(buf),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientPluginMessagePlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof PlayTask playTask))
            throw new IllegalArgumentException("The session task must be a play task");
        playTask.player().handlePluginMessage(packet.identifier(), packet.data());
    }
}
