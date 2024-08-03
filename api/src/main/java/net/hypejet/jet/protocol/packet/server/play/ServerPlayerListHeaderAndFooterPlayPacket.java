package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent to change text of a header and footer
 * on a player list.
 *
 * @param headerText the text to display on the header of the player list
 * @param footerText the text to display on the footer of the player list
 * @since 1.0
 * @author Codestech
 */
public record ServerPlayerListHeaderAndFooterPlayPacket(@NonNull Component headerText, @NonNull Component footerText)
        implements ServerPlayPacket {}