package net.hypejet.jet.protocol.packet.clientbound.login.plugin;

import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientBoundPacket client-bound packet} used to implement a custom handshaking flow.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientBoundPacket
 */
public sealed interface ServerPluginRequestPacket extends ClientBoundPacket permits ServerPluginRequestPacketImpl {
    /**
     * Gets an identifier of a message, should be unique to the connection.
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
     * Gets a data of a message.
     *
     * @return the data
     * @since 1.0
     */
    byte @NonNull [] data();

    /**
     * Creates a new {@link Builder plugin request packet builder}.
     *
     * @return the builder
     * @since 1.0
     */
    static @NonNull Builder builder() {
        return new ServerPluginRequestPacketImpl.Builder();
    }

    /**
     * Represents a builder creating {@link ServerPluginRequestPacket plugin request packet}.
     *
     * @since 1.0
     * @author Codstech
     * @see ServerPluginRequestPacket
     */
    sealed interface Builder permits ServerPluginRequestPacketImpl.Builder {
        /**
         * Sets an identifier of a message, should be unique to the connection.
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
         * Sets a data of a message.
         *
         * @param data the data
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder data(byte @NonNull [] data);

        /**
         * Builds the {@link ServerPluginRequestPacket plugin request packet}.
         *
         * @return the plugin request packet
         * @since 1.0
         */
        @NonNull ServerPluginRequestPacket build();
    }
}