package net.hypejet.jet.server.network.codec.packet.server.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerListResponseStatusPacket a server list response status packet}.
 * 
 * @since 1.0
 * @author Codestech
 * @see ServerListResponseStatusPacket
 * @see NetworkWriter
 */
public final class ServerListResponseStatusPacketWriter implements NetworkWriter<ServerListResponseStatusPacket> {

    private static final ServerListPingNetworkWriter SERVER_LIST_PING_WRITER = new ServerListPingNetworkWriter();

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerListResponseStatusPacket object) {
        SERVER_LIST_PING_WRITER.write(buf, object.ping());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerListPing a server list
     * ping}.
     *
     * @since 1.0
     * @see ServerListPing
     * @see NetworkWriter
     */
    private static final class ServerListPingNetworkWriter implements NetworkWriter<ServerListPing> {

        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(ServerListPing.Favicon.class, new FaviconSerializer())
                .registerTypeAdapter(ServerListPing.PingPlayer.class, new PingPlayerSerializer())
                .registerTypeAdapter(ServerListPing.Players.class, new PlayersSerializer())
                .registerTypeAdapter(ServerListPing.class, new ServerListPingSerializer())
                .registerTypeAdapter(ServerListPing.Version.class, new VersionSerializer())
                .create();

        private ServerListPingNetworkWriter() {}

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull ServerListPing object) {
            StringNetworkCodec.INSTANCE.write(buf, GSON.toJson(object));
        }
    }

    /**
     * Represents {@linkplain JsonSerializer a json serializer}, which serializes {@linkplain Component a component} to
     * {@linkplain JsonElement a json element}.
     *
     * @since 1.0
     * @see Component
     * @see JsonSerializer
     */
    private static final class ComponentSerializer implements JsonSerializer<Component> {

        private ComponentSerializer() {}

        @Override
        public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
            return GsonComponentSerializer.gson().serializeToTree(src);
        }
    }

    /**
     * Represents {@linkplain JsonSerializer a json serializer}, which serializes
     * {@linkplain ServerListPing.Favicon a server list ping favicon} to {@linkplain JsonElement a json element}.
     *
     * @since 1.0
     * @see ServerListPing.Favicon
     * @see JsonSerializer
     */
    private static final class FaviconSerializer implements JsonSerializer<ServerListPing.Favicon> {

        private static final String PREFIX = "data:image/" + ServerListPing.faviconFormatName() + ";base64,";

        private FaviconSerializer() {}

        @Override
        public JsonElement serialize(ServerListPing.Favicon src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(PREFIX + src.image());
        }
    }

    /**
     * Represents {@linkplain JsonSerializer a json serializer}, which serializes
     * {@linkplain ServerListPing.PingPlayer a server list ping player} to {@linkplain JsonElement a json element}.
     *
     * @since 1.0
     * @see ServerListPing.PingPlayer
     * @see JsonSerializer
     */
    private static final class PingPlayerSerializer implements JsonSerializer<ServerListPing.PingPlayer> {

        private static final String NAME_FIELD = "name";
        private static final String ID_FIELD = "id";

        private PingPlayerSerializer() {}

        @Override
        public JsonElement serialize(ServerListPing.PingPlayer src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty(NAME_FIELD, src.name());
            object.addProperty(ID_FIELD, src.uniqueId().toString());
            return object;
        }
    }

    /**
     * Represents {@linkplain JsonSerializer a json serializer}, which serializes
     * {@linkplain ServerListPing.Players server list ping players} to {@linkplain JsonElement s json element}.
     *
     * @since 1.0
     * @see ServerListPing.Players
     * @see JsonSerializer
     */
    private static final class PlayersSerializer implements JsonSerializer<ServerListPing.Players> {

        private static final String MAX_FIELD = "max";
        private static final String ONLINE_FIELD = "online";
        private static final String SAMPLE_FIELD = "sample";

        private PlayersSerializer() {}

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

    /**
     * Represents {@linkplain JsonSerializer a json serializer}, which serializes {@linkplain ServerListPing a server
     * list ping} to {@linkplain JsonElement a json element}.
     *
     * @since 1.0
     * @see ServerListPing
     * @see JsonSerializer
     */
    private static final class ServerListPingSerializer implements JsonSerializer<ServerListPing>{

        private static final String VERSION_FIELD = "version";
        private static final String PLAYERS_FIELD = "players";
        private static final String DESCRIPTION_FIELD = "description";
        private static final String FAVICON_FIELD = "favicon";
        private static final String ENFORCES_SECURE_CHAT_FIELD = "enforcesSecureChat";
        private static final String PREVIEWS_CHAT_FIELD = "previewsChat";

        private ServerListPingSerializer() {}

        @Override
        public JsonElement serialize(ServerListPing src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add(VERSION_FIELD, context.serialize(src.version()));

            ServerListPing.Players players = src.players();
            if (players != null)
                object.add(PLAYERS_FIELD, context.serialize(players));

            Component description = src.description();
            if (description != null)
                object.add(DESCRIPTION_FIELD, context.serialize(description, Component.class));

            ServerListPing.Favicon favicon = src.favicon();
            if (favicon != null)
                object.add(FAVICON_FIELD, context.serialize(favicon));

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
    }

    /**
     * Represents {@linkplain JsonSerializer a json serializer}, which serializes
     * {@linkplain ServerListPing.Version a server list ping version} to {@linkplain JsonElement a json element}.
     *
     * @since 1.0
     * @see ServerListPing.Version
     * @see JsonSerializer
     */
    private static final class VersionSerializer implements JsonSerializer<ServerListPing.Version> {

        private static final String NAME_FIELD = "name";
        private static final String PROTOCOL_FIELD = "protocol";

        private VersionSerializer() {}

        @Override
        public JsonElement serialize(ServerListPing.Version src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty(NAME_FIELD, src.versionName());
            object.addProperty(PROTOCOL_FIELD, src.protocolVersion());
            return object;
        }
    }
}