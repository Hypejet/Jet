package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet} updating data of some {@linkplain Score score}
 * in {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective} with name specified.
 *
 * @param entityName a name of an owner that the score data should be updated for
 * @param objectiveName the scoreboard objective name
 * @param score a new score data that the owner should have in the specified scoreboard objective
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateScorePlayPacket(@NonNull String entityName, @NonNull String objectiveName,
                                          @NonNull Score score) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateScorePlayPacket server update score play packet}.
     *
     * @param entityName a name of an owner that the score data should be updated for
     * @param objectiveName the scoreboard objective name
     * @param score a new score data that the owner should have  in the specified scoreboard objective
     * @since 1.0
     */
    public ServerUpdateScorePlayPacket {
        Objects.requireNonNull(entityName, "entity name");
        Objects.requireNonNull(objectiveName, "objective name");
        Objects.requireNonNull(score, "score");
    }
}