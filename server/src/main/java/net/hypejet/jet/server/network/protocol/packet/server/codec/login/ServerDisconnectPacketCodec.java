package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerDisconnectPacket
 * disconnect packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPacket
 * @see PacketCodec
 */
public final class ServerDisconnectPacketCodec extends PacketCodec<ServerDisconnectPacket> {
    /**
     * Constructs the {@linkplain ServerDisconnectPacketCodec disconnect packet codec}.
     *
     * @since 1.0
     */
    public ServerDisconnectPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_DISCONNECT, ServerDisconnectPacket.class);
    }

    @Override
    public @NonNull ServerDisconnectPacket read(@NonNull ByteBuf buf) {
        return new ServerDisconnectPacket(NetworkUtil.readJsonComponent(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPacket object) {
        NetworkUtil.writeJsonComponent(buf, object.reason());
    }
}