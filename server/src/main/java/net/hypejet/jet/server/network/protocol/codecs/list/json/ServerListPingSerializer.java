package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.hypejet.jet.ping.ServerListPing;
import net.kyori.adventure.text.Component;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes a {@linkplain ServerListPing server list ping} to a {@linkplain JsonElement json
 * element}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class ServerListPingSerializer implements JsonSerializer<ServerListPing>,
        JsonDeserializer<ServerListPing> {

    private static final String VERSION_FIELD = "version";
    private static final String PLAYERS_FIELD = "players";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String FAVICON_FIELD = "favicon";
    private static final String ENFORCES_SECURE_CHAT_FIELD = "enforcesSecureChat";
    private static final String PREVIEWS_CHAT_FIELD = "previewsChat";

    @Override
    public JsonElement serialize(ServerListPing src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add(VERSION_FIELD, context.serialize(src.version()));

        ServerListPing.Players players = src.players();
        if (players != null) object.add(PLAYERS_FIELD, context.serialize(players));

        Component description = src.description();
        if (description != null) object.add(DESCRIPTION_FIELD, context.serialize(description, Component.class));

        ServerListPing.Favicon favicon = src.favicon();
        if (favicon != null) object.add(FAVICON_FIELD, context.serialize(favicon));

        object.addProperty(ENFORCES_SECURE_CHAT_FIELD, src.enforcesSecureChat());
        object.addProperty(PREVIEWS_CHAT_FIELD, src.previewsChat());

        JsonObject customData = src.customData();

        if (customData != null) {
            for (Map.Entry<String, JsonElement> entry : customData.entrySet()) {
                if (object.has(entry.getKey())) continue;
                object.add(entry.getKey(), entry.getValue());
            }
        }

        return object;
    }

    @Override
    public ServerListPing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();

        ServerListPing.Version version = context.deserialize(object.remove(VERSION_FIELD), ServerListPing.Version.class);

        JsonElement playersJson = object.remove(PLAYERS_FIELD);
        ServerListPing.Players players = null;

        if (playersJson != null) {
            players = context.deserialize(playersJson, ServerListPing.Players.class);
        }

        JsonElement descriptionJson = object.remove(DESCRIPTION_FIELD);
        Component description = null;

        if (descriptionJson != null) {
            description = context.deserialize(descriptionJson, Component.class);
        }

        JsonElement faviconJson = object.remove(FAVICON_FIELD);
        ServerListPing.Favicon favicon = null;

        if (faviconJson != null) {
            favicon = context.deserialize(faviconJson, ServerListPing.Favicon.class);
        }

        boolean enforcesSecureChat = object.remove(ENFORCES_SECURE_CHAT_FIELD).getAsBoolean();
        boolean previewsChat = object.remove(PREVIEWS_CHAT_FIELD).getAsBoolean();

        return new ServerListPing(version, players, description, favicon, enforcesSecureChat, previewsChat, object);
    }
}