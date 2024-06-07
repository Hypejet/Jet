package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes a {@linkplain ServerListPing.PingPlayer server list ping player} to
 * a {@linkplain JsonElement json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.PingPlayer
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class PingPlayerSerializer implements JsonSerializer<ServerListPing.PingPlayer>,
        JsonDeserializer<ServerListPing.PingPlayer> {

    private static final String NAME_FIELD = "name";
    private static final String ID_FIELD = "id";

    @Override
    public JsonElement serialize(ServerListPing.PingPlayer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(NAME_FIELD, src.name());
        object.addProperty(ID_FIELD, src.uniqueId().toString());
        return object;
    }

    @Override
    public ServerListPing.PingPlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();
        return new ServerListPing.PingPlayer(object.get(NAME_FIELD).getAsString(),
                UUID.fromString(object.get(ID_FIELD).getAsString()));
    }
}