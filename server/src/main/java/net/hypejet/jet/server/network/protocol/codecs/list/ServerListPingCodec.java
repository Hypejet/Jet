package net.hypejet.jet.server.network.protocol.codecs.list;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.list.json.ComponentSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.FaviconSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.PingPlayerSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.PlayersSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.ServerListPingSerializer;
import net.hypejet.jet.server.network.protocol.codecs.list.json.VersionSerializer;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain ServerListPing server
 * list ping}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing
 * @see NetworkCodec
 */
public final class ServerListPingCodec implements NetworkCodec<ServerListPing> {

    private static final ServerListPingCodec INSTANCE = new ServerListPingCodec();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Component.class, new ComponentSerializer())
            .registerTypeAdapter(ServerListPing.Favicon.class, new FaviconSerializer())
            .registerTypeAdapter(ServerListPing.PingPlayer.class, new PingPlayerSerializer())
            .registerTypeAdapter(ServerListPing.Players.class, new PlayersSerializer())
            .registerTypeAdapter(ServerListPing.class, new ServerListPingSerializer())
            .registerTypeAdapter(ServerListPing.Version.class, new VersionSerializer())
            .create();

    private ServerListPingCodec() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull ServerListPing read(@NonNull ByteBuf buf) {
        return GSON.fromJson(StringNetworkCodec.instance().read(buf), ServerListPing.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerListPing object) {
        StringNetworkCodec.instance().write(buf, GSON.toJson(object));
    }

    /**
     * Gets an instance of the {@linkplain ServerListPingCodec server list ping codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ServerListPingCodec instance() {
        return INSTANCE;
    }
}