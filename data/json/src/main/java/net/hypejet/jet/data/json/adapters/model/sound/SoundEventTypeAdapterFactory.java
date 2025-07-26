package net.hypejet.jet.data.json.adapters.model.sound;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonSoundEvent sound events}.
 *
 * @since 1.0
 * @see JsonSoundEvent
 * @see TypeAdapterFactory
 */
public final class SoundEventTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain SoundEventTypeAdapterFactory sound event type adapter factory}.
     *
     * @since 1.0
     */
    public static final SoundEventTypeAdapterFactory INSTANCE = new SoundEventTypeAdapterFactory();

    private SoundEventTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonSoundEvent.class.isAssignableFrom(type.getRawType())) {
            return new SoundEventTypeAdapter(gson);
        } else {
            return null;
        }
    }
}