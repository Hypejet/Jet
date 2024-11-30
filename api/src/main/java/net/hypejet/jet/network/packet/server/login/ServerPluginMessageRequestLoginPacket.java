package net.hypejet.jet.network.packet.server.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet} sending a custom message to a client and requesting a response
 * to it.
 *
 * @param messageId a numeric identifier of the plugin message, should be unique to the connection
 * @param channel a key of the channel that the message should be sent in
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public record ServerPluginMessageRequestLoginPacket(int messageId, @NonNull Key channel,
                                                    @NonNull UnmodifiableByteArray data) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerPluginMessageRequestLoginPacket plugin message request login packet}.
     *
     * @param messageId a numeric identifier of the plugin message, should be unique to the connection
     * @param channel a key of the channel that the message should be sent in
     * @param data a data of the plugin message
     * @since 1.0
     */
    public ServerPluginMessageRequestLoginPacket(int messageId, @NonNull Key channel, byte @NonNull [] data) {
        this(messageId, channel, new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain ServerPluginMessageRequestLoginPacket plugin message request login packet}.
     *
     * @param messageId a numeric identifier of the plugin message, should be unique to the connection
     * @param channel a key of the channel that the message should be sent in
     * @param data a data of the plugin message
     * @since 1.0
     */
    public ServerPluginMessageRequestLoginPacket {
        NullabilityUtil.requireNonNull(channel, "channel");
        NullabilityUtil.requireNonNull(data, "data");
    }
}