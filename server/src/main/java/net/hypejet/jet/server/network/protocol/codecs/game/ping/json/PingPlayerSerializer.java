package net.hypejet.jet.server.network.protocol.codecs.game.ping.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;

/**
 * Represents {@linkplain JsonSerializer a json serializer}, which serializes {@linkplain ServerListPing.PingPlayer
 * a server list ping player} to {@linkplain JsonElement a json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.PingPlayer
 * @see JsonSerializer
 */
public final class PingPlayerSerializer implements JsonSerializer<ServerListPing.PingPlayer> {

    private static final String NAME_FIELD = "name";
    private static final String ID_FIELD = "id";

    @Override
    public JsonElement serialize(ServerListPing.PingPlayer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(NAME_FIELD, src.name());
        object.addProperty(ID_FIELD, src.uniqueId().toString());
        return object;
    }
}