package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientAcknowledgeMessagePacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientAcknowledgeMessagePacket acknowledge message packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeMessagePacket
 * @see ClientPacketCodec
 */
public final class ClientAcknowledgeMessagePlayPacketCodec extends ClientPacketCodec<ClientAcknowledgeMessagePacket> {
    /**
     * Constructs the {@linkplain ClientAcknowledgeMessagePlayPacketCodec acknowledge message play packet codec}.
     *
     * @since 1.0
     */
    public ClientAcknowledgeMessagePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_ACKNOWLEDGE_MESSAGE, ClientAcknowledgeMessagePacket.class);
    }

    @Override
    public @NonNull ClientAcknowledgeMessagePacket read(@NonNull ByteBuf buf) {
        return new ClientAcknowledgeMessagePacket(NetworkUtil.readVarInt(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientAcknowledgeMessagePacket object) {
        NetworkUtil.writeVarInt(buf, object.messageCount());
    }

    @Override
    public void handle(@NonNull ClientAcknowledgeMessagePacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}