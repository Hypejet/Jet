package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientPositionPlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientPositionPlayPacket position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPositionPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientPositionPlayPacketCodec extends ClientPacketCodec<ClientPositionPlayPacket> {
    /**
     * Constructs the {@linkplain ClientPositionPlayPacketCodec rotation and position play packet codec}.
     *
     * @since 1.0
     */
    public ClientPositionPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_POSITION, ClientPositionPlayPacket.class);
    }

    @Override
    public @NonNull ClientPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPositionPlayPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readBoolean());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPositionPlayPacket object) {
        buf.writeDouble(object.x());
        buf.writeDouble(object.feetY());
        buf.writeDouble(object.z());
        buf.writeBoolean(object.onGround());
    }

    @Override
    public void handle(@NonNull ClientPositionPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}