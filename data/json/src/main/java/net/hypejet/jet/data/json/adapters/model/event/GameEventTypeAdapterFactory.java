package net.hypejet.jet.data.json.adapters.model.event;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * of objects related to {@linkplain JsonGameEvent game events}.
 *
 * @since 1.0
 * @see JsonGameEvent
 * @see TypeAdapterFactory
 */
public final class GameEventTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain GameEventTypeAdapterFactory game event type adapter factory}.
     *
     * @since 1.0
     */
    public static final GameEventTypeAdapterFactory INSTANCE = new GameEventTypeAdapterFactory();

    private GameEventTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonGameEvent.class.isAssignableFrom(type.getRawType())) {
            return GameEventTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}