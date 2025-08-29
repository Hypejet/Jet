package net.hypejet.jet.server.network.packet.packets.server;

import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerAddResourcePackPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerCookieRequestPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerCustomLinksPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerCustomReportDetailsPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerDisconnectPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerKeepAlivePacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerPingPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerPingResponsePacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerPluginMessagePacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerRemoveResourcePackPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerStoreCookiePacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerTransferPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.common.ServerUpdateTagsPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.configuration.ServerFeatureFlagsConfigurationPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.configuration.ServerKnownPacksConfigurationPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.configuration.ServerRegistryDataConfigurationPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.login.ServerDisconnectLoginPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.login.ServerEnableCompressionLoginPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.login.ServerEncryptionRequestLoginPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.login.ServerLoginSuccessLoginPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.login.ServerPluginMessageRequestLoginPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerActionBarPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerCenterChunkPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerChunkAndLightDataPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerChunkBatchFinishedPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerCommandSuggestionsResponsePlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerDeclareCommandsPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerEntityAnimationPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerEntityEventPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerEntityPositionAndRotationPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerEntityPositionPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerEntityRotationPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerEntityVelocityPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerInvalidateChunkPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerJoinGamePlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerObjectiveActionPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerResetScorePlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerRespawnPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerSetObjectiveDisplayedPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerSpawnEntityPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerSynchronizeEntityPositionPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerSynchronizePositionPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerSynchronizeRotationPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerSystemMessagePlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerUpdateBiomesPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerUpdateBlockEntityPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerUpdateBlockStatePlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerUpdateChunkSectionBlockStatesPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerUpdateLightPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerUpdateScorePlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.play.ServerWorldEventPlayPacketWriter;
import net.hypejet.jet.server.network.codec.packet.server.status.ServerListResponseStatusPacketWriter;
import net.hypejet.jet.server.network.packet.identifiers.ServerConfigurationPackets;
import net.hypejet.jet.server.network.packet.identifiers.ServerLoginPackets;
import net.hypejet.jet.server.network.packet.identifiers.ServerPlayPackets;
import net.hypejet.jet.server.network.packet.identifiers.ServerStatusPackets;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerAddResourcePackPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCustomLinksPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCustomReportDetailsPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerDisconnectPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerKeepAlivePacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingResponsePacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerRemoveResourcePackPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerStoreCookiePacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerTransferPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerResetChatConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerPluginMessageRequestLoginPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchFinishedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchStartPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityEventPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityPositionAndRotationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityPositionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityRotationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityVelocityPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerInvalidateChunkPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerResetScorePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerRespawnPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSetObjectiveDisplayedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSpawnEntityPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizeEntityPositionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizeRotationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBiomesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockEntityPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockStatePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateChunkSectionBlockStatesPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateLightPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateScorePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.status.ServerListResponseStatusPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A registry of {@linkplain NetworkWriter network writers}
 * and identifiers of {@linkplain ServerPacket server packets}.
 *
 * @since 1.0
 * @see ServerPacket
 * @see NetworkWriter
 */
public final class ServerPacketRegistry {

    private static final Map<ProtocolState, ProtocolStateSpecification> SPECIFICATIONS;

    static {
        Map<ProtocolState, ProtocolStateSpecification> specifications = new EnumMap<>(ProtocolState.class);

        specifications.put(
                ProtocolState.STATUS,
                new ProtocolStateSpecification.Builder()
                        .add(
                                ServerStatusPackets.CLIENTBOUND_PONG_RESPONSE,
                                ServerPingResponsePacket.class,
                                ServerPingResponsePacketWriter.INSTANCE
                        )
                        .add(
                                ServerStatusPackets.CLIENTBOUND_STATUS_RESPONSE,
                                ServerListResponseStatusPacket.class,
                                ServerListResponseStatusPacketWriter.INSTANCE
                        )
                        .build()
        );

        specifications.put(
                ProtocolState.LOGIN,
                new ProtocolStateSpecification.Builder()
                        .add(
                                ServerLoginPackets.CLIENTBOUND_COOKIE_REQUEST,
                                ServerCookieRequestPacket.class,
                                ServerCookieRequestPacketWriter.INSTANCE
                        )
                        .add(
                                ServerLoginPackets.CLIENTBOUND_LOGIN_DISCONNECT,
                                ServerDisconnectPacket.class,
                                ServerDisconnectLoginPacketWriter.INSTANCE
                        )
                        .add(
                                ServerLoginPackets.CLIENTBOUND_HELLO,
                                ServerEncryptionRequestLoginPacket.class,
                                ServerEncryptionRequestLoginPacketWriter.INSTANCE
                        )
                        .add(
                                ServerLoginPackets.CLIENTBOUND_LOGIN_FINISHED,
                                ServerLoginSuccessLoginPacket.class,
                                ServerLoginSuccessLoginPacketWriter.INSTANCE
                        )
                        .add(
                                ServerLoginPackets.CLIENTBOUND_LOGIN_COMPRESSION,
                                ServerEnableCompressionLoginPacket.class,
                                ServerEnableCompressionLoginPacketWriter.INSTANCE
                        )
                        .add(
                                ServerLoginPackets.CLIENTBOUND_CUSTOM_QUERY,
                                ServerPluginMessageRequestLoginPacket.class,
                                ServerPluginMessageRequestLoginPacketWriter.INSTANCE
                        )
                        .build()
        );

        specifications.put(
                ProtocolState.CONFIGURATION,
                new ProtocolStateSpecification.Builder()
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_PING,
                                ServerPingPacket.class,
                                ServerPingPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_SERVER_LINKS,
                                ServerCustomLinksPacket.class,
                                ServerCustomLinksPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_DISCONNECT,
                                ServerDisconnectPacket.class,
                                ServerDisconnectPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_KEEP_ALIVE,
                                ServerKeepAlivePacket.class,
                                ServerKeepAlivePacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_TRANSFER,
                                ServerTransferPacket.class,
                                ServerTransferPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_STORE_COOKIE,
                                ServerStoreCookiePacket.class,
                                ServerStoreCookiePacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_UPDATE_TAGS,
                                ServerUpdateTagsPacket.class,
                                ServerUpdateTagsPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_COOKIE_REQUEST,
                                ServerCookieRequestPacket.class,
                                ServerCookieRequestPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_CUSTOM_PAYLOAD,
                                ServerPluginMessagePacket.class,
                                ServerPluginMessagePacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_CUSTOM_REPORT_DETAILS,
                                ServerCustomReportDetailsPacket.class,
                                ServerCustomReportDetailsPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_RESOURCE_PACK_PUSH,
                                ServerAddResourcePackPacket.class,
                                ServerAddResourcePackPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_RESOURCE_PACK_POP,
                                ServerRemoveResourcePackPacket.class,
                                ServerRemoveResourcePackPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_SELECT_KNOWN_PACKS,
                                ServerKnownPacksConfigurationPacket.class,
                                ServerKnownPacksConfigurationPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_REGISTRY_DATA,
                                ServerRegistryDataConfigurationPacket.class,
                                ServerRegistryDataConfigurationPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_UPDATE_ENABLED_FEATURES,
                                ServerFeatureFlagsConfigurationPacket.class,
                                ServerFeatureFlagsConfigurationPacketWriter.INSTANCE
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_FINISH_CONFIGURATION,
                                ServerFinishConfigurationPacket.class,
                                (buf, object) -> {}
                        )
                        .add(
                                ServerConfigurationPackets.CLIENTBOUND_RESET_CHAT,
                                ServerResetChatConfigurationPacket.class,
                                (buf, object) -> {}
                        )
                        .build()
        );

        specifications.put(
                ProtocolState.PLAY,
                new ProtocolStateSpecification.Builder()
                        .add(
                                ServerPlayPackets.CLIENTBOUND_PING,
                                ServerPingPacket.class,
                                ServerPingPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_COOKIE_REQUEST,
                                ServerCookieRequestPacket.class,
                                ServerCookieRequestPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_KEEP_ALIVE,
                                ServerKeepAlivePacket.class,
                                ServerKeepAlivePacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_DISCONNECT,
                                ServerDisconnectPacket.class,
                                ServerDisconnectPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_CUSTOM_PAYLOAD,
                                ServerPluginMessagePacket.class,
                                ServerPluginMessagePacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SERVER_LINKS,
                                ServerCustomLinksPacket.class,
                                ServerCustomLinksPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_TRANSFER,
                                ServerTransferPacket.class,
                                ServerTransferPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_STORE_COOKIE,
                                ServerStoreCookiePacket.class,
                                ServerStoreCookiePacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_UPDATE_TAGS,
                                ServerUpdateTagsPacket.class,
                                ServerUpdateTagsPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_PONG_RESPONSE,
                                ServerPingResponsePacket.class,
                                ServerPingResponsePacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_CUSTOM_REPORT_DETAILS,
                                ServerCustomReportDetailsPacket.class,
                                ServerCustomReportDetailsPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_RESOURCE_PACK_PUSH,
                                ServerAddResourcePackPacket.class,
                                ServerAddResourcePackPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_RESOURCE_PACK_POP,
                                ServerRemoveResourcePackPacket.class,
                                ServerRemoveResourcePackPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_LOGIN,
                                ServerJoinGamePlayPacket.class,
                                ServerJoinGamePlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_GAME_EVENT,
                                ServerWorldEventPlayPacket.class,
                                ServerWorldEventPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SYSTEM_CHAT,
                                ServerSystemMessagePlayPacket.class,
                                ServerSystemMessagePlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SET_ACTION_BAR_TEXT,
                                ServerActionBarPlayPacket.class,
                                ServerActionBarPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_TAB_LIST,
                                ServerPlayerListHeaderAndFooterPlayPacket.class,
                                ServerPlayerListHeaderAndFooterPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_PLAYER_POSITION,
                                ServerSynchronizePositionPlayPacket.class,
                                ServerSynchronizePositionPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SET_CHUNK_CACHE_CENTER,
                                ServerCenterChunkPlayPacket.class,
                                ServerCenterChunkPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_COMMANDS,
                                ServerDeclareCommandsPlayPacket.class,
                                ServerDeclareCommandsPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_COMMAND_SUGGESTIONS,
                                ServerCommandSuggestionsResponsePlayPacket.class,
                                ServerCommandSuggestionsResponsePlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT,
                                ServerChunkAndLightDataPlayPacket.class,
                                ServerChunkAndLightDataPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_FORGET_LEVEL_CHUNK,
                                ServerInvalidateChunkPlayPacket.class,
                                ServerInvalidateChunkPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_CHUNK_BATCH_FINISHED,
                                ServerChunkBatchFinishedPlayPacket.class,
                                ServerChunkBatchFinishedPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_RESPAWN,
                                ServerRespawnPlayPacket.class,
                                ServerRespawnPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_PLAYER_ROTATION,
                                ServerSynchronizeRotationPlayPacket.class,
                                ServerSynchronizeRotationPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_BLOCK_UPDATE,
                                ServerUpdateBlockStatePlayPacket.class,
                                ServerUpdateBlockStatePlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_BLOCK_ENTITY_DATA,
                                ServerUpdateBlockEntityPlayPacket.class,
                                ServerUpdateBlockEntityPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_CHUNKS_BIOMES,
                                ServerUpdateBiomesPlayPacket.class,
                                ServerUpdateBiomesPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_LIGHT_UPDATE,
                                ServerUpdateLightPlayPacket.class,
                                ServerUpdateLightPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SET_SCORE,
                                ServerUpdateScorePlayPacket.class,
                                ServerUpdateScorePlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_RESET_SCORE,
                                ServerResetScorePlayPacket.class,
                                ServerResetScorePlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SET_DISPLAY_OBJECTIVE,
                                ServerSetObjectiveDisplayedPlayPacket.class,
                                ServerSetObjectiveDisplayedPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SET_OBJECTIVE,
                                ServerObjectiveActionPlayPacket.class,
                                ServerObjectiveActionPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SECTION_BLOCKS_UPDATE,
                                ServerUpdateChunkSectionBlockStatesPlayPacket.class,
                                ServerUpdateChunkSectionBlockStatesPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_CHUNK_BATCH_START,
                                ServerChunkBatchStartPlayPacket.class,
                                (buf, object) -> {}
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_ANIMATE,
                                ServerEntityAnimationPlayPacket.class,
                                ServerEntityAnimationPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_ADD_ENTITY,
                                ServerSpawnEntityPlayPacket.class,
                                ServerSpawnEntityPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_ENTITY_EVENT,
                                ServerEntityEventPlayPacket.class,
                                ServerEntityEventPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_ENTITY_POSITION_SYNC,
                                ServerSynchronizeEntityPositionPlayPacket.class,
                                ServerSynchronizeEntityPositionPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_MOVE_ENTITY_POS,
                                ServerEntityPositionPlayPacket.class,
                                ServerEntityPositionPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_MOVE_ENTITY_POS_ROT,
                                ServerEntityPositionAndRotationPlayPacket.class,
                                ServerEntityPositionAndRotationPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_MOVE_ENTITY_ROT,
                                ServerEntityRotationPlayPacket.class,
                                ServerEntityRotationPlayPacketWriter.INSTANCE
                        )
                        .add(
                                ServerPlayPackets.CLIENTBOUND_SET_ENTITY_MOTION,
                                ServerEntityVelocityPlayPacket.class,
                                ServerEntityVelocityPlayPacketWriter.INSTANCE
                        )
                        .build()
        );

        SPECIFICATIONS = specifications;
    }

    private ServerPacketRegistry() {}

    /**
     * Gets whether a packet is supported in {@linkplain ProtocolState a protocol state} specified.
     *
     * @param protocolState the protocol state
     * @param packetClass a class of the packet
     * @return {@code true} if the packet is supported in the protocol state specified, {@code false} otherwise
     * @since 1.0
     */
    public static boolean isSupported(@NonNull ProtocolState protocolState,
                                      @NonNull Class<? extends ServerPacket> packetClass) {
        return specificationFor(protocolState, packetClass) != null;
    }

    /**
     * Gets a {@linkplain PacketSpecification packet specification}
     * for the specified packet class and {@linkplain ProtocolState protocol state}.
     *
     * @param protocolState the protocol state during which the packet is written
     * @param packetClass the packet class
     * @return the packet specification, {@code null} if there is no packet specification
     *         for the specified packet class in the specified protocol state
     * @since 1.0
     */
    public static @Nullable PacketSpecification<? extends ServerPacket> specificationFor(
            @NonNull ProtocolState protocolState,
            @NonNull Class<? extends ServerPacket> packetClass
    ) {
        Objects.requireNonNull(protocolState, "protocol state");
        ProtocolStateSpecification stateSpecification = SPECIFICATIONS.get(protocolState);
        if (stateSpecification == null) return null;
        return stateSpecification.specificationFor(packetClass);
    }

    /**
     * A specification of a {@linkplain ServerPacket server packet}.
     *
     * @param packetIdentifier an identifier of the packet
     * @param packetClass a class of the packet
     * @param packetWriter a network writer writing the packet
     * @param <P> a type of the packet
     * @since 1.0
     * @see ServerPacketRegistry
     */
    public record PacketSpecification<P extends ServerPacket>(int packetIdentifier, @NonNull Class<P> packetClass,
                                                              @NonNull NetworkWriter<P> packetWriter) {
        /**
         * Constructs the {@linkplain PacketSpecification packet specification}.
         *
         * @param packetIdentifier an identifier of the packet
         * @param packetClass a class of the packet
         * @param packetWriter a network writer writing the packet
         * @since 1.0
         */
        public PacketSpecification {
            Objects.requireNonNull(packetClass, "packet class");
            Objects.requireNonNull(packetWriter, "packet writer");
        }
    }

    /**
     * A {@linkplain ServerPacket server packet} {@linkplain NetworkWriter network-writer} specification
     * of a {@linkplain ProtocolState protocol state}.
     *
     * @since 1.0
     * @see NetworkWriter
     * @see ProtocolState
     */
    private static final class ProtocolStateSpecification {

        private final Map<Class<? extends ServerPacket>, PacketSpecification<?>> classToSpecificationMap;

        /**
         * Constructs the {@linkplain ProtocolStateSpecification protocol state specification}.
         *
         * @param specifications a map associating server packet classes with packet specifications of these packets
         * @since 1.0
         */
        private ProtocolStateSpecification(
                @NonNull Map<Class<? extends ServerPacket>, PacketSpecification<?>> specifications
        ) {
            this.classToSpecificationMap = Map.copyOf(Objects.requireNonNull(specifications, "specifications"));
        }

        /**
         * Gets a {@linkplain PacketSpecification packet specification} for the specified packet class.
         *
         * @param packetClass the packet class
         * @return the packet specification, {@code null} if there is no packet specification
         *         for the specified packet class
         * @since 1.0
         */
        private @Nullable PacketSpecification<?> specificationFor(@NonNull Class<? extends ServerPacket> packetClass) {
            return this.classToSpecificationMap.get(Objects.requireNonNull(packetClass, "packet class"));
        }

        /**
         * A builder of a {@linkplain ProtocolStateSpecification protocol state specification}.
         *
         * @since 1.0
         * @see ProtocolStateSpecification
         */
        private static final class Builder {

            private final Map<Class<? extends ServerPacket>, PacketSpecification<?>> specifications = new HashMap<>();

            /**
             * Constructs the {@linkplain Builder protocol state specification builder}.
             *
             * @since 1.0
             */
            private Builder() {}

            /**
             * Creates a {@linkplain PacketSpecification packet specification} with data specified
             * and adds it to this builder. The packet specification is going to be put to the final
             * {@linkplain ProtocolStateSpecification protocol specification} if it is not overridden.
             *
             * @param identifier an identifier of the packet for which the packet specification is created
             * @param packetClass a class of the packet for which the packet specification is created
             * @param packetWriter a network writer of the packet for which the packet specification is created
             * @return this builder
             * @param <P> a type of the packet
             * @since 1.0
             */
            private <P extends ServerPacket> @NonNull Builder add(int identifier, @NonNull Class<P> packetClass,
                                                                  @NonNull NetworkWriter<P> packetWriter) {
                Objects.requireNonNull(packetClass, "packet class");
                Objects.requireNonNull(packetWriter, "packet writer");
                this.specifications.put(packetClass, new PacketSpecification<>(identifier, packetClass, packetWriter));
                return this;
            }

            /**
             * Builds the {@linkplain ProtocolStateSpecification protocol state specification}.
             *
             * @return the created protocol state specification
             * @since 1.0
             */
            private @NonNull ProtocolStateSpecification build() {
                return new ProtocolStateSpecification(this.specifications);
            }
        }
    }
}