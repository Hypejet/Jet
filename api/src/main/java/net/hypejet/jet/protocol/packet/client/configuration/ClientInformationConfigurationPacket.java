package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet} sent by a client to inform
 * the server about settings of a player.
 *
 * @param settings the settings
 * @since 1.0
 * @author Codestech
 * @see ClientConfigurationPacket
 */
public record ClientInformationConfigurationPacket(Player.@NonNull Settings settings)
        implements ClientConfigurationPacket {}