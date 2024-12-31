package net.hypejet.jet.event.events.cookie;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an event, which is called when a cookie response from a client is received.
 *
 * <p>Remember that a player might have been not initialized, since a cookie can also be requested before
 * player initialization.</p>
 *
 * @param playerConnection a player connection with the client
 * @param key a key of the cookie
 * @param data a data of the cookie
 * @since 1.0
 */
public record CookieResponseEvent(@NonNull PlayerConnection playerConnection, @NonNull Key key,
                                  @Nullable UnmodifiableByteArray data) {
    /**
     * Constructs the {@linkplain CookieResponseEvent cookie response event}.
     *
     * @param playerConnection a player connection with the client
     * @param key a key of the cookie
     * @param data a data of the cookie
     * @since 1.0
     */
    public CookieResponseEvent(@NonNull PlayerConnection playerConnection, @NonNull Key key, byte @Nullable [] data) {
        this(playerConnection, key, data == null ? null : new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain CookieResponseEvent cookie response event}.
     *
     * @param playerConnection a player connection with the client
     * @param key a key of the cookie
     * @param data a data of the cookie
     * @since 1.0
     */
    public CookieResponseEvent {
        NullabilityUtil.requireNonNull(playerConnection, "player connection");
        NullabilityUtil.requireNonNull(key, "key");
    }
}
