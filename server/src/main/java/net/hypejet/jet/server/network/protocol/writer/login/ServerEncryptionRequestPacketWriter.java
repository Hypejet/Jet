package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.encryption.ServerEncryptionRequestPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which writes a {@linkplain ServerEncryptionRequestPacket
 * encryption request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEncryptionRequestPacket
 * @see PacketWriter
 */
public final class ServerEncryptionRequestPacketWriter extends PacketWriter<ServerEncryptionRequestPacket> {
    /**
     * Constructs a {@linkplain ServerEncryptionRequestPacketWriter encryption request packet writer}.
     *
     * @since 1.0
     */
    public ServerEncryptionRequestPacketWriter() {
        super(1, ProtocolState.LOGIN, ServerEncryptionRequestPacket.class);
    }

    @Override
    public void write(@NonNull ServerEncryptionRequestPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeString(packet.serverId());
        buffer.writeByteArray(packet.publicKey());
        buffer.writeByteArray(packet.verifyToken());
        buffer.writeBoolean(packet.shouldAuthenticate());
    }
}
