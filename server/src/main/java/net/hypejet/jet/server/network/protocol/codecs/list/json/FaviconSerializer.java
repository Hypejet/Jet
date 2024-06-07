package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes a {@linkplain ServerListPing.Favicon server list ping favicon} to
 * a {@linkplain JsonElement json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.Favicon
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class FaviconSerializer implements JsonSerializer<ServerListPing.Favicon>,
        JsonDeserializer<ServerListPing.Favicon> {

    private static final String PREFIX = "data:image/" + ServerListPing.faviconFormatName() + ";base64,";

    @Override
    public JsonElement serialize(ServerListPing.Favicon src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(PREFIX + src.image());
    }

    @Override
    public ServerListPing.Favicon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return new ServerListPing.Favicon(json.getAsString().replaceFirst(PREFIX, ""));
    }
}