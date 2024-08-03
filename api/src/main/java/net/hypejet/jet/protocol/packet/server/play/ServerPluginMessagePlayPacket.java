package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent when a server wants to send a plugin
 * message to a client.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerPlayPacket
 */
public record ServerPluginMessagePlayPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ServerPlayPacket {
    /**
     * Constructs the {@linkplain ServerPluginMessagePlayPacket plugin message play packet}.
     *
     * <p>The data array is cloned to prevent modifications on the record.</p>
     *
     * @param identifier an identifier of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    public ServerPluginMessagePlayPacket {
        data = data.clone();
    }

    /**
     * Gets a data of the plugin message.
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
        if (!(object instanceof ServerPluginMessagePlayPacket packet)) return false;
        return Objects.equals(this.identifier, packet.identifier) && Objects.deepEquals(this.data, packet.data);
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