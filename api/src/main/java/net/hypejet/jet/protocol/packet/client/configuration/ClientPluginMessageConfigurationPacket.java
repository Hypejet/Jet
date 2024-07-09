package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is received when a client
 * sends a plugin message.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ClientConfigurationPacket
 */
public record ClientPluginMessageConfigurationPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ClientConfigurationPacket {}