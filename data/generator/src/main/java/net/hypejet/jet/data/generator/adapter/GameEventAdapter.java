package net.hypejet.jet.data.generator.adapter;

import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain GameEvent game events} to a Jet data equivalent.
 *
 * @since 1.0
 * @see GameEvent
 */
public final class GameEventAdapter {

    private GameEventAdapter() {}

    /**
     * Converts the specified {@linkplain GameEvent game event} to a Jet data equivalent.
     *
     * @param event the game event to convert
     * @return the converted game event
     * @since 1.0
     */
    public static @NonNull JsonGameEvent convert(@NonNull GameEvent event) {
        return new JsonGameEvent(event.notificationRadius());
    }
}
