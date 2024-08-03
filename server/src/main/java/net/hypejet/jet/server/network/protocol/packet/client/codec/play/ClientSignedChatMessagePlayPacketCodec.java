package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ByteArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.signing.SeenMessagesNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
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

    private static final StringNetworkCodec MESSAGE_CODEC = StringNetworkCodec.create(256);
    private static final ByteArrayNetworkCodec SIGNATURE_CODEC = ByteArrayNetworkCodec.create(256);

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
        return new ClientSignedChatMessagePlayPacket(MESSAGE_CODEC.read(buf), buf.readLong(),
                buf.readLong(), buf.readBoolean() ? SIGNATURE_CODEC.read(buf) : null,
                SeenMessagesNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientSignedChatMessagePlayPacket object) {
        MESSAGE_CODEC.write(buf, object.message());

        buf.writeLong(object.timestamp());
        buf.writeLong(object.salt());

        byte[] signature = object.signature();
        buf.writeBoolean(signature != null);

        if (signature != null) {
            SIGNATURE_CODEC.write(buf, signature);
        }

        SeenMessagesNetworkCodec.instance().write(buf, object.seenMessages());
    }

    @Override
    public void handle(@NonNull ClientSignedChatMessagePlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}