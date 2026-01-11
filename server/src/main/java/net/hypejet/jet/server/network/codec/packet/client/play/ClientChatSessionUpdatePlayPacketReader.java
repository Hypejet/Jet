package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientChatSessionUpdatePlayPacket a chat session update play packet}.
 *
 * @since 1.0
 * @see ClientChatSessionUpdatePlayPacket
 * @see NetworkReader
 */
public final class ClientChatSessionUpdatePlayPacketReader
        implements NetworkReader<ClientChatSessionUpdatePlayPacket> {

    /**
     * An instance of the {@linkplain ClientChatSessionUpdatePlayPacketReader client chat session update play packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientChatSessionUpdatePlayPacketReader
            INSTANCE = new ClientChatSessionUpdatePlayPacketReader();

    private static final ByteArrayNetworkReader PUBLIC_KEY_READER = new ByteArrayNetworkReader(512);
    private static final ByteArrayNetworkReader KEY_SIGNATURE_READER = new ByteArrayNetworkReader(4096);

    private ClientChatSessionUpdatePlayPacketReader() {}

    @Override
    public @NonNull ClientChatSessionUpdatePlayPacket read(@NonNull ByteBuf buf,
                                                           @NonNull JetRegistryManager registryManager) {
        return new ClientChatSessionUpdatePlayPacket(
                UUIDNetworkCodec.INSTANCE.read(buf, registryManager), buf.readLong(),
                PUBLIC_KEY_READER.read(buf, registryManager), KEY_SIGNATURE_READER.read(buf, registryManager)
        );
    }
}