package net.hypejet.jet.server.network.protocol.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.handshake.HandshakePacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientCookieResponsePacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientEncryptionResponsePacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginAcknowledgePacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginRequestPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientPluginMessageResponsePacketCodec;
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

        // Handshake
        register(ProtocolState.HANDSHAKE, new HandshakePacketCodec());

        // Login
        register(ProtocolState.LOGIN, new ClientLoginRequestPacketCodec(), new ClientEncryptionResponsePacketCodec(),
                new ClientPluginMessageResponsePacketCodec(), new ClientLoginAcknowledgePacketCodec(),
                new ClientCookieResponsePacketCodec());
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

    /**
     * Registers {@linkplain ClientPacketCodec client packet codecs} to the registry.
     *
     * @param state a protocol state of the packets that the codecs write
     * @param codecs the codecs
     * @since 1.0
     */
    private static void register(@NonNull ProtocolState state, @NonNull ClientPacketCodec<?> @NonNull ... codecs) {
        IntObjectMap<NetworkCodec<? extends ClientPacket>> map = new IntObjectHashMap<>();
        for (ClientPacketCodec<?> codec : codecs)
            map.put(codec.getPacketId(), codec);
        packetReaders.put(state, map);
    }
}