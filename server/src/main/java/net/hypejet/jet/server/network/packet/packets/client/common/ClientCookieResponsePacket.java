package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCookieRequestPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response to a cookie request made by a server.
 *
 * @param key a key of the cookie
 * @param data a data of the cookie, {@code null} if none
 * @since 1.0
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
        Objects.requireNonNull(key, "key");
    }
}