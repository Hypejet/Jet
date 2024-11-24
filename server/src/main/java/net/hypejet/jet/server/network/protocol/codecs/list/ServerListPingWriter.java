package net.hypejet.jet.server.network.protocol.codecs.list;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.list.json.ComponentSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.FaviconSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.PingPlayerSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.PlayersSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.ServerListPingSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.VersionSerializer;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerListPing a server list ping}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing
 * @see NetworkWriter
 */
public final class ServerListPingWriter implements NetworkWriter<ServerListPing> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Component.class, new ComponentSerializer())
            .registerTypeAdapter(ServerListPing.Favicon.class, new FaviconSerializer())
            .registerTypeAdapter(ServerListPing.PingPlayer.class, new PingPlayerSerializer())
            .registerTypeAdapter(ServerListPing.Players.class, new PlayersSerializer())
            .registerTypeAdapter(ServerListPing.class, new ServerListPingSerializer())
            .registerTypeAdapter(ServerListPing.Version.class, new VersionSerializer())
            .create();

    /**
     * An instance of {@linkplain ServerListPingWriter a server list ping codec}.
     *
     * @since 1.0
     */
    public static final ServerListPingWriter INSTANCE = new ServerListPingWriter();

    private ServerListPingWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerListPing object) {
        StringNetworkCodec.instance().write(buf, GSON.toJson(object));
    }
}