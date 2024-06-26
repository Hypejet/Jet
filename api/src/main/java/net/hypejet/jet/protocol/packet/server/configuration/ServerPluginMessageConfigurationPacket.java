package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent when a server
 * wants to send a plugin message to a client.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerPluginMessageConfigurationPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ServerConfigurationPacket {}