package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientAcknowledgeMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientAcknowledgeMessagePlayPacket acknowledge message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeMessagePlayPacket
 * @see ClientPacketCodec
 */
public final class ClientAcknowledgeMessagePlayPacketCodec extends ClientPacketCodec<ClientAcknowledgeMessagePlayPacket> {
    /**
     * Constructs the {@linkplain ClientAcknowledgeMessagePlayPacketCodec acknowledge message play packet codec}.
     *
     * @since 1.0
     */
    public ClientAcknowledgeMessagePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_ACKNOWLEDGE_MESSAGE, ClientAcknowledgeMessagePlayPacket.class);
    }

    @Override
    public @NonNull ClientAcknowledgeMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientAcknowledgeMessagePlayPacket(VarIntNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientAcknowledgeMessagePlayPacket object) {
        VarIntNetworkCodec.instance().read(buf);
    }

    @Override
    public void handle(@NonNull ClientAcknowledgeMessagePlayPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}