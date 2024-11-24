package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientConfirmTeleportationPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientConfirmTeleportationPlayPacket a confirm teleportation play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientConfirmTeleportationPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientConfirmTeleportationPlayPacketHandler
        implements ClientPacketHandler<ClientConfirmTeleportationPlayPacket> {
    @Override
    public @NonNull ClientConfirmTeleportationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientConfirmTeleportationPlayPacket(VarIntNetworkCodec.instance().read(buf));
    }

    @Override
    public void handle(@NonNull ClientConfirmTeleportationPlayPacket packet,
                       @NonNull SessionTask sessionTask) {
        // TODO
    }
}