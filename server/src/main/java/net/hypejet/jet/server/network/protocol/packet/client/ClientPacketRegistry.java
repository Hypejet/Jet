package net.hypejet.jet.server.network.protocol.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientAcknowledgeFinishConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientCookieResponseConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientInformationConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientKeepAliveConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientKnownPacksConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientPluginMessageConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientPongConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.configuration.ClientResourcePackResponseConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.handshake.HandshakePacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientCookieResponseLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientEncryptionResponseLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginAcknowledgeLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientLoginRequestLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.login.ClientPluginMessageResponseLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientAcknowledgeMessagePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientActionPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientChangeDifficultyPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientChatCommandPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientConfirmTeleportationPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientKeepAlivePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientPluginMessagePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientPositionPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientQueryBlockEntityTagPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientRotationAndPositionPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientRotationPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientSignedChatCommandPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.play.ClientSignedChatMessagePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.status.ClientPingRequestStatusPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.codec.status.ClientServerListRequestStatusPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain ClientPacketCodec client packet codecs}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 * @see ClientPacketCodec
 */
public final class ClientPacketRegistry {

    private static final Map<ProtocolState, IntObjectMap<ClientPacketCodec<?>>> identityToCodecMap;
    private static final Map<Class<? extends ClientPacket>, ClientPacketCodec<?>> classToCodecMap;

    static {
        identityToCodecMap = new EnumMap<>(ProtocolState.class);
        classToCodecMap = new HashMap<>();

        // Handshake packets
        register(ProtocolState.HANDSHAKE, new HandshakePacketCodec());

        // Status packets
        register(ProtocolState.STATUS,  new ClientServerListRequestStatusPacketCodec(),
                new ClientPingRequestStatusPacketCodec());

        // Login packets
        register(ProtocolState.LOGIN, new ClientLoginRequestLoginPacketCodec(),
                new ClientEncryptionResponseLoginPacketCodec(), new ClientPluginMessageResponseLoginPacketCodec(),
                new ClientCookieResponseLoginPacketCodec(), new ClientLoginAcknowledgeLoginPacketCodec());

        // Configuration packets
        register(ProtocolState.CONFIGURATION, new ClientPluginMessageConfigurationPacketCodec(),
                new ClientInformationConfigurationPacketCodec(), new ClientCookieResponseConfigurationPacketCodec(),
                new ClientPongConfigurationPacketCodec(), new ClientResourcePackResponseConfigurationPacketCodec(),
                new ClientKnownPacksConfigurationPacketCodec(), new ClientKeepAliveConfigurationPacketCodec(),
                new ClientAcknowledgeFinishConfigurationPacketCodec());

        // Play packets
        register(ProtocolState.PLAY, new ClientKeepAlivePlayPacketCodec(), new ClientPluginMessagePlayPacketCodec(),
                new ClientRotationAndPositionPlayPacketCodec(), new ClientPositionPlayPacketCodec(),
                new ClientRotationPlayPacketCodec(), new ClientActionPlayPacketCodec(),
                new ClientConfirmTeleportationPlayPacketCodec(), new ClientQueryBlockEntityTagPacketCodec(),
                new ClientChangeDifficultyPlayPacketCodec(), new ClientAcknowledgeMessagePlayPacketCodec(),
                new ClientChatCommandPlayPacketCodec(), new ClientSignedChatCommandPlayPacketCodec(),
                new ClientSignedChatMessagePlayPacketCodec());
    }

    private ClientPacketRegistry() {}

    /**
     * Gets a {@linkplain ClientPacketCodec client packet codec}, which can read a specific packet and reads it from
     * a {@linkplain ByteBuf byte buf} using the codec. .
     *
     * @param packetId an id of the packet
     * @param state a current state of the protocol
     * @param buf the byte buf
     * @return the packet, which may be null if the packet codec is not present
     * @since 1.0
     */
    public static @Nullable ClientPacket read(int packetId, @NonNull ProtocolState state, @NonNull ByteBuf buf) {
        IntObjectMap<ClientPacketCodec<?>> codecMap = identityToCodecMap.get(state);
        if (codecMap == null) return null;

        ClientPacketCodec<?> codec = codecMap.get(packetId);
        if (codec == null) return null;

        return codec.read(buf);
    }

    /**
     * Gets a {@linkplain ClientPacketCodec client packet codec}, which can handle a specific packet.
     *
     * @param packet the packet
     * @param connection a player connection, from which the packet was received
     * @since 1.0
     */
    public static void handle(@NonNull ClientPacket packet, @NonNull SocketPlayerConnection connection) {
        ClientPacketCodec<?> codec = classToCodecMap.get(packet.getClass());
        if (codec == null) return;
        handle0(codec, packet, connection); // Handle the packet with generics
    }

    /**
     * Gets whether the packet registry has a {@linkplain ClientPacketCodec client packet codec} for a specific packet.
     *
     * @param packetId an identifier of the packet
     * @param state a protocol state of the packet
     * @return true if the packet registry has a client packet codec for a specific packet, false otherwise
     * @since 1.0
     */
    public static boolean hasCodec(int packetId, @NonNull ProtocolState state) {
        IntObjectMap<ClientPacketCodec<?>> codecMap = identityToCodecMap.get(state);
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
        IntObjectMap<ClientPacketCodec<?>> map = new IntObjectHashMap<>();
        for (ClientPacketCodec<?> codec : codecs) {
            map.put(codec.getPacketId(), codec);
            classToCodecMap.put(codec.getPacketClass(), codec);
        }
        identityToCodecMap.put(state, map);
    }

    /**
     * Handles a {@linkplain ClientPacket client packet} using a {@linkplain ClientPacketCodec client packet codec}
     * specified using generics.
     *
     * @param codec the client packet codec
     * @param packet the client packet
     * @param connection a player connection, from which the packet was received
     * @param <P> the type of the packet
     * @since 1.0
     */
    private static <P extends ClientPacket> void handle0(@NonNull ClientPacketCodec<P> codec,
                                                         @NonNull ClientPacket packet,
                                                         @NonNull SocketPlayerConnection connection) {
        codec.handle(codec.getPacketClass().cast(packet), connection);
    }
}