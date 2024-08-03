package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ByteArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientChatSessionUpdatePlayPacket chat session update play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChatSessionUpdatePlayPacket
 * @see ClientPacketCodec
 */
public final class ClientChatSessionUpdatePlayPacketCodec
        extends ClientPacketCodec<ClientChatSessionUpdatePlayPacket> {

    private static final ByteArrayNetworkCodec PUBLIC_KEY_CODEC = ByteArrayNetworkCodec.create(512);
    private static final ByteArrayNetworkCodec KEY_SIGNATURE_CODEC = ByteArrayNetworkCodec.create(4096);

    /**
     * Constructs the {@linkplain ClientChatSessionUpdatePlayPacket chat session update play packet}.
     *
     * @since 1.0
     */
    public ClientChatSessionUpdatePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_PLAYER_SESSION, ClientChatSessionUpdatePlayPacket.class);
    }

    @Override
    public @NonNull ClientChatSessionUpdatePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChatSessionUpdatePlayPacket(UUIDNetworkCodec.instance().read(buf), buf.readLong(),
                PUBLIC_KEY_CODEC.read(buf), KEY_SIGNATURE_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientChatSessionUpdatePlayPacket object) {
        UUIDNetworkCodec.instance().write(buf, object.sessionId());
        buf.writeLong(object.expiresAt());
        PUBLIC_KEY_CODEC.write(buf, object.publicKey());
        KEY_SIGNATURE_CODEC.write(buf, object.keySignature());
    }

    @Override
    public void handle(@NonNull ClientChatSessionUpdatePlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}