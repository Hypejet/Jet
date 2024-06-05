package net.hypejet.jet.server.network.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.handshake.HandshakePacketReader;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientEncryptionResponsePacketReader;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginAcknowledgePacketReader;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginRequestPacketReader;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientPluginMessageResponsePacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain NetworkCodec network codecs}, which can read and write
 * {@linkplain ClientPacket client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 * @see NetworkCodec
 */
public final class ClientPacketRegistry {

    private static final Map<ProtocolState, IntObjectMap<NetworkCodec<? extends ClientPacket>>> packetReaders;

    static {
        packetReaders = new EnumMap<>(ProtocolState.class);

        IntObjectMap<NetworkCodec<? extends ClientPacket>> handshake = new IntObjectHashMap<>();
        handshake.put(0, new HandshakePacketReader());

        IntObjectMap<NetworkCodec<? extends ClientPacket>> login = new IntObjectHashMap<>();
        login.put(0, new ClientLoginRequestPacketReader());
        login.put(1, new ClientEncryptionResponsePacketReader());
        login.put(2, new ClientPluginMessageResponsePacketReader());
        login.put(3, new ClientLoginAcknowledgePacketReader());

        packetReaders.put(ProtocolState.HANDSHAKE, handshake);
        packetReaders.put(ProtocolState.LOGIN, login);
    }

    private ClientPacketRegistry() {}

    /**
     * Gets a {@linkplain NetworkCodec network codec}, which can read a specific packet and reads it using the codec
     * from a {@linkplain ByteBuf byte buf}.
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @param buf the byte buf
     * @return the packet, which may be null if the packet reader is not present
     * @since 1.0
     */
    public static @Nullable ClientPacket read(int packetId, @NonNull ProtocolState state, @NonNull ByteBuf buf) {
        IntObjectMap<NetworkCodec<? extends ClientPacket>> codecMap = packetReaders.get(state);
        if (codecMap == null) return null;

        NetworkCodec<? extends ClientPacket> codec = codecMap.get(packetId);
        if (codec == null) return null;

        return codec.read(buf);
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
        IntObjectMap<NetworkCodec<? extends ClientPacket>> codecMap = packetReaders.get(state);
        if (codecMap == null) return false;
        return codecMap.containsKey(packetId);
    }
}