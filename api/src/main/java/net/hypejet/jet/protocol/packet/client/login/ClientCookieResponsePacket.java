package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.cookie.ServerCookieRequestPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which contains a cookie data requested by a server
 * via {@linkplain ServerCookieRequestPacket cookie request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginPacket
 */
public non-sealed interface ClientCookieResponsePacket extends ClientLoginPacket {
    /**
     * Gets an identifier of the cookie.
     *
     * @return the identifier
     * @since 1.0
     */
    @NonNull Key identifier();

    /**
     * Gets an optional cookie data.
     *
     * @return the cookie data, which may be null
     * @since 1.0
     */
    byte @NonNull [] data();
}