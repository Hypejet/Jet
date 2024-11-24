package net.hypejet.jet.server.test.network.protocol.codecs.link;

import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.link.label.BuiltinLabel;
import net.hypejet.jet.link.label.ComponentLabel;
import net.hypejet.jet.server.network.protocol.codecs.link.ServerLinkNetworkWriter;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain ServerLinkNetworkWriter server link network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLinkNetworkWriter
 */
public final class ServerLinkNetworkWriterTest {
    @Test
    public void testComponent() {
        ServerLinkNetworkWriter codec = ServerLinkNetworkWriter.instance();
        ServerLink link = new ServerLink(new ComponentLabel(Component.text("A server link")), "https://hypejet.net");
        NetworkCodecTestUtil.test(codec, link);
    }

    @Test
    public void testBuiltIn() {
        ServerLinkNetworkWriter codec = ServerLinkNetworkWriter.instance();
        ServerLink link = new ServerLink(BuiltinLabel.BUG_REPORT, "https://bugreport.hypejet.net");
        NetworkCodecTestUtil.test(codec, link);
    }
}