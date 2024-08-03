package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which is a response for a
 * {@linkplain ServerPluginMessageRequestLoginPacket plugin request packet} sent by a server.
 *
 * @param messageId an identifier of the plugin message
 * @param successful whether a client understood the plugin message
 * @param data an optional response data sent by a client
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestLoginPacket
 * @see ClientLoginPacket
 */
public record ClientPluginMessageResponseLoginPacket(int messageId, boolean successful, byte @NonNull [] data)
        implements ClientLoginPacket {
    /**
     * Constructs the {@linkplain ClientPluginMessageResponseLoginPacket plugin message response login packet}.
     *
     * <p>The data is cloned to prevent modifications on the record.</p>
     *
     * @param messageId an identifier of the plugin message
     * @param successful whether a client understood the plugin message
     * @param data an optional response data sent by a client
     * @since 1.0
     */
    public ClientPluginMessageResponseLoginPacket {
        data = data.clone();
    }

    /**
     * Gets an optional response data sent by a client.
     *
     * <p>The array returned is a clone to prevent modifications of the original array.</p>
     *
     * @return the response data
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
        if (!(object instanceof ClientPluginMessageResponseLoginPacket that)) return false;
        return this.messageId == that.messageId && this.successful == that.successful
                && Objects.deepEquals(this.data, that.data);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(this.messageId, this.successful, Arrays.hashCode(this.data));
    }
}