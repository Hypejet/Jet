package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.signing.SeenMessagesNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.signing.SignedArgumentNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientSignedChatCommandPlayPacket signed chat command play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientSignedChatCommandPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientSignedChatCommandPlayPacketCodec
        extends ClientPacketCodec<ClientSignedChatCommandPlayPacket> {
    /**
     * Constructs the {@linkplain ClientSignedChatCommandPlayPacket signed chat command play packet}.
     *
     * @since 1.0
     */
    public ClientSignedChatCommandPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_SIGNED_CHAT_COMMAND, ClientSignedChatCommandPlayPacket.class);
    }

    @Override
    public @NonNull ClientSignedChatCommandPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientSignedChatCommandPlayPacket(NetworkUtil.readString(buf), buf.readLong(), buf.readLong(),
                NetworkUtil.readCollection(buf, SignedArgumentNetworkCodec.instance()),
                SeenMessagesNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientSignedChatCommandPlayPacket object) {
        NetworkUtil.writeString(buf, object.command());
        buf.writeLong(object.timestamp());
        buf.writeLong(object.salt());
        NetworkUtil.writeCollection(buf, SignedArgumentNetworkCodec.instance(), object.arguments());
        SeenMessagesNetworkCodec.instance().write(buf, object.seenMessages());
    }

    @Override
    public void handle(@NonNull ClientSignedChatCommandPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}