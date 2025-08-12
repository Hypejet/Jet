package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.util.game.gamemode.GameModeUtil;
import net.hypejet.jet.server.world.event.WorldEventValueProvider;
import net.hypejet.jet.world.event.world.events.ChangeGameModeWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WorldEventValueProvider a world event value provider}, which provides value for
 * {@linkplain ChangeGameModeWorldEvent a change game mode world event}.
 *
 * @since 1.0
 * @see ChangeGameModeWorldEvent
 * @see WorldEventValueProvider
 */
public final class ChangeGameModeWorldEventValueProvider extends WorldEventValueProvider<ChangeGameModeWorldEvent> {
    /**
     * Constructs the {@linkplain ChangeGameModeWorldEventValueProvider change game mode world event value provider}.
     *
     * @since 1.0
     */
    public ChangeGameModeWorldEventValueProvider() {
        super((byte) 3);
    }

    @Override
    public float value(@NonNull ChangeGameModeWorldEvent worldEvent) {
        return GameModeUtil.identifierOf(worldEvent.gameMode());
    }
}