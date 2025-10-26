package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientEndTickPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientEndTickPlayPacket a client end tick play packet}.
 *
 * @since 1.0
 * @see ClientEndTickPlayPacket
 * @see NetworkReader
 */
public final class ClientEndTickPlayPacketReader implements NetworkReader<ClientEndTickPlayPacket> {

    /**
     * An instance of the {@linkplain ClientEndTickPlayPacketReader client end tick play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientEndTickPlayPacketReader INSTANCE = new ClientEndTickPlayPacketReader();

    private static final ClientEndTickPlayPacket PACKET = new ClientEndTickPlayPacket();

    private ClientEndTickPlayPacketReader() {}

    @Override
    public @NonNull ClientEndTickPlayPacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return PACKET;
    }
}