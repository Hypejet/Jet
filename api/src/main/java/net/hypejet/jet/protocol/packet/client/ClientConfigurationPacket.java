package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientCookieResponseConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPluginMessageConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet}, which can be received during
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public sealed interface ClientConfigurationPacket extends ClientPacket permits
        ClientAcknowledgeFinishConfigurationPacket, ClientCookieResponseConfigurationPacket,
        ClientInformationConfigurationPacket, ClientKeepAliveConfigurationPacket, ClientKnownPacksConfigurationPacket,
        ClientPluginMessageConfigurationPacket, ClientPongConfigurationPacket,
        ClientResourcePackResponseConfigurationPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.CONFIGURATION;
    }
}