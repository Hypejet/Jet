package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which is a response for a
 * {@linkplain ServerPluginMessageRequestLoginPacket plugin request packet} sent by a server.
 *
 * @param messageId an identifier of the plugin message
 * @param successful whether a client understood the plugin message
 * @param data an optional response data sent by a client
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestLoginPacket
 * @see ClientLoginPacket
 */
public record ClientPluginMessageResponseLoginPacket(int messageId, boolean successful, byte @NonNull [] data)
        implements ClientLoginPacket {}