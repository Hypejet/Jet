package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet} used to implement a custom handshaking flow.
 *
 * @param messageId an identifier of the plugin message, should be unique to the connection
 * @param channel a name of a plugin channel used to send the data
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerPluginMessageRequestPacket(int messageId, @NonNull Key channel, byte @NonNull [] data)
        implements ServerLoginPacket {}