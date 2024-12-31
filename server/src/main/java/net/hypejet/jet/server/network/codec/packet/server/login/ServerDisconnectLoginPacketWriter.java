package net.hypejet.jet.server.network.codec.packet.server.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.JsonComponentNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerDisconnectPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerDisconnectPacket a disconnect login packet} during a login protocol state.
 *
 * <p>This exists since server disconnect packet during login protocol state is serialized using another component
 * serializer.</p>
 *
 * @since 1.0
 * @see ProtocolState#LOGIN
 * @see ServerDisconnectPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectLoginPacketWriter implements NetworkWriter<ServerDisconnectPacket> {

    /**
     * An instance of the {@linkplain ServerDisconnectLoginPacketWriter server disconnect login packet writer}.
     *
     * @since 1.0
     */
    public static final ServerDisconnectLoginPacketWriter INSTANCE = new ServerDisconnectLoginPacketWriter();

    private ServerDisconnectLoginPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPacket object) {
        JsonComponentNetworkWriter.INSTANCE.write(buf, object.reason());
    }
}