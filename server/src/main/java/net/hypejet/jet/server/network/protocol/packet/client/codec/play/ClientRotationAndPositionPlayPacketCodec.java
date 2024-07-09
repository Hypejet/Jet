package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationAndPositionPlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientRotationAndPositionPlayPacket rotation and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationAndPositionPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientRotationAndPositionPlayPacketCodec
        extends ClientPacketCodec<ClientRotationAndPositionPlayPacket> {
    /**
     * Constructs the {@linkplain ClientRotationAndPositionPlayPacketCodec rotation and position play packet codec}.
     *
     * @since 1.0
     */
    public ClientRotationAndPositionPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_ROTATION_AND_POSITION, ClientRotationAndPositionPlayPacket.class);
    }

    @Override
    public @NonNull ClientRotationAndPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationAndPositionPlayPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readFloat(), buf.readFloat(), buf.readBoolean());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientRotationAndPositionPlayPacket object) {
        buf.writeDouble(object.x());
        buf.writeDouble(object.feetY());
        buf.writeDouble(object.z());
        buf.writeFloat(object.yaw());
        buf.writeFloat(object.pitch());
        buf.writeBoolean(object.onGround());
    }

    @Override
    public void handle(@NonNull ClientRotationAndPositionPlayPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}