package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link NetworkCodec network codec}, which reads and writes a {@link ClientLoginRequestPacket login
 * request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginRequestPacket
 * @see NetworkCodec
 */
public final class ClientLoginRequestPacketReader implements NetworkCodec<ClientLoginRequestPacket> {
    @Override
    public @NonNull ClientLoginRequestPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginRequestPacket(NetworkUtil.readString(buf), NetworkUtil.readUniqueId(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginRequestPacket object) {
        NetworkUtil.writeString(buf, object.username());
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
    }
}