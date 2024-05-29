package net.hypejet.jet.protocol.packet.serverbound.login;

import net.hypejet.jet.protocol.packet.clientbound.login.plugin.ServerPluginRequestPacket;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerBoundPacket server-bound packet}, which is a response for a
 * {@linkplain ServerPluginRequestPacket plugin request packet} sent by a server.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginRequestPacket
 * @see ServerBoundPacket
 */
public interface ClientPluginMessageResponsePacket extends ServerBoundPacket {
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