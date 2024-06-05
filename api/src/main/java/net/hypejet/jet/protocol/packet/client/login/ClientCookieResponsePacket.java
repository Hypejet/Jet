package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which contains a cookie data requested by a server
 * via {@linkplain ServerCookieRequestPacket cookie request packet}.
 *
 * @param identifier an identifier of the cookie
 * @param data an optional cookie data, which may be null
 * @since 1.0
 * @author Codestech
 * @see ClientLoginPacket
 */
public record ClientCookieResponsePacket(@NonNull Key identifier, byte @Nullable [] data) implements ClientLoginPacket {}