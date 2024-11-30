package net.hypejet.jet.server.network.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.common.ServerDisconnectPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.JsonComponentNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerDisconnectPacket a disconnect login packet} during a login protocol state.
 *
 * <p>This exists since server disconnect packet during login protocol state is serialized using another component
 * serializer.</p>
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.network.ProtocolState#LOGIN
 * @see ServerDisconnectPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectLoginPacketWriter implements NetworkWriter<ServerDisconnectPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPacket object) {
        JsonComponentNetworkWriter.INSTANCE.write(buf, object.reason());
    }
}