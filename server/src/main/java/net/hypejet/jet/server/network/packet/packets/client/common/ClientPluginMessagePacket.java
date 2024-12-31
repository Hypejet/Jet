package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet}, which contains a custom data sent by a client.
 *
 * @param key a key that the data should be distinguished with
 * @param data the data
 * @since 1.0
 * @see ClientPacket
 */
public record ClientPluginMessagePacket(@NonNull Key key, @NonNull UnmodifiableByteArray data)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientPluginMessagePacket client plugin message packet}.
     *
     * @param key a key that the data should be distinguished with
     * @param data the data
     * @since 1.0
     */
    public ClientPluginMessagePacket(@NonNull Key key, byte @NonNull [] data) {
        this(key, new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain ClientPluginMessagePacket client plugin message packet}.
     *
     * @param key a key that the data should be distinguished with
     * @param data the data
     * @since 1.0
     */
    public ClientPluginMessagePacket {
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(data, "data");
    }
}