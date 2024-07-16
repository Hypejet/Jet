package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientPluginMessagePlayPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.other.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

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
        return new ClientPluginMessagePlayPacket(IdentifierNetworkCodec.instance().read(buf),
                NetworkUtil.readRemainingBytes(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPluginMessagePlayPacket object) {
        IdentifierNetworkCodec.instance().write(buf, object.identifier());
        buf.writeBytes(object.data());
    }

    @Override
    public void handle(@NonNull ClientPluginMessagePlayPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "Player cannot be null");
        player.handlePluginMessage(packet.identifier(), packet.data());
    }
}
