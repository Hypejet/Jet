package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which stores a cookie on a client.
 *
 * @param key a key of the cookie
 * @param data a data of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public record ServerStoreCookiePacket(@NonNull Key key, @NonNull UnmodifiableByteArray data)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerStoreCookiePacket store cookie configuration packet}.
     *
     * @param key a key of the cookie
     * @param data a data of the cookie
     * @since 1.0
     */
    public ServerStoreCookiePacket {
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(data, "data");
    }
}