package net.hypejet.jet.protocol.packet.server.login.cookie;

import net.hypejet.jet.protocol.packet.server.login.ServerLoginPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which requests a client to send a cookie data.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public sealed interface ServerCookieRequestPacket extends ServerLoginPacket permits ServerCookieRequestPacketImpl {
    /**
     * Gets an identifier of the cookie.
     *
     * @return the identifier
     * @since 1.0
     */
    @NonNull Key identifier();

    /**
     * Creates a new {@linkplain Builder cookie request packet builder}.
     *
     * @return the builder
     * @since 1.0
     */
    static @NonNull Builder builder() {
        return new ServerCookieRequestPacketImpl.Builder();
    }

    /**
     * Represents a builder of {@linkplain ServerCookieRequestPacket cookie request packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerCookieRequestPacket
     */
    sealed interface Builder permits ServerCookieRequestPacketImpl.Builder {
        /**
         * Sets an identifier of the cookie.
         *
         * @param identifier the identifier
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder identifier(@NonNull Key identifier);

        /**
         * Builds the {@linkplain ServerCookieRequestPacket cookie request packet}.
         *
         * @return the cookie request packet
         * @throws IllegalStateException if an identifier of the cookie was not set
         * @since 1.0
         */
        @NonNull ServerCookieRequestPacket build();
    }
}