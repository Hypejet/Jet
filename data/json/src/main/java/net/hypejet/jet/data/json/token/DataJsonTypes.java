package net.hypejet.jet.data.json.token;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.kyori.adventure.key.Key;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Represents a holder of {@linkplain ParameterizedType parameterized types} to be used with {@linkplain Gson gson}.
 *
 * @since 1.0
 * @see ParameterizedType
 */
public final class DataJsonTypes {
    /**
     * A parameterized type of {@linkplain Set set} of {@linkplain String strings}.
     *
     * @since 1.0
     */
    public static final Type STRING_SET = new TypeToken<Set<String>>() {}.getType();

    /**
     * A parameterized type of {@linkplain Set set} of {@linkplain Key keys}.
     *
     * @since 1.0
     */
    public static final Type KEY_SET = new TypeToken<Set<Key>>() {}.getType();

    /**
     * A parameterized type of {@linkplain Map map} associating
     * {@linkplain String strings} with {@linkplain String strings}.
     *
     * @since 1.0
     */
    public static final Type STRING_TO_STRING_MAP = new TypeToken<Map<String, String>>() {}.getType();

    /**
     * A parameterized type of {@linkplain Map map} associating
     * {@linkplain String strings} with {@linkplain JsonStateProperty state properties}.
     *
     * @since 1.0
     */
    public static final Type
            STRING_TO_STATE_PROPERTY_MAP = new TypeToken<Map<String, JsonStateProperty>>() {}.getType();

    private DataJsonTypes() {}
}