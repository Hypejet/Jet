package net.hypejet.jet.server.network.protocol.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestPacket;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which reads and writes
 * an {@linkplain ServerEncryptionRequestPacket encryption request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEncryptionRequestPacket
 * @see PacketWriter
 */
public final class ServerEncryptionRequestPacketWriter extends PacketWriter<ServerEncryptionRequestPacket> {
    /**
     * Constructs the {@linkplain ServerEncryptionRequestPacketWriter encryption request packet writer}.
     *
     * @since 1.0
     */
    public ServerEncryptionRequestPacketWriter() {
        super(1, ServerEncryptionRequestPacket.class);
    }

    @Override
    public @NonNull ServerEncryptionRequestPacket read(@NonNull ByteBuf buf) {
        return new ServerEncryptionRequestPacket(
                NetworkUtil.readString(buf),
                NetworkUtil.readByteArray(buf),
                NetworkUtil.readByteArray(buf),
                buf.readBoolean()
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEncryptionRequestPacket object) {
        NetworkUtil.writeString(buf, object.serverId());
        NetworkUtil.writeByteArray(buf, object.publicKey());
        NetworkUtil.writeByteArray(buf, object.verifyToken());
        buf.writeBoolean(object.shouldAuthenticate());
    }
}
