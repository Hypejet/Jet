package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientChatCommandPlayPacket chat command play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChatCommandPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientChatCommandPlayPacketCodec extends ClientPacketCodec<ClientChatCommandPlayPacket> {
    /**
     * Constructs the {@linkplain ClientChatCommandPlayPacketCodec chat command play packet codec}.
     *
     * @since 1.0
     */
    public ClientChatCommandPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CHAT_COMMAND, ClientChatCommandPlayPacket.class);
    }

    @Override
    public @NonNull ClientChatCommandPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChatCommandPlayPacket(StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientChatCommandPlayPacket object) {
        StringNetworkCodec.instance().write(buf, object.commandString());
    }

    @Override
    public void handle(@NonNull ClientChatCommandPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "The player must not be null");
        connection.server().commandManager().execute(packet.commandString(), player);
    }
}