package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client sends a plugin
 * message.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientPluginMessagePlayPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ClientPlayPacket {
    /**
     * Constructs the {@linkplain ClientPluginMessagePlayPacket plugin message play packet}.
     *
     * <p>The data array is cloned to prevent modifications on the record.</p>
     *
     * @param identifier an identifier of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    public ClientPluginMessagePlayPacket {
        data = data.clone();
    }

    /**
     * Gets a data of the plugin message.
     *
     * <p>The array returned is a clone to prevent modifications of the original array.</p>
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
        if (!(object instanceof ClientPluginMessagePlayPacket that)) return false;
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