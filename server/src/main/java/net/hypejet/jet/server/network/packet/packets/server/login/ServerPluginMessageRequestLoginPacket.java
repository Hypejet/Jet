package net.hypejet.jet.server.network.packet.packets.server.login;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet} sending a custom message to a client and requesting a response
 * to it.
 *
 * @param messageId a numeric identifier of the plugin message, should be unique to the connection
 * @param channel a key of the channel that the message should be sent in
 * @param data a data of the plugin message
 * @since 1.0
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
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(data, "data");
    }
}