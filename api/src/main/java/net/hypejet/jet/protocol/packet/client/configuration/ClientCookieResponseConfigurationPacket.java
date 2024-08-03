package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCookieRequestConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * as a response for a cookie requested by a server via {@linkplain ServerCookieRequestConfigurationPacket cookie
 * request configuration packet}.
 *
 * @param identifier an identifier of the cookie
 * @param data a data of the cookie, {@code null} if none
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestConfigurationPacket
 * @see ClientConfigurationPacket
 */
public record ClientCookieResponseConfigurationPacket(@NonNull Key identifier, byte @Nullable [] data)
        implements ClientConfigurationPacket {
    /**
     * Constructs teh {@linkplain ClientCookieResponseConfigurationPacket cookie response configuration packet}.
     *
     * <p>The data array is cloned to prevent modifications on the record.</p>
     *
     * @param identifier an identifier of the cookie
     * @param data a data of the cookie, {@code null} if none
     * @since 1.0
     */
    public ClientCookieResponseConfigurationPacket {
        data = data == null ? null : data.clone();
    }

    /**
     * Gets a data of the cookie, {@code null} if none.
     *
     * <p>The array returned is a clone to prevent modifications of the original array.</p>
     *
     * @return the data
     * @since 1.0
     */
    @Override
    public byte @Nullable [] data() {
        return this.data == null ? null : this.data.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClientCookieResponseConfigurationPacket that)) return false;
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