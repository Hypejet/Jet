package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.position.PositionFlagsCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientRotationPlayPacket rotation and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientRotationPlayPacketCodec extends ClientPacketCodec<ClientRotationPlayPacket> {
    /**
     * Constructs the {@linkplain ClientRotationPlayPacketCodec rotation and position play packet codec}.
     *
     * @since 1.0
     */
    public ClientRotationPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_ROTATION, ClientRotationPlayPacket.class);
    }

    @Override
    public @NonNull ClientRotationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationPlayPacket(buf.readFloat(), buf.readFloat(), PositionFlagsCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientRotationPlayPacket object) {
        buf.writeFloat(object.yaw());
        buf.writeFloat(object.pitch());
        PositionFlagsCodec.instance().write(buf, object.flags());
    }

    @Override
    public void handle(@NonNull ClientRotationPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}