package net.hypejet.jet.protocol.packet.server.login.cookie;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain  ServerCookieRequestPacket cookie request packet}.
 *
 * @param identifier an identifier of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestPacket
 */
record ServerCookieRequestPacketImpl(@NonNull Key identifier) implements ServerCookieRequestPacket {
    @Override
    public int getPacketId() {
        return 5;
    }

    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeIdentifier(this.identifier);
    }

    /**
     * Represents a {@link ServerCookieRequestPacket.Builder cookie request packet builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerCookieRequestPacket.Builder
     */
    static final class Builder implements ServerCookieRequestPacket.Builder {

        private Key identifier;

        @Override
        public ServerCookieRequestPacket.@NonNull Builder identifier(@NonNull Key identifier) {
            this.identifier = identifier;
            return this;
        }

        @Override
        public @NonNull ServerCookieRequestPacket build() {
            if (this.identifier == null)
                throw new IllegalStateException("An identifier of the cookie was not set");
            return new ServerCookieRequestPacketImpl(this.identifier);
        }
    }
}