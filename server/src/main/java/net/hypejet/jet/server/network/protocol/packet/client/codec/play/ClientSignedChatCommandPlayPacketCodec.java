package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.signing.SeenMessagesNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.signing.SignedArgumentNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.signing.SignedArgument;
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

    private static final CollectionNetworkCodec<SignedArgument> ARGUMENT_COLLECTION_CODEC = CollectionNetworkCodec
            .create(SignedArgumentNetworkCodec.instance());

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
        return new ClientSignedChatCommandPlayPacket(StringNetworkCodec.instance().read(buf),
                buf.readLong(), buf.readLong(),
                ARGUMENT_COLLECTION_CODEC.read(buf),
                SeenMessagesNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientSignedChatCommandPlayPacket object) {
        StringNetworkCodec.instance().write(buf, object.command());
        buf.writeLong(object.timestamp());
        buf.writeLong(object.salt());
        ARGUMENT_COLLECTION_CODEC.write(buf, object.arguments());
        SeenMessagesNetworkCodec.instance().write(buf, object.seenMessages());
    }

    @Override
    public void handle(@NonNull ClientSignedChatCommandPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}