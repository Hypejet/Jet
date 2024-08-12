package net.hypejet.jet.server.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a utility for managing {@linkplain JsonElement json elements}.
 *
 * @since 1.0
 * @author Codestech
 * @see JsonElement
 */
public final class JsonUtil {

    private JsonUtil() {}

    /**
     * Gets a {@linkplain String string} from a {@linkplain JsonObject json object}, throws an exception if
     * the {@linkplain JsonElement json element} is null.
     *
     * @param name a name of the string
     * @param object the json object
     * @return the string
     * @since 1.0
     * @throws NullPointerException if the string is null
     * @see JsonElement#getAsString()
     */
    public static @NonNull String getRequiredString(@NonNull String name, @NonNull JsonObject object) {
        return getRequiredElement(name, object).getAsString();
    }

    /**
     * Gets a {@linkplain JsonArray json array} from a {@linkplain JsonObject json object}, throws an exception if
     * the {@linkplain JsonElement json element} is null.
     *
     * @param name a name of the json array
     * @param object the json object
     * @return the json array
     * @since 1.0
     * @throws NullPointerException if the json array is null
     * @see JsonElement#getAsJsonArray()
     */
    public static @NonNull JsonArray getRequiredArray(@NonNull String name, @NonNull JsonObject object) {
        return getRequiredElement(name, object).getAsJsonArray();
    }

    /**
     * Gets a {@linkplain JsonElement json element} from a {@linkplain JsonObject json object}, throws an exception if
     * the {@linkplain JsonElement json element} is null.
     *
     * @param name a name of the element
     * @param object the json object
     * @return the json element
     * @since 1.0
     */
    public static @NonNull JsonElement getRequiredElement(@NonNull String name, @NonNull JsonObject object) {
        Objects.requireNonNull(name, "The name must not be null");
        Objects.requireNonNull(object, "The object must not be null");
        return Objects.requireNonNull(object.get(name),
                "The plugin metadata does not contain a required property with name of \"" + name + "\"");
    }

    /**
     * Gets a {@linkplain Boolean boolean} from a {@linkplain JsonObject json object}. Uses a default value if
     * the {@linkplain JsonElement json element} is null.
     *
     * @param name a name of the boolean
     * @param object the json object
     * @param defaultValue the default value
     * @return the boolean
     * @since 1.0
     * @see JsonElement#getAsBoolean()
     */
    public static boolean getOptionalBoolean(@NonNull String name, @NonNull JsonObject object, boolean defaultValue) {
        Objects.requireNonNull(name, "The name must not be null");
        Objects.requireNonNull(object, "The object must not be null");

        JsonElement element = object.get(name);
        if (element != null)
            return element.getAsBoolean();
        return defaultValue;
    }
}