package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent when a server
 * wants to send a plugin message to a client.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerPluginMessageConfigurationPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerPluginMessageConfigurationPacket plugin message configuration packet}.
     *
     * @param identifier an identifier of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    public ServerPluginMessageConfigurationPacket {
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
        if (!(object instanceof ServerPluginMessageConfigurationPacket that)) return false;
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