package net.hypejet.jet.server.network.protocol.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientPluginMessageConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.handshake.HandshakePacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientCookieResponseLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientEncryptionResponseLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginAcknowledgeLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginRequestLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientPluginMessageResponseLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.status.ClientPingRequestStatusPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.status.ClientServerListRequestStatusPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain PacketCodec packet codecs}, which can read and write {@linkplain ClientPacket
 * client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 * @see PacketCodec
 */
public final class ClientPacketRegistry {

    private static final Map<ProtocolState, IntObjectMap<PacketCodec<? extends ClientPacket>>> packetCodecs;

    static {
        packetCodecs = new EnumMap<>(ProtocolState.class);

        // Handshake packets
        register(ProtocolState.HANDSHAKE, new HandshakePacketCodec());

        // Status packets
        register(ProtocolState.STATUS,  new ClientServerListRequestStatusPacketCodec(),
                new ClientPingRequestStatusPacketCodec());

        // Login packets
        register(ProtocolState.LOGIN, new ClientLoginRequestLoginPacketCodec(),
                new ClientEncryptionResponseLoginPacketCodec(), new ClientPluginMessageResponseLoginPacketCodec(),
                new ClientLoginAcknowledgeLoginPacketCodec(), new ClientCookieResponseLoginPacketCodec());

        // Configuration packets
        register(ProtocolState.CONFIGURATION, new ClientPluginMessageConfigurationPacketCodec());
    }

    private ClientPacketRegistry() {}

    /**
     * Gets a {@linkplain PacketCodec packet codec}, which can read a specific packet and reads it from
     * a {@linkplain ByteBuf byte buf} using the codec. .
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @param buf the byte buf
     * @return the packet, which may be null if the packet codec is not present
     * @since 1.0
     */
    public static @Nullable ClientPacket read(int packetId, @NonNull ProtocolState state, @NonNull ByteBuf buf) {
        IntObjectMap<PacketCodec<? extends ClientPacket>> codecMap = packetCodecs.get(state);
        if (codecMap == null) return null;

        PacketCodec<? extends ClientPacket> codec = codecMap.get(packetId);
        if (codec == null) return null;

        return codec.read(buf);
    }


    /**
     * Gets whether the packet registry has a {@linkplain PacketCodec packet codec} for a specific packet.
     *
     * @param packetId an identifier of the packet
     * @param state a protocol state of the packet
     * @return true if the packet registry has a client packet codec for a specific packet, false otherwise
     * @since 1.0
     */
    public static boolean hasCodec(int packetId, @NonNull ProtocolState state) {
        IntObjectMap<PacketCodec<? extends ClientPacket>> codecMap = packetCodecs.get(state);
        if (codecMap == null) return false;
        return codecMap.containsKey(packetId);
    }

    /**
     * Registers {@linkplain PacketCodec packet codecs} to the registry.
     *
     * @param state a protocol state of the packets that the codecs write
     * @param codecs the codecs
     * @since 1.0
     */
    @SafeVarargs
    private static void register(@NonNull ProtocolState state,
                                 @NonNull PacketCodec<? extends ClientPacket> @NonNull ... codecs) {
        IntObjectMap<PacketCodec<? extends ClientPacket>> map = new IntObjectHashMap<>();
        for (PacketCodec<? extends ClientPacket> codec : codecs)
            map.put(codec.getPacketId(), codec);
        packetCodecs.put(state, map);
    }
}