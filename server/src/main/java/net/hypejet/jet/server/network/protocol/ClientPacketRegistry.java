package net.hypejet.jet.server.network.protocol;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.listener.PacketReader;
import net.hypejet.jet.server.network.protocol.listener.handshake.HandshakePacketReader;
import net.hypejet.jet.server.network.protocol.listener.login.ClientCookieResponsePacketReader;
import net.hypejet.jet.server.network.protocol.listener.login.ClientEncryptionResponsePacketReader;
import net.hypejet.jet.server.network.protocol.listener.login.ClientLoginAcknowledgePacketReader;
import net.hypejet.jet.server.network.protocol.listener.login.ClientPluginMessageResponsePacketReader;
import net.hypejet.jet.server.network.protocol.listener.login.LoginRequestPacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

/**
 * Represents a registry of a {@linkplain ClientPacket client packet} readers.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ClientPacketRegistry {

    private static final Set<PacketReader<?>> packetReaders = Set.of(
            new HandshakePacketReader(),
            new LoginRequestPacketReader(),
            new ClientLoginAcknowledgePacketReader(),
            new ClientEncryptionResponsePacketReader(),
            new ClientPluginMessageResponsePacketReader(),
            new ClientCookieResponsePacketReader()
    );

    private ClientPacketRegistry() {}

    /**
     * Gets a {@linkplain PacketReader packet reader}, which can read a specific packet and reads it from
     * a {@linkplain NetworkBuffer network buffer}.
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @param buffer the byte buffer
     * @return the packet, which may be null if the packet reader is not present
     * @since 1.0
     */
    public static @Nullable ClientPacket read(int packetId, @NonNull ProtocolState state, @NonNull NetworkBuffer buffer) {
        PacketReader<?> reader = null;

        for (PacketReader<?> packetReader : packetReaders) {
            if (packetReader.getPacketId() != packetId || packetReader.getProtocolState() != state) continue;
            reader = packetReader;
        }

        if (reader == null) return null;
        return reader.read(buffer);
    }


    /**
     * Gets whether the packet registry has a reader for a specific packet.
     *
     * @param packetId an identifier of the packet
     * @param state a protocol state of the packet
     * @return true if the packet registry has a reader for a specific packet, false otherwise
     * @since 1.0
     */
    public static boolean hasPacketReader(int packetId, @NonNull ProtocolState state) {
        for (PacketReader<?> packetReader : packetReaders) {
            if (packetReader.getPacketId() != packetId || packetReader.getProtocolState() != state) continue;
            return true;
        }
        return false;
    }
}