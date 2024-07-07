package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.pack.ResourcePackResult;
import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * to state a result of the resource pack load.
 *
 * @param uniqueId an unique identifier of the resource pack
 * @param result a result of the load
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket
 * @see ResourcePackResult
 * @see ClientConfigurationPacket
 */
public record ClientResourcePackResponseConfigurationPacket(@NonNull UUID uniqueId, @NonNull ResourcePackResult result)
        implements ClientConfigurationPacket {
}