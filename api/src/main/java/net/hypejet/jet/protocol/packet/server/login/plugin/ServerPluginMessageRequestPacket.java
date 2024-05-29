package net.hypejet.jet.protocol.packet.server.login.plugin;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet} used to implement a custom handshaking flow.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerPluginMessageRequestPacket extends ServerPacket
        permits ServerPluginMessageRequestPacketImpl {
    /**
     * Gets an identifier of the plugin message, should be unique to the connection.
     *
     * @return the identifier
     * @since 1.0
     */
    int messageId();

    /**
     * Gets a name of a plugin channel used to send the data.
     *
     * @return the name
     * @since 1.0
     */
    @NonNull Key channel();

    /**
     * Gets a data of the plugin message.
     *
     * @return the data
     * @since 1.0
     */
    byte @NonNull [] data();

    /**
     * Creates a new {@linkplain Builder plugin message request packet builder}.
     *
     * @return the builder
     * @since 1.0
     */
    static @NonNull Builder builder() {
        return new ServerPluginMessageRequestPacketImpl.Builder();
    }

    /**
     * Represents a builder creating {@linkplain ServerPluginMessageRequestPacket plugin message request packet}.
     *
     * @since 1.0
     * @author Codstech
     * @see ServerPluginMessageRequestPacket
     */
    sealed interface Builder permits ServerPluginMessageRequestPacketImpl.Builder {
        /**
         * Sets an identifier of the plugin message, should be unique to the connection.
         *
         * @param messageId the identifier
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder messageId(int messageId);

        /**
         * Sets a name of a plugin channel used to send the data.
         *
         * @param channel the name
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder channel(@NonNull Key channel);

        /**
         * Sets a data of the plugin message.
         *
         * @param data the data
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder data(byte @NonNull [] data);

        /**
         * Builds the {@linkplain ServerPluginMessageRequestPacket plugin message request packet}.
         *
         * @return the plugin message request packet
         * @since 1.0
         */
        @NonNull
        ServerPluginMessageRequestPacket build();
    }
}