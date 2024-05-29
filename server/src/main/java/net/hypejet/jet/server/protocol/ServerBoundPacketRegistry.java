package net.hypejet.jet.server.protocol;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.server.protocol.listener.PacketReader;
import net.hypejet.jet.server.protocol.listener.handshake.HandshakePacketReader;
import net.hypejet.jet.server.protocol.listener.login.ClientEncryptionResponsePacketReader;
import net.hypejet.jet.server.protocol.listener.login.ClientLoginAcknowledgePacketReader;
import net.hypejet.jet.server.protocol.listener.login.LoginRequestPacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

/**
 * Represents a registry of a {@link ServerBoundPacket server-bound packet} readers.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ServerBoundPacketRegistry {

    private final Set<PacketReader<?>> packetReaders = Set.of(
            new HandshakePacketReader(),
            new LoginRequestPacketReader(),
            new ClientLoginAcknowledgePacketReader(),
            new ClientEncryptionResponsePacketReader()
    );

    /**
     * Gets a {@link PacketReader packet reader}, which can read a specific packet  and reads it from
     * a {@link NetworkBuffer network buffer}.
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @param buffer the byte buffer
     * @return the packet, which may be null if the packet reader is not present
     * @since 1.0
     */
    public @Nullable ServerBoundPacket read(int packetId, @NonNull ProtocolState state, @NonNull NetworkBuffer buffer) {
        PacketReader<?> packetReader = this.getReader(packetId, state);
        if (packetReader == null) return null;
        return packetReader.read(buffer);
    }

    /**
     * Gets a {@link PacketReader packet reader}, which can read a specific packet.
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @return the packet reader, which may be null if not present
     * @since 1.0
     */
    public @Nullable PacketReader<?> getReader(int packetId, @NonNull ProtocolState state) {
        for (PacketReader<?> packetReader : this.packetReaders) {
            if (packetReader.getPacketId() != packetId || packetReader.getProtocolState() != state) continue;
            return packetReader;
        }
        return null;
    }
}