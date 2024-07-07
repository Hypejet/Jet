package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * an {@linkplain ServerEncryptionRequestLoginPacket encryption request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEncryptionRequestLoginPacket
 * @see PacketCodec
 */
public final class ServerEncryptionRequestLoginPacketCodec extends PacketCodec<ServerEncryptionRequestLoginPacket> {

    private static final int MAX_SERVER_ID_LENGTH = 20;

    /**
     * Constructs the {@linkplain ServerEncryptionRequestLoginPacketCodec encryption request packet codec}.
     *
     * @since 1.0
     */
    public ServerEncryptionRequestLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_ENCRYPTION_REQUEST, ServerEncryptionRequestLoginPacket.class);
    }

    @Override
    public @NonNull ServerEncryptionRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerEncryptionRequestLoginPacket(
                NetworkUtil.readString(buf, MAX_SERVER_ID_LENGTH),
                NetworkUtil.readByteArray(buf),
                NetworkUtil.readByteArray(buf),
                buf.readBoolean()
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEncryptionRequestLoginPacket object) {
        NetworkUtil.writeString(buf, object.serverId(), MAX_SERVER_ID_LENGTH);
        NetworkUtil.writeByteArray(buf, object.publicKey());
        NetworkUtil.writeByteArray(buf, object.verifyToken());
        buf.writeBoolean(object.shouldAuthenticate());
    }
}
