package net.hypejet.jet.server.util.game.gamemode;

import java.util.Objects;
import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a utility for identifying {@linkplain Player.GameMode game modes}.
 *
 * @since 1.0
 * @see Player.GameMode
 */
public final class GameModeUtil {

    private static final byte NULL_GAME_MODE = -1;

    private static final byte SURVIVAL_GAME_MODE = 0;
    private static final byte CREATIVE_GAME_MODE = 1;
    private static final byte ADVENTURE_GAME_MODE = 2;
    private static final byte SPECTATOR_GAME_MODE = 3;

    private GameModeUtil() {}

    /**
     * Gets an identifier of {@linkplain Player.GameMode a game mode} specified, which may be null.
     *
     * @param gameMode the game mode
     * @return the identifier
     * @since 1.0
     */
    public static byte nullableIdentifierOf(Player.@Nullable GameMode gameMode) {
        if (gameMode == null)
            return NULL_GAME_MODE;
        return identifierOf(gameMode);
    }

    /**
     * Gets an identifier of {@linkplain Player.GameMode a game mode} specified.
     *
     * @param gameMode the game mode
     * @return the identifier
     * @since 1.0
     */
    public static byte identifierOf(Player.@NonNull GameMode gameMode) {
        Objects.requireNonNull(gameMode, "game mode");
        if (gameMode == Player.GameMode.SURVIVAL)
            return SURVIVAL_GAME_MODE;
        else if (gameMode == Player.GameMode.CREATIVE)
            return CREATIVE_GAME_MODE;
        else if (gameMode == Player.GameMode.ADVENTURE)
            return ADVENTURE_GAME_MODE;
        else if (gameMode == Player.GameMode.SPECTATOR)
            return SPECTATOR_GAME_MODE;
        throw new IllegalArgumentException(String.format("Unknown game mode: %s", gameMode.name()));
    }
}