package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponsePacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketCodec client packet codec}, which reads and writes a {@link ClientEncryptionResponsePacket
 * encryption response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEncryptionResponsePacket
 * @see PacketCodec
 */
public final class ClientEncryptionResponsePacketCodec extends PacketCodec<ClientEncryptionResponsePacket> {
    /**
     * Constructs the {@linkplain ClientEncryptionResponsePacketCodec encryption response packet codec}.
     *
     * @since 1.0
     */
    public ClientEncryptionResponsePacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_ENCRYPTION_RESPONSE, ClientEncryptionResponsePacket.class);
    }

    @Override
    public @NonNull ClientEncryptionResponsePacket read(@NonNull ByteBuf buf) {
        return new ClientEncryptionResponsePacket(NetworkUtil.readByteArray(buf), NetworkUtil.readByteArray(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientEncryptionResponsePacket object) {
        NetworkUtil.writeByteArray(buf, object.sharedSecret());
        NetworkUtil.writeByteArray(buf, object.verifyToken());
    }
}