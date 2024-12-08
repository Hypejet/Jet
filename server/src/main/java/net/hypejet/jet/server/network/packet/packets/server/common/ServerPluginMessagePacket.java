package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sends a custom message to a client.
 *
 * @param key a key of the message
 * @param data data of the message
 * @since 1.0
 * @author Codesetech
 * @see ServerPacket
 */
public record ServerPluginMessagePacket(@NonNull Key key, @NonNull UnmodifiableByteArray data)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerPluginMessagePacket server plugin message packet}.
     *
     * @param key a key of the message
     * @param data data of the message
     * @since 1.0
     */
    public ServerPluginMessagePacket(@NonNull Key key, byte @NonNull [] data) {
        this(key, new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain ServerPluginMessagePacket server plugin message packet}.
     *
     * @param key a key of the message
     * @param data data of the message
     * @since 1.0
     */
    public ServerPluginMessagePacket {
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(data, "data");
    }
}