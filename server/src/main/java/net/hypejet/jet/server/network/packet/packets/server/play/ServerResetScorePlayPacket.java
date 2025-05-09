package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ServerPacket a server packet} removing a score of some entity on some scoreboard objective.
 *
 * @param entityName a name of an entity that the score belongs to
 * @param objectiveName a name of the scoreboard objective, {@code null} to remove score of the entity
 *                      in all objectives
 * @since 1.0
 * @see ServerPacket
 */
public record ServerResetScorePlayPacket(@NonNull String entityName, @Nullable String objectiveName)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerResetScorePlayPacket server reset score play packet}.
     *
     * @param entityName a name of an entity that the score belongs to
     * @param objectiveName a name of the scoreboard objective, {@code null} to remove score of the entity
     *                      in all objectives
     * @since 1.0
     */
    public ServerResetScorePlayPacket {
        NullabilityUtil.requireNonNull(entityName, "entity name");
    }
}