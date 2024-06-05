package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestPacket;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.server.codec.ServerPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacketCodec server packet codec}, which reads and writes
 * an {@linkplain ServerEncryptionRequestPacket encryption request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEncryptionRequestPacket
 * @see ServerPacketCodec
 */
public final class ServerEncryptionRequestPacketCodec extends ServerPacketCodec<ServerEncryptionRequestPacket> {
    /**
     * Constructs the {@linkplain ServerEncryptionRequestPacketCodec encryption request packet codec}.
     *
     * @since 1.0
     */
    public ServerEncryptionRequestPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_ENCRYPTION_REQUEST, ServerEncryptionRequestPacket.class);
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
