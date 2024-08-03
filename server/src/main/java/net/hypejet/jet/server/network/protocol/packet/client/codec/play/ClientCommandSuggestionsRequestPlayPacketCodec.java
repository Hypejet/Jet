package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientCommandSuggestionsRequestPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientCommandSuggestionsRequestPlayPacket command suggestions request play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCommandSuggestionsRequestPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientCommandSuggestionsRequestPlayPacketCodec
        extends ClientPacketCodec<ClientCommandSuggestionsRequestPlayPacket> {

    private static final StringNetworkCodec TEXT_CODEC = StringNetworkCodec.create(32_500);

    /**
     * Constructs the {@linkplain ClientPacketCodec client paket codec}.
     *
     * @since 1.0
     */
    public ClientCommandSuggestionsRequestPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_SUGGESTIONS_REQUEST, ClientCommandSuggestionsRequestPlayPacket.class);
    }

    @Override
    public @NonNull ClientCommandSuggestionsRequestPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientCommandSuggestionsRequestPlayPacket(VarIntNetworkCodec.instance().read(buf),
                TEXT_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientCommandSuggestionsRequestPlayPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.transactionId());
        TEXT_CODEC.write(buf, object.text());
    }

    @Override
    public void handle(@NonNull ClientCommandSuggestionsRequestPlayPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}