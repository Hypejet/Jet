package net.hypejet.jet.data.json.token;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Represents a holder of {@linkplain ParameterizedType parameterized types} to be used with {@linkplain Gson gson}.
 *
 * @since 1.0
 * @see ParameterizedType
 */
public final class DataJsonTypes {
    /**
     * A parameterized type of {@linkplain JsonHolder holder} of a {@linkplain JsonSoundEvent sound event}.
     *
     * @since 1.0
     */
    public static final Type SOUND_EVENT_HOLDER = new TypeToken<JsonHolder<JsonSoundEvent>>() {}.getType();

    private DataJsonTypes() {}

}