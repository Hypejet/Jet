package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent by a server when
 * it wants to store a cookie on a client.
 *
 * @param identifier an identifier of the cookie
 * @param data a data of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestConfigurationPacket
 * @see ServerConfigurationPacket
 */
public record ServerStoreCookieConfigurationPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ServerConfigurationPacket {}