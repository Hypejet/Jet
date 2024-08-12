package net.hypejet.jet.server.plugin.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypejet.jet.plugin.metadata.PluginVersion;
import net.hypejet.jet.server.util.json.JsonUtil;

import java.lang.reflect.Type;

/**
 * Represents a {@linkplain JsonDeserializer json deserializer}, which deserializes a {@linkplain PluginVersion plugin
 * version}.
 *
 * @since 1.0
 * @author Codestech
 * @see PluginVersion
 * @see JsonDeserializer
 */
public final class PluginVersionDeserializer implements JsonDeserializer<PluginVersion> {

    private static final String NAME = "name";
    private static final String BACKWARDS_COMPATIBLE = "backwards-compatible";
    private static final String FORWARD_COMPATIBLE = "forward-compatible";

    @Override
    public PluginVersion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (!(json instanceof JsonObject object))
            return new PluginVersion(json.getAsString(), false, false);
        return new PluginVersion(JsonUtil.getRequiredString(NAME, object),
                JsonUtil.getOptionalBoolean(BACKWARDS_COMPATIBLE, object, false),
                JsonUtil.getOptionalBoolean(FORWARD_COMPATIBLE, object, false));
    }
}