package net.hypejet.jet.entity.acquisition.gamemode;

import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.jet.entity.player.Player;

/**
 * Represents {@linkplain GameModeAcquisition a game mode acquisition}, which allows to update
 * the {@linkplain Player.GameMode game mode}.
 *
 * @since 1.0
 * @see GameModeAcquisition
 */
public interface WriteGameModeAcquisition extends GameModeAcquisition,
        WriteNotNullObjectAcquisition<Player.GameMode> {}