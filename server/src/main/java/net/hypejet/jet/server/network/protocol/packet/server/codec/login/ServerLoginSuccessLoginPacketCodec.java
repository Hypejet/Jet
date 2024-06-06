package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.GameProfilePropertiesCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec server packet codec}, which reads and writes
 * a {@linkplain ServerLoginSuccessLoginPacket login success login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginSuccessLoginPacket
 * @see PacketCodec
 */
public final class ServerLoginSuccessLoginPacketCodec extends PacketCodec<ServerLoginSuccessLoginPacket> {
    /**
     * Constructs the {@linkplain ServerLoginSuccessLoginPacketCodec login success packet codec}.
     *
     * @since 1.0
     */
    public ServerLoginSuccessLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_SUCCESS, ServerLoginSuccessLoginPacket.class);
    }

    @Override
    public @NonNull ServerLoginSuccessLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerLoginSuccessLoginPacket(
                NetworkUtil.readUniqueId(buf),
                NetworkUtil.readString(buf),
                NetworkUtil.readCollection(buf, GameProfilePropertiesCodec.instance()),
                buf.readBoolean()
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLoginSuccessLoginPacket object) {
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
        NetworkUtil.writeString(buf, object.username());
        NetworkUtil.writeCollection(buf, GameProfilePropertiesCodec.instance(), object.properties());
        buf.writeBoolean(object.strictErrorHandling());
    }
}