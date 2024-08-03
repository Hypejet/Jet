package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.component.JsonComponentNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerDisconnectLoginPacket
 * disconnect login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectLoginPacket
 * @see PacketCodec
 */
public final class ServerDisconnectLoginPacketCodec extends PacketCodec<ServerDisconnectLoginPacket> {
    /**
     * Constructs the {@linkplain ServerDisconnectLoginPacketCodec disconnect packet codec}.
     *
     * @since 1.0
     */
    public ServerDisconnectLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_DISCONNECT, ServerDisconnectLoginPacket.class);
    }

    @Override
    public @NonNull ServerDisconnectLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerDisconnectLoginPacket(JsonComponentNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectLoginPacket object) {
        JsonComponentNetworkCodec.instance().write(buf, object.reason());
    }
}