package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which is sent to change text of a header and footer
 * of a player list on a client.
 *
 * @param headerText the text to display as the header of the player list
 * @param footerText the text to display as the footer of the player list
 * @since 1.0
 */
public record ServerPlayerListHeaderAndFooterPlayPacket(@NonNull Component headerText, @NonNull Component footerText)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerPlayerListHeaderAndFooterPlayPacket server play list header and footer
     * play packet}.
     *
     * @param headerText the text to display as the header of the player list
     * @param footerText the text to display as the footer of the player list
     * @since 1.0
     */
    public ServerPlayerListHeaderAndFooterPlayPacket {
        Objects.requireNonNull(headerText, "header text");
        Objects.requireNonNull(footerText, "footer text");
    }
}