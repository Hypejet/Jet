package net.hypejet.jet.server.network.packet.packets.server.common;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which contains a custom data sent by a server.
 *
 * @param key a key that the data should be distinguished with
 * @param data the data
 * @since 1.0
 * @see ServerPacket
 */
public record ServerPluginMessagePacket(@NonNull Key key, @NonNull UnmodifiableByteArray data)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerPluginMessagePacket server plugin message packet}.
     *
     * @param key a key that the data should be distinguished with
     * @param data the data
     * @since 1.0
     */
    public ServerPluginMessagePacket(@NonNull Key key, byte @NonNull [] data) {
        this(key, new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain ServerPluginMessagePacket server plugin message packet}.
     *
     * @param key a key that the data should be distinguished with
     * @param data the data
     * @since 1.0
     */
    public ServerPluginMessagePacket {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(data, "data");
    }
}