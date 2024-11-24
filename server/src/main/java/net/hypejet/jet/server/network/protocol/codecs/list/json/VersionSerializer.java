package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;

import java.lang.reflect.Type;

/**
 * Represents {@linkplain JsonSerializer a json serializer}, which serializes
 * {@linkplain ServerListPing.Version a server list ping version} to {@linkplain JsonElement a json element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing.Version
 * @see JsonSerializer
 */
public final class VersionSerializer implements JsonSerializer<ServerListPing.Version> {

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
}