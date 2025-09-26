package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientConfirmMovementSynchronizationPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientConfirmMovementSynchronizationPlayPacket a confirm movement synchronization play packet}.
 *
 * @since 1.0
 * @see ClientConfirmMovementSynchronizationPlayPacket
 * @see NetworkReader
 */
public final class ClientConfirmMovementSynchronizationPlayPacketReader
        implements NetworkReader<ClientConfirmMovementSynchronizationPlayPacket> {
    /**
     * An instance of the {@linkplain ClientConfirmMovementSynchronizationPlayPacketReader client confirm position
     * synchronization play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientConfirmMovementSynchronizationPlayPacketReader
            INSTANCE = new ClientConfirmMovementSynchronizationPlayPacketReader();

    private ClientConfirmMovementSynchronizationPlayPacketReader() {}

    @Override
    public @NonNull ClientConfirmMovementSynchronizationPlayPacket read(@NonNull ByteBuf buf,
                                                                        @NonNull JetRegistryManager registryManager) {
        return new ClientConfirmMovementSynchronizationPlayPacket(
                VarIntNetworkCodec.INSTANCE.read(buf, registryManager)
        );
    }
}