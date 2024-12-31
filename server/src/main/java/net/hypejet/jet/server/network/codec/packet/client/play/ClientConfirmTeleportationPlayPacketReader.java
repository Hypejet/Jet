package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientConfirmTeleportationPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientConfirmTeleportationPlayPacket a confirm teleportation play packet}.
 *
 * @since 1.0
 * @see ClientConfirmTeleportationPlayPacket
 * @see NetworkReader
 */
public final class ClientConfirmTeleportationPlayPacketReader
        implements NetworkReader<ClientConfirmTeleportationPlayPacket> {

    /**
     * An instance of the {@linkplain ClientConfirmTeleportationPlayPacketReader client confirm teleportation play
     * packet reader}.
     *
     * @since 1.0
     */
    public static final ClientConfirmTeleportationPlayPacketReader
            INSTANCE = new ClientConfirmTeleportationPlayPacketReader();

    private ClientConfirmTeleportationPlayPacketReader() {}

    @Override
    public @NonNull ClientConfirmTeleportationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientConfirmTeleportationPlayPacket(VarIntNetworkCodec.INSTANCE.read(buf));
    }
}