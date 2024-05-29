package net.hypejet.jet.server.protocol.listener.login;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponsePacket;
import net.hypejet.jet.server.protocol.listener.PacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketReader packet reader}, which reads
 * a {@link ClientEncryptionResponsePacket encryption response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEncryptionResponsePacket
 * @see PacketReader
 */
public final class ClientEncryptionResponsePacketReader extends PacketReader<ClientEncryptionResponsePacket> {

    /**
     * Constructs a {@link ClientEncryptionResponsePacketReader encryption response packet reader}.
     *
     * @since 1.0
     */
    public ClientEncryptionResponsePacketReader() {
        super(1, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull ClientEncryptionResponsePacket read(@NonNull NetworkBuffer buffer) {
        return new ClientEncryptionResponsePacketImpl(buffer.readByteArray(), buffer.readByteArray());
    }

    /**
     * Represents an implementation of {@link ClientEncryptionResponsePacket encryption response packet}.
     *
     * @param sharedSecret a shared secret value, which is encrypted with a public key of the server
     * @param verifyToken a verify token value, which is encrypted with a public key of the server
     * @since 1.0
     */
    private record ClientEncryptionResponsePacketImpl(byte @NonNull [] sharedSecret, byte @NonNull [] verifyToken)
            implements ClientEncryptionResponsePacket {}
}