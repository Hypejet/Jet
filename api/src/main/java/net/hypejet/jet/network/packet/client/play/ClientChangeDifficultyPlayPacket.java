package net.hypejet.jet.network.packet.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet}, which requests a server to change
 * {@linkplain Difficulty a difficulty level}.
 *
 * @param difficulty the new difficulty level
 * @since 1.0
 * @author Codestech
 */
public record ClientChangeDifficultyPlayPacket(@NonNull Difficulty difficulty) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientChangeDifficultyPlayPacket client change difficulty play packet}.
     *
     * @param difficulty the new difficulty level
     * @since 1.0
     */
    public ClientChangeDifficultyPlayPacket {
        NullabilityUtil.requireNonNull(difficulty, "difficulty");
    }
}