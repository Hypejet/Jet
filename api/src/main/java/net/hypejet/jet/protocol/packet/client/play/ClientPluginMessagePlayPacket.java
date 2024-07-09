package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client sends a plugin
 * message.
 *
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientPluginMessagePlayPacket(@NonNull Key identifier, byte @NonNull [] data)
        implements ClientPlayPacket {}