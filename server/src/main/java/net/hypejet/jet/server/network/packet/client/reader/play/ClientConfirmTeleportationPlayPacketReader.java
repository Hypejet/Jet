package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientConfirmTeleportationPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientConfirmTeleportationPlayPacket a confirm teleportation play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientConfirmTeleportationPlayPacket
 * @see NetworkReader
 */
public final class ClientConfirmTeleportationPlayPacketReader
        implements NetworkReader<ClientConfirmTeleportationPlayPacket> {
    @Override
    public @NonNull ClientConfirmTeleportationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientConfirmTeleportationPlayPacket(VarIntNetworkCodec.INSTANCE.read(buf));
    }
}