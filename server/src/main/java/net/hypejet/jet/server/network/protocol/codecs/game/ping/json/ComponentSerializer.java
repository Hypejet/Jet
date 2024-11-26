package net.hypejet.jet.server.network.protocol.codecs.game.ping.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;

/**
 * Represents {@linkplain JsonSerializer a json serializer}, which serializes {@linkplain Component a component} to
 * {@linkplain JsonElement a json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see JsonSerializer
 */
public final class ComponentSerializer implements JsonSerializer<Component> {
    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        return GsonComponentSerializer.gson().serializeToTree(src);
    }
}