package net.hypejet.jet.server.util.gamemode;

import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a utility for identifying {@linkplain Player.GameMode game modes}.
 *
 * @since 1.0
 * @author Codestech
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
     * Gets a {@linkplain Player.GameMode game mode} from an identifier.
     *
     * @param identifier an identifier of the game mode
     * @return the game mode
     * @since 1.0
     */
    public static Player.@Nullable GameMode gameMode(byte identifier) {
        return switch (identifier) {
            case SURVIVAL_GAME_MODE -> Player.GameMode.SURVIVAL;
            case CREATIVE_GAME_MODE -> Player.GameMode.CREATIVE;
            case ADVENTURE_GAME_MODE -> Player.GameMode.ADVENTURE;
            case SPECTATOR_GAME_MODE -> Player.GameMode.SPECTATOR;
            case NULL_GAME_MODE -> null;
            default -> throw new IllegalStateException("Unknown game mode with identifier of: " + identifier);
        };
    }

    /**
     * Gets an identifier of a {@linkplain Player.GameMode game mode}.
     *
     * @param gameMode the game mode
     * @return the identifier
     * @since 1.0
     */
    public static byte gameModeIdentifier(Player.@Nullable GameMode gameMode) {
        return switch (gameMode) {
            case SURVIVAL -> SURVIVAL_GAME_MODE;
            case CREATIVE -> CREATIVE_GAME_MODE;
            case ADVENTURE -> ADVENTURE_GAME_MODE;
            case SPECTATOR -> SPECTATOR_GAME_MODE;
            case null -> NULL_GAME_MODE;
        };
    }
}