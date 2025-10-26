package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPlayerLoadedPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkReader network reader}
 * of {@linkplain ClientPlayerLoadedPlayPacket client player loaded play packet}.
 *
 * @since 1.0
 * @see ClientPlayerLoadedPlayPacket
 * @see NetworkReader
 */
public final class ClientPlayerLoadedPlayPacketReader implements NetworkReader<ClientPlayerLoadedPlayPacket> {

    private static final ClientPlayerLoadedPlayPacket PACKET = new ClientPlayerLoadedPlayPacket();

    /**
     * An instance of the {@linkplain ClientPlayerLoadedPlayPacketReader client player loaded play packet reader}.
     *
     * @since 1.0
     * @see ClientPlayerLoadedPlayPacketReader
     */
    public static final ClientPlayerLoadedPlayPacketReader INSTANCE = new ClientPlayerLoadedPlayPacketReader();

    private ClientPlayerLoadedPlayPacketReader() {}

    @Override
    public @NonNull ClientPlayerLoadedPlayPacket read(@NonNull ByteBuf buf,
                                                      @NonNull JetRegistryManager registryManager) {
        return PACKET;
    }
}