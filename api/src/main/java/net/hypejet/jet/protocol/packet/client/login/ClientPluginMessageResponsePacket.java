package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.server.login.plugin.ServerPluginMessageRequestPacket;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet}, which is a response for a
 * {@linkplain ServerPluginMessageRequestPacket plugin request packet} sent by a server.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestPacket
 * @see ClientPacket
 */
public interface ClientPluginMessageResponsePacket extends ClientPacket {
    /**
     * Gets an identifier of the plugin message.
     *
     * @return the id
     * @since 1.0
     */
    int messageId();

    /**
     * Gets whether a client understood the plugin message.
     *
     * @return true if a client understood the plugin message, false otherwise
     * @since 1.0
     */
    boolean successful();

    /**
     * Gets an optional response data sent by a client.
     *
     * @return the optional response data
     * @since 1.0
     */
    byte @NonNull [] data();
}