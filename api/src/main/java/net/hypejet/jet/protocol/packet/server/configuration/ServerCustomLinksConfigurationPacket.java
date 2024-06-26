package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which displays
 * {@linkplain ServerLink server links} in a menu available on a pause menu.
 *
 * @param serverLinks the server links to display
 * @since 1.0
 * @author Codestech
 * @see ServerLink
 * @see ServerConfigurationPacket
 */
public record ServerCustomLinksConfigurationPacket(@NonNull Collection<ServerLink> serverLinks)
        implements ServerConfigurationPacket {}