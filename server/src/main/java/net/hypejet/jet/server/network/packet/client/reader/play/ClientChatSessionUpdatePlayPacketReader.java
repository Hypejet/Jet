package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientChatSessionUpdatePlayPacket a chat session update play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChatSessionUpdatePlayPacket
 * @see NetworkReader
 */
public final class ClientChatSessionUpdatePlayPacketReader
        implements NetworkReader<ClientChatSessionUpdatePlayPacket> {

    private static final ByteArrayNetworkReader PUBLIC_KEY_READER = new ByteArrayNetworkReader(512);
    private static final ByteArrayNetworkReader KEY_SIGNATURE_READER = new ByteArrayNetworkReader(4096);

    @Override
    public @NonNull ClientChatSessionUpdatePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChatSessionUpdatePlayPacket(
                UUIDNetworkCodec.INSTANCE.read(buf), buf.readLong(),
                PUBLIC_KEY_READER.read(buf), KEY_SIGNATURE_READER.read(buf)
        );
    }
}