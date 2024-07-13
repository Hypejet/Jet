package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientConfirmTeleportationPlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientConfirmTeleportationPlayPacket confirm teleportation play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientConfirmTeleportationPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientConfirmTeleportationPlayPacketCodec
        extends ClientPacketCodec<ClientConfirmTeleportationPlayPacket> {
    /**
     * Constructs the {@linkplain ClientConfirmTeleportationPlayPacketCodec confirm teleportation play packet codec}.
     *
     * @since 1.0
     */
    public ClientConfirmTeleportationPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CONFIRM_TELEPORTATION, ClientConfirmTeleportationPlayPacket.class);
    }

    @Override
    public @NonNull ClientConfirmTeleportationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientConfirmTeleportationPlayPacket(NetworkUtil.readVarInt(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientConfirmTeleportationPlayPacket object) {
        NetworkUtil.writeVarInt(buf, object.teleportationId());
    }

    @Override
    public void handle(@NonNull ClientConfirmTeleportationPlayPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}