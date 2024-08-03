package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientKeepAlivePlayPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetPlaySession;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientKeepAlivePlayPacket keep alive play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAlivePlayPacket
 * @see ClientPacketCodec
 */
public final class ClientKeepAlivePlayPacketCodec extends ClientPacketCodec<ClientKeepAlivePlayPacket> {
    /**
     * Constructs the {@linkplain ClientKeepAlivePlayPacketCodec keep alive play packet codec}.
     *
     * @since 1.0
     */
    public ClientKeepAlivePlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_KEEP_ALIVE, ClientKeepAlivePlayPacket.class);
    }

    @Override
    public @NonNull ClientKeepAlivePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientKeepAlivePlayPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientKeepAlivePlayPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }

    @Override
    public void handle(@NonNull ClientKeepAlivePlayPacket packet, @NonNull SocketPlayerConnection connection) {
        JetPlaySession session = JetPlaySession.asPlaySession(connection.getSession());
        session.keepAliveHandler().handleKeepAlive(packet.keepAliveIdentifier());
    }
}