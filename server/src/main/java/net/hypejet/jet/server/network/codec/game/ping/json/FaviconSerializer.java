package net.hypejet.jet.server.network.codec.game.ping.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;

/**
 * Represents {@linkplain JsonSerializer a json serializer}, which serializes
 * {@linkplain ServerListPing.Favicon a server list ping favicon} to {@linkplain JsonElement a json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.Favicon
 * @see JsonSerializer
 */
public final class FaviconSerializer implements JsonSerializer<ServerListPing.Favicon> {

    private static final String PREFIX = "data:image/" + ServerListPing.faviconFormatName() + ";base64,";

    @Override
    public JsonElement serialize(ServerListPing.Favicon src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(PREFIX + src.image());
    }
}