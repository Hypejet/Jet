package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ByteArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
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

    private static final StringNetworkCodec SERVER_ID_CODEC = StringNetworkCodec.create(20);

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
        return new ServerEncryptionRequestLoginPacket(SERVER_ID_CODEC.read(buf),
                ByteArrayNetworkCodec.instance().read(buf), ByteArrayNetworkCodec.instance().read(buf),
                buf.readBoolean());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEncryptionRequestLoginPacket object) {
        SERVER_ID_CODEC.write(buf, object.serverId());
        ByteArrayNetworkCodec.instance().write(buf, object.publicKey());
        ByteArrayNetworkCodec.instance().write(buf, object.verifyToken());
        buf.writeBoolean(object.shouldAuthenticate());
    }
}
