package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet} used to implement a custom handshaking flow.
 *
 * @param messageId an identifier of the plugin message, should be unique to the connection
 * @param channel a name of a plugin channel used to send the data
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerPluginMessageRequestLoginPacket(int messageId, @NonNull Key channel, byte @NonNull [] data)
        implements ServerLoginPacket {
    /**
     * Constructs the {@linkplain ServerPluginMessageRequestLoginPacket plugin message request login packet}.
     *
     * @param messageId an identifier of the plugin message, should be unique to the connection
     * @param channel a name of a plugin channel used to send the data
     * @param data a data of the plugin message
     * @since 1.0
     */
    public ServerPluginMessageRequestLoginPacket {
        data = data.clone();
    }

    /**
     * Gets a data of the plugin message.
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
        if (!(object instanceof ServerPluginMessageRequestLoginPacket that)) return false;
        return this.messageId == that.messageId && Objects.equals(this.channel, that.channel)
                && Objects.deepEquals(this.data, that.data);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(this.messageId, this.channel, Arrays.hashCode(this.data));
    }
}