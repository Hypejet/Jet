package net.hypejet.jet.entity.acquisition.gamemode;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain NotNullObjectAcquisition a not-null object acquisition}
 * of {@linkplain Player.GameMode a game mode} of {@linkplain Player a player}.
 *
 * @since 1.0
 * @see Player.GameMode
 * @see Player
 */
public interface GameModeAcquisition extends NotNullObjectAcquisition<Player.GameMode> {
    /**
     * Gets a previous {@linkplain Player.GameMode game mode} that {@linkplain Player a player} associated
     * with this acquisition had.
     *
     * @return the game mode, {@code null} if the player has not had any game mode before the current game mode
     * @since 1.0
     */
    Player.@Nullable GameMode previous();
}