package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which requests a client to send a cookie data.
 *
 * @param identifier an identifier of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerCookieRequestLoginPacket(@NonNull Key identifier) implements ServerLoginPacket {}