package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
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

    private static final int MAX_PUBLIC_KEY_LENGTH = 512;
    private static final int MAX_KEY_SIGNATURE_LENGTH = 4096;

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
        return new ClientChatSessionUpdatePlayPacket(NetworkUtil.readUniqueId(buf), buf.readLong(),
                NetworkUtil.readByteArray(buf, MAX_PUBLIC_KEY_LENGTH),
                NetworkUtil.readByteArray(buf, MAX_KEY_SIGNATURE_LENGTH));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientChatSessionUpdatePlayPacket object) {
        NetworkUtil.writeUniqueId(buf, object.sessionId());
        buf.writeLong(object.expiresAt());
        NetworkUtil.writeByteArray(buf, object.publicKey(), MAX_PUBLIC_KEY_LENGTH);
        NetworkUtil.writeByteArray(buf, object.keySignature(), MAX_KEY_SIGNATURE_LENGTH);
    }

    @Override
    public void handle(@NonNull ClientChatSessionUpdatePlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}