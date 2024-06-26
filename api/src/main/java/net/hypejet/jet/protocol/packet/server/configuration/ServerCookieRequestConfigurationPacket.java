package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which requests a client to send
 * a cookie data.
 *
 * @param identifier an identifier of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerStoreCookieConfigurationPacket
 * @see ServerConfigurationPacket
 */
public record ServerCookieRequestConfigurationPacket(@NonNull Key identifier) implements ServerConfigurationPacket {}