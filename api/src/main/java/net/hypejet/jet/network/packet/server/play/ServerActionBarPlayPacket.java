package net.hypejet.jet.network.packet.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which displays a text on an action bar on a client.
 *
 * @param text the text to display
 * @since 1.0
 * @author Codestech
 */
public record ServerActionBarPlayPacket(@NonNull Component text) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerActionBarPlayPacket server action bar play packet}.
     *
     * @param text the text to display
     * @since 1.0
     */
    public ServerActionBarPlayPacket {
        NullabilityUtil.requireNonNull(text, "text");
    }
}