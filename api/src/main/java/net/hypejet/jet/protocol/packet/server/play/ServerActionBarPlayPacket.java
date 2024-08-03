package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent by a server to display a text
 * on an action bar on a client.
 *
 * @param text the text to display
 * @since 1.0
 * @author Codestech
 */
public record ServerActionBarPlayPacket(@NonNull Component text) implements ServerPlayPacket {}