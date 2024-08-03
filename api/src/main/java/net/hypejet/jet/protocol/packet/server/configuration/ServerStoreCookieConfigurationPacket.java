package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent by a server when
 * it wants to store a cookie on a client.
 *
 * @param identifier an identifier of the cookie
 * @param data a data of the cookie
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestConfigurationPacket
 * @see ServerConfigurationPacket
 */
public record ServerStoreCookieConfigurationPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerStoreCookieConfigurationPacket store cookie configuration packet}.
     *
     * <p>The data array is cloned to prevent modifications on the record.</p>
     *
     * @param identifier an identifier of the cookie
     * @param data a data of the cookie
     * @since 1.0
     */
    public ServerStoreCookieConfigurationPacket {
        data = data.clone();
    }

    /**
     * Gets a data of the cookie.
     *
     * <p>The returned array is a clone to prevent modifications of the original array.</p>
     *
     * @return the data
     * @since 1.0
     */
    @Override
    public byte @NonNull [] data() {
        return this.data.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ServerStoreCookieConfigurationPacket that)) return false;
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
}