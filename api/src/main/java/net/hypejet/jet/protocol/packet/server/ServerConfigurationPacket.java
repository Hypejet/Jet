package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomLinksConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCookieRequestConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPluginMessageConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRemoveResourcePackConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerResetChatConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerStoreCookieConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerTransferConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during
 * a {@linkplain ProtocolState#CONFIGURATION configuration protocol state} only.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerConfigurationPacket extends ServerPacket permits ServerAddResourcePackConfigurationPacket,
        ServerCookieRequestConfigurationPacket, ServerCustomLinksConfigurationPacket,
        ServerCustomReportDetailsConfigurationPacket, ServerDisconnectConfigurationPacket,
        ServerFeatureFlagsConfigurationPacket, ServerFinishConfigurationPacket, ServerKeepAliveConfigurationPacket,
        ServerKnownPacksConfigurationPacket, ServerPingConfigurationPacket, ServerPluginMessageConfigurationPacket,
        ServerRegistryDataConfigurationPacket, ServerRemoveResourcePackConfigurationPacket,
        ServerResetChatConfigurationPacket, ServerStoreCookieConfigurationPacket, ServerTransferConfigurationPacket,
        ServerUpdateTagsConfigurationPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.CONFIGURATION;
    }
}