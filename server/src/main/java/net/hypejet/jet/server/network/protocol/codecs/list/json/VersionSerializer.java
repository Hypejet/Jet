package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes a {@linkplain ServerListPing.Version server list ping version} to
 * a {@linkplain JsonElement json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.Version
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class VersionSerializer implements JsonSerializer<ServerListPing.Version>,
        JsonDeserializer<ServerListPing.Version> {

    private static final String NAME_FIELD = "name";
    private static final String PROTOCOL_FIELD = "protocol";
   ;
    @Override
    public JsonElement serialize(ServerListPing.Version src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(NAME_FIELD, src.versionName());
        object.addProperty(PROTOCOL_FIELD, src.protocolVersion());
        return object;
    }

    @Override
    public ServerListPing.Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();
        return new ServerListPing.Version(object.get(NAME_FIELD).getAsString(), object.get(PROTOCOL_FIELD).getAsInt());
    }
}