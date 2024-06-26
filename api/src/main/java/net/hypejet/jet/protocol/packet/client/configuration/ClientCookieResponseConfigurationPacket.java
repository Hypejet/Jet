package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCookieRequestConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * as a response for a cookie requested by a server via {@linkplain ServerCookieRequestConfigurationPacket cookie
 * request configuration packet}.
 *
 * @param identifier an identifier of the cookie
 * @param data a data of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestConfigurationPacket
 * @see ClientConfigurationPacket
 */
public record ClientCookieResponseConfigurationPacket(@NonNull Key identifier, byte @Nullable [] data)
        implements ClientConfigurationPacket {}