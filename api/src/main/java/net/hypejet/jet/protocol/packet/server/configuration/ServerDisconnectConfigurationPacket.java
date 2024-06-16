package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet} sent by a server to disconnect
 * a player.
 *
 * @param reason a reason of the disconnection
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerDisconnectConfigurationPacket(@NonNull Component reason) implements ServerConfigurationPacket {}