package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientPluginMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedIdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.PlayTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientPluginMessagePlayPacket plugin message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessagePlayPacket
 * @see ClientPacketCodec
 */
public final class ClientPluginMessagePlayPacketCodec extends ClientPacketCodec<ClientPluginMessagePlayPacket> {
    /**
     * Constructs a {@linkplain ClientPluginMessagePlayPacketCodec plugin message configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientPluginMessagePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_PLUGIN_MESSAGE, ClientPluginMessagePlayPacket.class);
    }

    @Override
    public @NonNull ClientPluginMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessagePlayPacket(PackedIdentifierNetworkCodec.instance().read(buf),
                NetworkUtil.readRemainingBytes(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPluginMessagePlayPacket object) {
        PackedIdentifierNetworkCodec.instance().write(buf, object.identifier());
        buf.writeBytes(object.data());
    }

    @Override
    public void handle(@NonNull ClientPluginMessagePlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof PlayTask playTask))
            throw new IllegalArgumentException("The session task must be a play task");
        playTask.player().handlePluginMessage(packet.identifier(), packet.data());
    }
}
