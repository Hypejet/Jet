package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent when a server wants to send a plugin
 * message to a client.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ServerPlayPacket
 */
public record ServerPluginMessagePlayPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ServerPlayPacket {}