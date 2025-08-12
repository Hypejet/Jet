package net.hypejet.jet.server.plugin.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.hypejet.jet.plugin.dependency.PluginDependency;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents {@linkplain JsonDeserializer a json deserializer}, which deserializes
 * {@linkplain PluginDependency a plugin dependency},
 *
 * @since 1.0
 * @see PluginDependency
 * @see JsonDeserializer
 */
public final class PluginDependencyDeserializer implements JsonDeserializer<PluginDependency> {

    private static final String NAME = "name";
    private static final String VERSIONS_SUPPORTED = "versions-supported";
    private static final String REQUIRED = "required";

    @Override
    public PluginDependency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();

        JsonElement name = object.get(NAME);
        if (name == null)
            throw new JsonParseException("The plugin name of plugin dependency has not been specified");

        JsonElement required = object.get(REQUIRED);
        if (required == null)
            throw new JsonParseException("It has not been specified whether the plugin dependency is required");

        JsonArray versionsSupportedJson = object.getAsJsonArray(VERSIONS_SUPPORTED);
        if (versionsSupportedJson == null)
            throw new JsonParseException("No plugin versions satisfying the plugin dependency have been specified");

        Set<String> versionsSupported = new HashSet<>();
        for (JsonElement element : versionsSupportedJson)
            versionsSupported.add(element.getAsString());

        return new PluginDependency(
                name.getAsString(),
                Set.copyOf(versionsSupported),
                required.getAsBoolean()
        );
    }
}