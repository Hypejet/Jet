package net.hypejet.jet.network.packet.client.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.network.packet.server.common.ServerCookieRequestPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response to a cookie request made by a server.
 *
 * @param key a key of the cookie
 * @param data a data of the cookie, {@code null} if none
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestPacket
 * @see ClientPacket
 */
public record ClientCookieResponsePacket(@NonNull Key key, @Nullable UnmodifiableByteArray data)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientCookieResponsePacket client cookie response configuration packet}.
     *
     * @param key a key of the cookie
     * @param data a data of the cookie, {@code null} if none
     * @since 1.0
     */
    public ClientCookieResponsePacket(@NonNull Key key, byte @Nullable [] data) {
        this(key, data == null ? null : new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain ClientCookieResponsePacket client cookie response configuration packet}.
     *
     * @param key a key of the cookie
     * @param data a data of the cookie, {@code null} if none
     * @since 1.0
     */
    public ClientCookieResponsePacket {
        NullabilityUtil.requireNonNull(key, "key");
    }
}