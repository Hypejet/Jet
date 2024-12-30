package net.hypejet.jet.server.plugin.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypejet.jet.data.codecs.util.JsonUtil;
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

        JsonArray versionsSupportedJson = JsonUtil.read(VERSIONS_SUPPORTED, JsonArray.class, object, context);
        Set<String> versionsSupported = new HashSet<>();

        for (JsonElement element : versionsSupportedJson)
            versionsSupported.add(context.deserialize(element, String.class));

        return new PluginDependency(
                JsonUtil.read(NAME, String.class, object, context),
                Set.copyOf(versionsSupported),
                JsonUtil.read(REQUIRED, boolean.class, object, context)
        );
    }
}