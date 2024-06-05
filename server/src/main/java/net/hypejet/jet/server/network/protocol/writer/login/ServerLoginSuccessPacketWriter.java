package net.hypejet.jet.server.network.protocol.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessPacket;
import net.hypejet.jet.server.network.codec.codecs.GameProfilePropertiesCodec;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which reads and writes
 * a {@linkplain ServerLoginSuccessPacket login success packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginSuccessPacket
 * @see PacketWriter
 */
public final class ServerLoginSuccessPacketWriter extends PacketWriter<ServerLoginSuccessPacket> {
    /**
     * Constructs the {@linkplain ServerLoginSuccessPacketWriter login success packet writer}.
     *
     * @since 1.0
     */
    public ServerLoginSuccessPacketWriter() {
        super(2, ServerLoginSuccessPacket.class);
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