package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which displays a system message in a chat or on an action bar.
 *
 * @param message the system message
 * @param overlay whether the message should be displayed on the action bar
 * @since 1.0
 * @author Codestech
 */
public record ServerSystemMessagePlayPacket(@NonNull Component message, boolean overlay) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerSystemMessagePlayPacket server system message play packet}.
     *
     * @param message the system message
     * @param overlay whether the message should be displayed on the action bar
     * @since 1.0
     */
    public ServerSystemMessagePlayPacket {
        NullabilityUtil.requireNonNull(message, "message");
    }
}