package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests a client to send data of a cookie.
 *
 * @param key a key of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerStoreCookiePacket
 * @see ServerPacket
 */
public record ServerCookieRequestPacket(@NonNull Key key) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCookieRequestPacket server cookie request packet}.
     *
     * @param key a key of the cookie
     * @since 1.0
     */
    public ServerCookieRequestPacket {
        NullabilityUtil.requireNonNull(key, "key");
    }
}