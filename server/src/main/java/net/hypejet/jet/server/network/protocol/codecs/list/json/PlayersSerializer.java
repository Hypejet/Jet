package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes {@linkplain ServerListPing.Players players} to a {@linkplain JsonElement json
 * element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.Players
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class PlayersSerializer implements JsonSerializer<ServerListPing.Players>,
        JsonDeserializer<ServerListPing.Players> {

    private static final String MAX_FIELD = "max";
    private static final String ONLINE_FIELD = "online";
    private static final String SAMPLE_FIELD = "sample";

    @Override
    public JsonElement serialize(ServerListPing.Players src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject playersJson = new JsonObject();

        playersJson.addProperty(MAX_FIELD, src.max());
        playersJson.addProperty(ONLINE_FIELD, src.online());

        JsonArray sampleJson = new JsonArray();

        for (ServerListPing.PingPlayer player : src.players()) {
            sampleJson.add(context.serialize(player));
        }

        playersJson.add(SAMPLE_FIELD, sampleJson);
        return playersJson;
    }

    @Override
    public ServerListPing.Players deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();

        List<JsonElement> sampleJson = object.get(SAMPLE_FIELD).getAsJsonArray().asList();
        List<ServerListPing.PingPlayer> sample = new ArrayList<>();

        for (JsonElement element : sampleJson) {
            sample.add(context.deserialize(element, ServerListPing.PingPlayer.class));
        }

        return new ServerListPing.Players(
                object.get(MAX_FIELD).getAsInt(),
                object.get(ONLINE_FIELD).getAsInt(),
                sample
        );
    }
}