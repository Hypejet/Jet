package net.hypejet.jet.server.network.packet.packets.client;

import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientCookieResponsePacketReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientInformationPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientKeepAlivePacketReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientPingRequestPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientPluginMessagePacketReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientPongPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.common.ClientResourcePackStatePacketReader;
import net.hypejet.jet.server.network.codec.packet.client.configuration.ClientAcknowledgeFinishConfigurationPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.configuration.ClientKnownPacksConfigurationPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.handshake.HandshakePacketReader;
import net.hypejet.jet.server.network.codec.packet.client.login.ClientEncryptionResponseLoginPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.login.ClientLoginAcknowledgeLoginPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.login.ClientLoginRequestLoginPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.login.ClientPluginMessageResponseLoginPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientAcknowledgeMessagePlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientActionPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientChangeDifficultyPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientChatCommandPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientChatSessionUpdatePlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientChunkBatchReceivedPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientCommandSuggestionsRequestPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientConfirmMovementSynchronizationPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientEndTickPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientPlayerInputPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientPositionFlagsPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientPositionPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientQueryBlockEntityTagPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientRequestActionPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientRotationAndPositionPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientRotationPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientSignedChatCommandPlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.play.ClientSignedChatMessagePlayPacketReader;
import net.hypejet.jet.server.network.codec.packet.client.status.ClientServerListRequestStatusPacketReader;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.handler.common.ClientCookieResponsePacketHandler;
import net.hypejet.jet.server.network.packet.handler.common.ClientInformationPacketHandler;
import net.hypejet.jet.server.network.packet.handler.common.ClientKeepAlivePacketHandler;
import net.hypejet.jet.server.network.packet.handler.common.ClientPluginMessagePacketHandler;
import net.hypejet.jet.server.network.packet.handler.common.ClientPongPacketHandler;
import net.hypejet.jet.server.network.packet.handler.common.ClientResourcePackStatePacketHandler;
import net.hypejet.jet.server.network.packet.handler.configuration.ClientAcknowledgeFinishConfigurationPacketHandler;
import net.hypejet.jet.server.network.packet.handler.configuration.ClientKnownPacksConfigurationPacketHandler;
import net.hypejet.jet.server.network.packet.handler.handshake.HandshakePacketHandler;
import net.hypejet.jet.server.network.packet.handler.login.ClientEncryptionResponseLoginPacketHandler;
import net.hypejet.jet.server.network.packet.handler.login.ClientLoginAcknowledgeLoginPacketHandler;
import net.hypejet.jet.server.network.packet.handler.login.ClientLoginRequestLoginPacketHandler;
import net.hypejet.jet.server.network.packet.handler.login.ClientPluginMessageResponseLoginPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientChatCommandPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientChunkBatchReceivedPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientCommandSuggestionsRequestPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientConfirmMovementSynchronizationPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientPositionPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientRotationAndPositionPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.play.ClientRotationPlayPacketHandler;
import net.hypejet.jet.server.network.packet.handler.status.ClientPingRequestStatusPacketHandler;
import net.hypejet.jet.server.network.packet.handler.status.ClientServerListRequestStatusPacketHandler;
import net.hypejet.jet.server.network.packet.identifiers.ClientConfigurationPackets;
import net.hypejet.jet.server.network.packet.identifiers.ClientHandshakePackets;
import net.hypejet.jet.server.network.packet.identifiers.ClientLoginPackets;
import net.hypejet.jet.server.network.packet.identifiers.ClientPlayPackets;
import net.hypejet.jet.server.network.packet.identifiers.ClientStatusPackets;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a registry of {@linkplain NetworkReader network readers}, which read
 * {@linkplain ClientPacket client packets} and {@linkplain ClientPacketHandler client packet handlers}.
 *
 * @since 1.0
 * @see ClientPacket
 * @see NetworkReader
 */
public final class ClientPacketRegistry {

    private static final Map<ProtocolState, IntObjectMap<NetworkReader<? extends ClientPacket>>> READERS;
    private static final Map<Class<? extends ClientPacket>, ClientPacketHandler<?>> HANDLERS = new HandlersBuilder()
            // Handshake packets
            .add(new HandshakePacketHandler())
            // Login packets
            .add(new ClientEncryptionResponseLoginPacketHandler())
            .add(new ClientLoginAcknowledgeLoginPacketHandler())
            .add(new ClientLoginRequestLoginPacketHandler())
            .add(new ClientPluginMessageResponseLoginPacketHandler())
            // Status packets
            .add(new ClientPingRequestStatusPacketHandler())
            .add(new ClientServerListRequestStatusPacketHandler())
            // Configuration packets
            .add(new ClientAcknowledgeFinishConfigurationPacketHandler())
            .add(new ClientKnownPacksConfigurationPacketHandler())
            // Play packets
            .add(new ClientChatCommandPlayPacketHandler())
            .add(new ClientCommandSuggestionsRequestPlayPacketHandler())
            .add(new ClientChunkBatchReceivedPlayPacketHandler())
            .add(new ClientConfirmMovementSynchronizationPlayPacketHandler())
            .add(new ClientPositionPlayPacketHandler())
            .add(new ClientRotationAndPositionPlayPacketHandler())
            .add(new ClientRotationPlayPacketHandler())
            // Common packets
            .add(new ClientCookieResponsePacketHandler())
            .add(new ClientInformationPacketHandler())
            .add(new ClientKeepAlivePacketHandler())
            .add(new ClientPluginMessagePacketHandler())
            .add(new ClientPongPacketHandler())
            .add(new ClientResourcePackStatePacketHandler())
            .build();

    static {
        Map<ProtocolState, IntObjectMap<NetworkReader<? extends ClientPacket>>> readers = new HashMap<>();

        readers.put(
                ProtocolState.HANDSHAKE,
                new IntObjectMapBuilder<NetworkReader<? extends ClientPacket>>()
                        .put(
                                ClientHandshakePackets.CLIENT_INTENTION,
                                HandshakePacketReader.INSTANCE
                        )
                        .build()
        );

        readers.put(
                ProtocolState.STATUS,
                new IntObjectMapBuilder<NetworkReader<? extends ClientPacket>>()
                        .put(
                                ClientStatusPackets.SERVERBOUND_STATUS_REQUEST,
                                ClientServerListRequestStatusPacketReader.INSTANCE)
                        .put(
                                ClientStatusPackets.SERVERBOUND_PING_REQUEST,
                                ClientPingRequestPacketReader.INSTANCE
                        )
                        .build()
        );

        readers.put(
                ProtocolState.LOGIN,
                new IntObjectMapBuilder<NetworkReader<? extends ClientPacket>>()
                        .put(
                                ClientLoginPackets.SERVERBOUND_COOKIE_RESPONSE,
                                ClientCookieResponsePacketReader.INSTANCE
                        )
                        .put(
                                ClientLoginPackets.SERVERBOUND_HELLO,
                                ClientLoginRequestLoginPacketReader.INSTANCE
                        )
                        .put(
                                ClientLoginPackets.SERVERBOUND_KEY,
                                ClientEncryptionResponseLoginPacketReader.INSTANCE
                        )
                        .put(
                                ClientLoginPackets.SERVERBOUND_LOGIN_ACKNOWLEDGED,
                                ClientLoginAcknowledgeLoginPacketReader.INSTANCE
                        )
                        .put(
                                ClientLoginPackets.SERVERBOUND_CUSTOM_QUERY_ANSWER,
                                ClientPluginMessageResponseLoginPacketReader.INSTANCE
                        )
                        .build()
        );

        readers.put(
                ProtocolState.CONFIGURATION,
                new IntObjectMapBuilder<NetworkReader<? extends ClientPacket>>()
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_PONG,
                                ClientPongPacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_KEEP_ALIVE,
                                ClientKeepAlivePacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_CUSTOM_PAYLOAD,
                                ClientPluginMessagePacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_CLIENT_INFORMATION,
                                ClientInformationPacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_COOKIE_RESPONSE,
                                ClientCookieResponsePacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_RESOURCE_PACK,
                                ClientResourcePackStatePacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_FINISH_CONFIGURATION,
                                ClientAcknowledgeFinishConfigurationPacketReader.INSTANCE
                        )
                        .put(
                                ClientConfigurationPackets.SERVERBOUND_SELECT_KNOWN_PACKS,
                                ClientKnownPacksConfigurationPacketReader.INSTANCE
                        )
                        .build()
        );

        readers.put(
                ProtocolState.PLAY,
                new IntObjectMapBuilder<NetworkReader<? extends ClientPacket>>()
                        .put(
                                ClientPlayPackets.SERVERBOUND_KEEP_ALIVE,
                                ClientKeepAlivePacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CUSTOM_PAYLOAD,
                                ClientPluginMessagePacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CLIENT_INFORMATION,
                                ClientInformationPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_PONG,
                                ClientPongPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_COOKIE_RESPONSE,
                                ClientCookieResponsePacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_RESOURCE_PACK,
                                ClientResourcePackStatePacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_PING_REQUEST,
                                ClientPingRequestPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_MOVE_PLAYER_POS_ROT,
                                ClientRotationAndPositionPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_MOVE_PLAYER_ROT,
                                ClientRotationPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_MOVE_PLAYER_POS,
                                ClientPositionPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY,
                                ClientPositionFlagsPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_PLAYER_COMMAND,
                                ClientActionPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_BLOCK_ENTITY_TAG_QUERY,
                                ClientQueryBlockEntityTagPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHANGE_DIFFICULTY,
                                ClientChangeDifficultyPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHAT_ACK,
                                ClientAcknowledgeMessagePlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHAT_COMMAND,
                                ClientChatCommandPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHAT_COMMAND_SIGNED,
                                ClientSignedChatCommandPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHAT,
                                ClientSignedChatMessagePlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHAT_SESSION_UPDATE,
                                ClientChatSessionUpdatePlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CLIENT_COMMAND,
                                ClientRequestActionPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CLIENT_TICK_END,
                                ClientEndTickPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_PLAYER_INPUT,
                                ClientPlayerInputPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_CHUNK_BATCH_RECEIVED,
                                ClientChunkBatchReceivedPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_ACCEPT_TELEPORTATION,
                                ClientConfirmMovementSynchronizationPlayPacketReader.INSTANCE
                        )
                        .put(
                                ClientPlayPackets.SERVERBOUND_COMMAND_SUGGESTION,
                                ClientCommandSuggestionsRequestPlayPacketReader.INSTANCE
                        )
                        .build()
        );

        READERS = Map.copyOf(readers);
    }

    private ClientPacketRegistry() {}

    /**
     * Gets a {@linkplain NetworkReader client packet network-reader} for the specified packet identifier
     * and {@linkplain ProtocolState protocol state}.
     *
     * @param packetId the packet identifier
     * @param protocolState the protocol state during which the packet is being read
     * @return the client packet network-reader, {@code null} if there is no network reader
     *         for the specified packet identifier in the specified protocol state
     * @since 1.0
     */
    public static @Nullable NetworkReader<? extends ClientPacket> readerFor(int packetId,
                                                                            @NonNull ProtocolState protocolState) {
        Objects.requireNonNull(protocolState, "protocol state");
        IntObjectMap<NetworkReader<? extends ClientPacket>> readers = READERS.get(protocolState);
        if (readers == null) return null;
        return readers.get(packetId);
    }

    /**
     * Gets {@linkplain ClientPacketHandler a client packet handler} for a client packet with class specified.
     *
     * @param packetClass the class
     * @return the client packet handler
     * @since 1.0
     */
    public static @Nullable ClientPacketHandler<?> handlerFor(@NonNull Class<? extends ClientPacket> packetClass) {
        return HANDLERS.get(packetClass);
    }

    /**
     * A builder of a {@linkplain Map map} associating classes of {@linkplain ClientPacket client packets}
     * with their {@linkplain ClientPacketHandler client packet handlers}.
     *
     * @since 1.0
     * @see ClientPacket
     * @see ClientPacketHandler
     * @see Map
     */
    private static final class HandlersBuilder {

        private final Map<Class<? extends ClientPacket>, ClientPacketHandler<?>> handlers = new HashMap<>();

        /**
         * Registers the specified {@linkplain ClientPacketHandler client packet handler} to be put
         * into the {@linkplain Map map} associated with this {@linkplain HandlersBuilder handlers builder}.
         *
         * @param handler the client packet handler to register
         * @return this handlers builder
         * @param <P> a type of the client packet of the specified client packet handler
         * @since 1.0
         */
        private <P extends ClientPacket> @NonNull HandlersBuilder add(@NonNull ClientPacketHandler<P> handler) {
            this.handlers.put(handler.packetClass(), handler);
            return this;
        }

        /**
         * Builds the {@linkplain Map map}.
         *
         * @return the created map
         * @since 1.0
         */
        private @NonNull Map<Class<? extends ClientPacket>, ClientPacketHandler<?>> build() {
            return Map.copyOf(this.handlers);
        }
    }
}