package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientChatCommandPlayPacket a chat
 * command play packet}.
 *
 * @since 1.0
 * @see ClientChatCommandPlayPacket
 * @see NetworkReader
 */
public final class ClientChatCommandPlayPacketReader implements NetworkReader<ClientChatCommandPlayPacket> {
    /**
     * An instance of the {@linkplain ClientChatCommandPlayPacket client chat command play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientChatCommandPlayPacketReader INSTANCE = new ClientChatCommandPlayPacketReader();

    private ClientChatCommandPlayPacketReader() {}

    @Override
    public @NonNull ClientChatCommandPlayPacket read(@NonNull ByteBuf buf,
                                                     @NonNull JetRegistryManager registryManager) {
        return new ClientChatCommandPlayPacket(StringNetworkCodec.INSTANCE.read(buf, registryManager));
    }
}