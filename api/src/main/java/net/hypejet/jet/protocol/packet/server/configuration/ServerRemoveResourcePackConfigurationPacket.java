package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet} sent by a server when it wants to
 * remove a resource pack of all of them from the client.
 *
 * @param uniqueId an unique identifier of the resource pack to remove, or {@code null} to remove all of them
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerRemoveResourcePackConfigurationPacket(@Nullable UUID uniqueId)
        implements ServerConfigurationPacket {}