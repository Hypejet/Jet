package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet} that requests a client to display a scoreboard objective.
 *
 * @param position a scoreboard position that the scoreboard objective should be displayed at
 * @param name a name of the scoreboard objective
 * @since 1.0
 * @see ServerPacket
 */
public record ServerDisplayObjectivePlayPacket(@NonNull ScoreboardPosition position, @NonNull String name)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerDisplayObjectivePlayPacket server display objective play packet}.
     *
     * @param position a scoreboard position that the scoreboard objective should be displayed at
     * @param name a name of the scoreboard objective
     * @since 1.0
     */
    public ServerDisplayObjectivePlayPacket {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(name, "name");
    }
}