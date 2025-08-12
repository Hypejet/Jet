package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet} that requests a client to replace a scoreboard objective
 * displayed at {@linkplain ScoreboardPosition a scoreboard position} specified with an objective with name specified.
 *
 * @param position the scoreboard position
 * @param name the objective name, {@code null} if nothing should be displayed at the scoreboard position specified
 * @since 1.0
 * @see ServerPacket
 */
public record ServerSetObjectiveDisplayedPlayPacket(@NonNull ScoreboardPosition position, @Nullable String name)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerSetObjectiveDisplayedPlayPacket server set objective displayed play packet}.
     *
     * @param position the scoreboard position
     * @param name the objective name, {@code null} if nothing should be displayed at the scoreboard position specified
     * @since 1.0
     */
    public ServerSetObjectiveDisplayedPlayPacket {
        Objects.requireNonNull(position, "position");
    }
}