package net.hypejet.jet.server.plugin.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypejet.jet.data.codecs.util.JsonUtil;
import net.hypejet.jet.plugin.dependency.PluginDependency;
import net.hypejet.jet.server.plugin.metadata.PluginMetadata;
import net.kyori.adventure.key.Key;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents {@linkplain JsonDeserializer a json deserializer}, which deserializes
 * {@linkplain PluginMetadata a plugin metadata}.
 *
 * @since 1.0
 * @see PluginMetadata
 * @see JsonDeserializer
 */
public final class PluginMetadataDeserializer implements JsonDeserializer<PluginMetadata> {

    private static final String NAME = "name";
    private static final String VERSION = "version";
    private static final String ENTRYPOINTS = "entrypoints";
    private static final String AUTHORS = "authors";
    private static final String DEPENDENCIES = "dependencies";

    @Override
    public PluginMetadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();

        JsonObject entrypointsObject = object.getAsJsonObject(ENTRYPOINTS);
        Map<Key, String> entrypoints = new HashMap<>();

        if (entrypointsObject != null) {
            for (Map.Entry<String, JsonElement> entry : entrypointsObject.entrySet()) {
                entrypoints.put(Key.key(entry.getKey()), entry.getValue().getAsString());
            }
        }

        Set<String> authors = new HashSet<>();
        JsonArray authorsArray = object.getAsJsonArray(AUTHORS);

        if (authorsArray != null)
            authorsArray.forEach(element -> authors.add(element.getAsString()));

        Set<PluginDependency> dependencies = new HashSet<>();
        JsonArray dependenciesArray = object.getAsJsonArray(DEPENDENCIES);

        if (dependenciesArray != null) {
            for (JsonElement element : dependenciesArray) {
                dependencies.add(context.deserialize(element, PluginDependency.class));
            }
        }

        return new PluginMetadata(
                JsonUtil.read(NAME, String.class, object, context),
                JsonUtil.read(VERSION, String.class, object, context),
                Map.copyOf(entrypoints),
                Set.copyOf(authors),
                Set.copyOf(dependencies)
        );
    }
}