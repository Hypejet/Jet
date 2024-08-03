package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientOnGroundPlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientOnGroundPlayPacket on ground play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientOnGroundPlayPacket
 */
public final class ClientOnGroundPlayPacketCodec extends ClientPacketCodec<ClientOnGroundPlayPacket> {
    /**
     * Constructs the {@linkplain ClientOnGroundPlayPacketCodec on ground play packet codec}.
     *
     * @since 1.0
     */
    public ClientOnGroundPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_ON_GROUND, ClientOnGroundPlayPacket.class);
    }

    @Override
    public @NonNull ClientOnGroundPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientOnGroundPlayPacket(buf.readBoolean());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientOnGroundPlayPacket object) {
        buf.writeBoolean(object.onGround());
    }

    @Override
    public void handle(@NonNull ClientOnGroundPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}