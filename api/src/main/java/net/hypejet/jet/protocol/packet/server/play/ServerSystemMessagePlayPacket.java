package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent when a server wants to display
 * a system message on chat or an action bar.
 *
 * @param message the system message
 * @param overlay whether the message should be displayed on action bar
 * @since 1.0
 * @author Codestech
 */
public record ServerSystemMessagePlayPacket(@NonNull Component message, boolean overlay) implements ServerPlayPacket {}