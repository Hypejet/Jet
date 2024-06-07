package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes a {@linkplain Component component} to a {@linkplain JsonElement json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class ComponentSerializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        return GsonComponentSerializer.gson().serializeToTree(src);
    }

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return GsonComponentSerializer.gson().deserializeFromTree(json);
    }
}