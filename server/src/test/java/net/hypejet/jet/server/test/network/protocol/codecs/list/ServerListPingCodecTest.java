package net.hypejet.jet.server.test.network.protocol.codecs.list;

import com.google.gson.JsonObject;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.server.network.protocol.codecs.list.ServerListPingCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

/**
 * Represents a test of reading and writing of {@linkplain ServerListPingCodec server list ping codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPingCodec
 */
public final class ServerListPingCodecTest {
    @Test
    public void test() {
        JsonObject customData = new JsonObject();
        customData.addProperty("property-one", 2);
        customData.addProperty("another-property", "I do not know");

        ServerListPing ping = new ServerListPing(
                new ServerListPing.Version("1.7.10", 5),
                new ServerListPing.Players(5, 3, Set.of(
                        new ServerListPing.PingPlayer("Codestech", UUID.randomUUID()),
                        new ServerListPing.PingPlayer("AnotherPlayer", UUID.randomUUID())
                )),
                Component.text("A \n description", NamedTextColor.RED, TextDecoration.UNDERLINED),
                new ServerListPing.Favicon("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"),
                false,
                false,
                customData
        );

        NetworkCodecTestUtil.test(ServerListPingCodec.instance(), ping);
    }
}