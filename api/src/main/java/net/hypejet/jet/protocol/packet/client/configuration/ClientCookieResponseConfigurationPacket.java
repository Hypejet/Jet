package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * as a response for a cookie requested by a server via {@linkplain ???}.
 *
 * @param identifier an identifier of the cookie
 * @param data a data of the cookie
 * @since 1.0
 * @author Codestech
 * @see ???
 * @see ClientConfigurationPacket
 */
public record ClientCookieResponseConfigurationPacket(@NonNull Key identifier, byte @Nullable [] data)
        implements ClientConfigurationPacket {}