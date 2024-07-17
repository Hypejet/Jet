package net.hypejet.jet.server.network.protocol.packet.server;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerResetChatConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.EmptyPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerAddResourcePackConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerCookieRequestConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerCustomLinksConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerCustomReportDetailsConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerDisconnectConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerFeatureFlagsConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerKeepAliveConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerKnownPacksConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerPingConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerPluginMessageConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerRegistryDataConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerRemoveResourcePackConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerStoreCookieConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerTransferConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.configuration.ServerUpdateTagsConfigurationPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerCookieRequestLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerDisconnectLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerEnableCompressionLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerEncryptionRequestLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerLoginSuccessLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerPlayerListHeaderAndFooterPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerPluginMessageRequestLoginPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerActionBarPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerDisconnectPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerGameEventPlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerJoinGamePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerKeepAlivePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerPluginMessagePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.play.ServerSystemMessagePlayPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.status.ServerListResponseStatusPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.status.ServerPingResponseStatusPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain PacketCodec packet codecs}, which can read and write {@linkplain ServerPacket
 * server packets}..
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see NetworkCodec
 */
public final class ServerPacketRegistry {

    private static final Map<Class<? extends ServerPacket>, PacketCodec<? extends ServerPacket>> packetCodecs;

    static {
        packetCodecs = new HashMap<>();

        // Status packets
        register(new ServerListResponseStatusPacketCodec(), new ServerPingResponseStatusPacketCodec());

        // Login packets
        register(new ServerDisconnectLoginPacketCodec(), new ServerEncryptionRequestLoginPacketCodec(),
                new ServerLoginSuccessLoginPacketCodec(), new ServerEnableCompressionLoginPacketCodec(),
                new ServerPluginMessageRequestLoginPacketCodec(), new ServerCookieRequestLoginPacketCodec());

        // Configuration packets
        register(new ServerAddResourcePackConfigurationPacketCodec(),
                new ServerCookieRequestConfigurationPacketCodec(), new ServerCustomLinksConfigurationPacketCodec(),
                new ServerCustomReportDetailsConfigurationPacketCodec(), new ServerDisconnectConfigurationPacketCodec(),
                new ServerFeatureFlagsConfigurationPacketCodec(), new ServerKeepAliveConfigurationPacketCodec(),
                new ServerKnownPacksConfigurationPacketCodec(), new ServerPingConfigurationPacketCodec(),
                new ServerPluginMessageConfigurationPacketCodec(), new ServerRegistryDataConfigurationPacketCodec(),
                new ServerRemoveResourcePackConfigurationPacketCodec(), new ServerStoreCookieConfigurationPacketCodec(),
                new ServerTransferConfigurationPacketCodec(), new ServerUpdateTagsConfigurationPacketCodec(),
                new EmptyPacketCodec<>(ServerPacketIdentifiers.CONFIGURATION_FINISH_CONFIGURATION,
                        ServerFinishConfigurationPacket.class, new ServerFinishConfigurationPacket()),
                new EmptyPacketCodec<>(ServerPacketIdentifiers.CONFIGURATION_RESET_CHAT,
                        ServerResetChatConfigurationPacket.class, new ServerResetChatConfigurationPacket()));

        // Play packet
        register(new ServerKeepAlivePlayPacketCodec(), new ServerDisconnectPlayPacketCodec(),
                new ServerJoinGamePlayPacketCodec(), new ServerPluginMessagePlayPacketCodec(),
                new ServerGameEventPlayPacketCodec(), new ServerSystemMessagePlayPacketCodec(),
                new ServerActionBarPlayPacketCodec(), new ServerPlayerListHeaderAndFooterPlayPacketCodec());
    }

    private ServerPacketRegistry() {}

    /**
     * Gets a {@linkplain PacketCodec packet codec} for a packet specified, which was registered in this registry.
     *
     * @param packetClass a class of the packet
     * @return the packet codec, {@code null} if not present
     * @since 1.0
     */
    public static @Nullable PacketCodec<? extends ServerPacket> codec(
            @NonNull Class<? extends ServerPacket> packetClass
    ) {
        return packetCodecs.get(packetClass);
    }

    /**
     * Registers {@linkplain PacketCodec packet codecs} to the registry.
     *
     * @param codecs the packet codecs
     * @since 1.0
     */
    @SafeVarargs
    private static void register(@NonNull PacketCodec<? extends ServerPacket> @NonNull ... codecs) {
        for (PacketCodec<? extends ServerPacket> codec : codecs)
            packetCodecs.put(codec.getPacketClass(), codec);
    }
}