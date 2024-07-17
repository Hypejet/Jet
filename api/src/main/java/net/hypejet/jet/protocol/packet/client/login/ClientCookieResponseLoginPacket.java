package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestLoginPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which contains a cookie data requested by a server
 * via {@linkplain ServerCookieRequestLoginPacket cookie request packet}.
 *
 * @param identifier an identifier of the cookie
 * @param data a response data of the cookie, {@code} null if none
 * @since 1.0
 * @author Codestech
 * @see ClientLoginPacket
 */
public record ClientCookieResponseLoginPacket(@NonNull Key identifier, byte @Nullable [] data)
        implements ClientLoginPacket {
    /**
     * Constructs the {@linkplain ClientCookieResponseLoginPacket cookie response login packet}.
     *
     * <p>The data is cloned to prevent modifications on the record.</p>
     *
     * @param identifier an identifier of the cookie
     * @param data a response data of the cookie, {@code} null if none
     * @since 1.0
     */
    public ClientCookieResponseLoginPacket {
        data = data == null ? null : data.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClientCookieResponseLoginPacket that)) return false;
        return Objects.equals(this.identifier, that.identifier) && Objects.deepEquals(this.data, that.data);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(this.identifier, Arrays.hashCode(this.data));
    }

    /**
     * Gets a response data of the cookie, {@code} null if none.
     *
     * <p>The returned array is a clone to prevent modifications of the original array.</p>
     *
     * @return the response data
     * @since 1.0
     */
    @Override
    public byte @Nullable [] data() {
        return this.data == null ? null : this.data.clone();
    }
}