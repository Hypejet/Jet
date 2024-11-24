package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;

/**
 * Represents {@linkplain JsonSerializer a json serializer}, which serializes {@linkplain ServerListPing.Players server
 * list ping players} to {@linkplain JsonElement s json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.Players
 * @see JsonSerializer
 */
public final class PlayersSerializer implements JsonSerializer<ServerListPing.Players> {

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
}