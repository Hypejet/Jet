package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientRequestActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRequestActionPlayPacket.Action;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientRequestActionPlayPacket request action play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRequestActionPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientRequestActionPlayPacketCodec extends ClientPacketCodec<ClientRequestActionPlayPacket> {

    private static final EnumVarIntCodec<Action> ACTION_CODEC = EnumVarIntCodec.builder(Action.class)
            .add(Action.PERFORM_RESPAWN, 0)
            .add(Action.REQUEST_STATS, 1)
            .build();

    /**
     * Constructs the {@linkplain ClientRequestActionPlayPacket request action play packet}.
     *
     * @since 1.0
     */
    public ClientRequestActionPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CLIENT_REQUEST_ACTION, ClientRequestActionPlayPacket.class);
    }

    @Override
    public @NonNull ClientRequestActionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRequestActionPlayPacket(ACTION_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientRequestActionPlayPacket object) {
        ACTION_CODEC.write(buf, object.action());
    }

    @Override
    public void handle(@NonNull ClientRequestActionPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}