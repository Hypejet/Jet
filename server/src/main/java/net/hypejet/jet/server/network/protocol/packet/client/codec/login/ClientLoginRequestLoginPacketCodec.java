package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketCodec packet codec}, which reads and writes a {@link ClientLoginRequestLoginPacket login
 * request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginRequestLoginPacket
 * @see PacketCodec
 */
public final class ClientLoginRequestLoginPacketCodec extends PacketCodec<ClientLoginRequestLoginPacket> {
    /**
     * Constructs a {@linkplain ClientLoginRequestLoginPacketCodec login request packet codec}..
     *
     * @since 1.0
     */
    public ClientLoginRequestLoginPacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_REQUEST, ClientLoginRequestLoginPacket.class);
    }

    @Override
    public @NonNull ClientLoginRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginRequestLoginPacket(NetworkUtil.readString(buf), NetworkUtil.readUniqueId(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginRequestLoginPacket object) {
        NetworkUtil.writeString(buf, object.username());
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
    }
}