package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during
 * a {@linkplain ProtocolState#CONFIGURATION configuration protocol state} only.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerConfigurationPacket extends ServerPacket permits ServerDisconnectConfigurationPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.CONFIGURATION;
    }
}