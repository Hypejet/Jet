package net.hypejet.jet.server.plugin.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypejet.jet.plugin.PluginDependency;
import net.hypejet.jet.server.util.json.JsonUtil;

import java.lang.reflect.Type;

/**
 * Represents a {@linkplain JsonDeserializer json deserializer}, which deserializes a {@linkplain PluginDependency
 * plugin dependency},
 *
 * @since 1.0
 * @author Codestech
 * @see PluginDependency
 * @see JsonDeserializer
 */
public final class PluginDependencyDeserializer implements JsonDeserializer<PluginDependency> {

    private static final String NAME = "name";
    private static final String REQUIRED = "required";

    @Override
    public PluginDependency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();
        return new PluginDependency(JsonUtil.getRequiredString(NAME, object),
                JsonUtil.getOptionalBoolean(REQUIRED, object, true));
    }
}
