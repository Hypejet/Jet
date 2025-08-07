package net.hypejet.jet.server.network.packet.packets.client.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet}, which requests a server to change
 * {@linkplain Difficulty a difficulty level}.
 *
 * @param difficulty the new difficulty level
 * @since 1.0
 */
public record ClientChangeDifficultyPlayPacket(@NonNull Difficulty difficulty) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientChangeDifficultyPlayPacket client change difficulty play packet}.
     *
     * @param difficulty the new difficulty level
     * @since 1.0
     */
    public ClientChangeDifficultyPlayPacket {
        Objects.requireNonNull(difficulty, "difficulty");
    }
}