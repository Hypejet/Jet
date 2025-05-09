package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.scoreboard.score.number.NumberFormat;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packets} updating a score of an entity on the client.
 *
 * @param entityName a name of the entity
 * @param objectiveName an objective that the score belongs to
 * @param score the score
 * @param numberFormat a number format of the score
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateScorePlayPacket(@NonNull String entityName, @NonNull String objectiveName,
                                          int score, @NonNull NumberFormat numberFormat) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateScorePlayPacket server update score play packet}.
     *
     * @param entityName a name of the entity
     * @param objectiveName an objective that the score belongs to
     * @param score the score
     * @param numberFormat a number format of the score
     * @since 1.0
     */
    public ServerUpdateScorePlayPacket {
        NullabilityUtil.requireNonNull(entityName, "entity name");
        NullabilityUtil.requireNonNull(objectiveName, "objective name");
        NullabilityUtil.requireNonNull(numberFormat, "number format");
    }
}