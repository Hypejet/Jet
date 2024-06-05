package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessPacket;
import net.hypejet.jet.server.network.protocol.codecs.GameProfilePropertiesCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.server.codec.ServerPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacketCodec server packet codec}, which reads and writes
 * a {@linkplain ServerLoginSuccessPacket login success packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginSuccessPacket
 * @see ServerPacketCodec
 */
public final class ServerLoginSuccessPacketCodec extends ServerPacketCodec<ServerLoginSuccessPacket> {
    /**
     * Constructs the {@linkplain ServerLoginSuccessPacketCodec login success packet codec}.
     *
     * @since 1.0
     */
    public ServerLoginSuccessPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_SUCCESS, ServerLoginSuccessPacket.class);
    }

    @Override
    public @NonNull ServerLoginSuccessPacket read(@NonNull ByteBuf buf) {
        return new ServerLoginSuccessPacket(
                NetworkUtil.readUniqueId(buf),
                NetworkUtil.readString(buf),
                NetworkUtil.readCollection(buf, GameProfilePropertiesCodec.instance()),
                buf.readBoolean()
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLoginSuccessPacket object) {
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
        NetworkUtil.writeString(buf, object.username());
        NetworkUtil.writeCollection(buf, GameProfilePropertiesCodec.instance(), object.properties());
        buf.writeBoolean(object.strictErrorHandling());
    }
}