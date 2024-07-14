package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.signing.SeenMessagesNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientSignedChatMessagePlayPacket signed chat message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientSignedChatMessagePlayPacket
 * @see ClientPacketCodec
 */
public final class ClientSignedChatMessagePlayPacketCodec
        extends ClientPacketCodec<ClientSignedChatMessagePlayPacket> {

    private static final int MAX_MESSAGE_LENGTH = 256;

    /**
     * Constructs the {@linkplain ClientPacketCodec client paket codec}.
     *
     * @since 1.0
     */
    public ClientSignedChatMessagePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CHAT_MESSAGE, ClientSignedChatMessagePlayPacket.class);
    }

    @Override
    public @NonNull ClientSignedChatMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientSignedChatMessagePlayPacket(NetworkUtil.readString(buf, MAX_MESSAGE_LENGTH), buf.readLong(),
                buf.readLong(), buf.readBoolean() ? NetworkUtil.readBytes(buf, MAX_MESSAGE_LENGTH) : null,
                SeenMessagesNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientSignedChatMessagePlayPacket object) {
        NetworkUtil.writeString(buf, object.message(), MAX_MESSAGE_LENGTH);
        buf.writeLong(object.timestamp());
        buf.writeLong(object.salt());

        byte[] signature = object.signature();
        buf.writeBoolean(signature != null);

        if (signature != null) {
            buf.writeBytes(signature);
        }

        SeenMessagesNetworkCodec.instance().write(buf, object.seenMessages());
    }

    @Override
    public void handle(@NonNull ClientSignedChatMessagePlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}