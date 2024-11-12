package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientInformationPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.settings.PlayerSettingsCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.PlayTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientInformationPlayPacket client information play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientInformationPlayPacketCodec extends ClientPacketCodec<ClientInformationPlayPacket> {
    /**
     * Constructs a {@linkplain ClientInformationPlayPacketCodec client information configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ClientInformationPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CLIENT_INFORMATION, ClientInformationPlayPacket.class);
    }

    @Override
    public @NonNull ClientInformationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientInformationPlayPacket(PlayerSettingsCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientInformationPlayPacket object) {
        PlayerSettingsCodec.instance().write(buf, object.settings());
    }

    @Override
    public void handle(@NonNull ClientInformationPlayPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof PlayTask playTask))
            throw new IllegalArgumentException("The session task must be a play task");
        playTask.player().settings(packet.settings());
    }
}