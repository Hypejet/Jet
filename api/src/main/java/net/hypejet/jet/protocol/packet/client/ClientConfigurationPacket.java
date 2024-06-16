package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPluginMessageConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet}, which can be received during
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public sealed interface ClientConfigurationPacket extends ClientPacket permits ClientPluginMessageConfigurationPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.CONFIGURATION;
    }
}