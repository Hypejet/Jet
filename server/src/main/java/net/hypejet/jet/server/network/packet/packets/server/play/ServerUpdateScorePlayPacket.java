package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.scoreboard.score.number.NumberFormat;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ServerPacket a server packets} updating a score of an entity on a scoreboard objective.
 *
 * @param entityName a name of the entity
 * @param objectiveName a name of the scoreboard objective
 * @param score the new score
 * @param displayName a text that should be displayed instead of an entity name in case when score owner name
 *                    is displayed, {@code null} if the entity name should be used instead
 * @param numberFormat a number format that should be applied to the score, {@code null} if default formatting
 *                     should be used
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateScorePlayPacket(
        @NonNull String entityName, @NonNull String objectiveName,
        int score, @Nullable Component displayName, @Nullable NumberFormat numberFormat
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateScorePlayPacket server update score play packet}.
     *
     * @param entityName a name of the entity
     * @param objectiveName a name of the scoreboard objective
     * @param score the new score
     * @param displayName a text that should be displayed instead of an entity name in case when score owner name
     *                    is displayed, {@code null} if the entity name should be used instead
     * @param numberFormat a number format that should be applied to the score, {@code null} if default formatting
     *                     should be used
     * @since 1.0
     */
    public ServerUpdateScorePlayPacket {
        NullabilityUtil.requireNonNull(entityName, "entity name");
        NullabilityUtil.requireNonNull(objectiveName, "objective name");
        NullabilityUtil.requireNonNull(numberFormat, "number format");
    }
}