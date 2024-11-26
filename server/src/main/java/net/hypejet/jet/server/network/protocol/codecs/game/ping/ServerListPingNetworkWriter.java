package net.hypejet.jet.server.network.protocol.codecs.game.ping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.ping.json.ComponentSerializer;
import net.hypejet.jet.server.network.protocol.codecs.game.ping.json.FaviconSerializer;
import net.hypejet.jet.server.network.protocol.codecs.game.ping.json.PingPlayerSerializer;
import net.hypejet.jet.server.network.protocol.codecs.game.ping.json.PlayersSerializer;
import net.hypejet.jet.server.network.protocol.codecs.game.ping.json.ServerListPingSerializer;
import net.hypejet.jet.server.network.protocol.codecs.game.ping.json.VersionSerializer;
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
public final class ServerListPingNetworkWriter implements NetworkWriter<ServerListPing> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Component.class, new ComponentSerializer())
            .registerTypeAdapter(ServerListPing.Favicon.class, new FaviconSerializer())
            .registerTypeAdapter(ServerListPing.PingPlayer.class, new PingPlayerSerializer())
            .registerTypeAdapter(ServerListPing.Players.class, new PlayersSerializer())
            .registerTypeAdapter(ServerListPing.class, new ServerListPingSerializer())
            .registerTypeAdapter(ServerListPing.Version.class, new VersionSerializer())
            .create();

    /**
     * An instance of {@linkplain ServerListPingNetworkWriter a server list ping codec}.
     *
     * @since 1.0
     */
    public static final ServerListPingNetworkWriter INSTANCE = new ServerListPingNetworkWriter();

    private ServerListPingNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerListPing object) {
        StringNetworkCodec.INSTANCE.write(buf, GSON.toJson(object));
    }
}