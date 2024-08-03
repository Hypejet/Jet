package net.hypejet.jet.server.test.network.protocol.codecs.component;

import net.hypejet.jet.server.network.protocol.codecs.component.JsonComponentNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain JsonComponentNetworkCodec json component network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see JsonComponentNetworkCodec
 */
public final class JsonComponentNetworkCodecTest {
    @Test
    public void testNonStyled() {
        NetworkCodecTestUtil.test(JsonComponentNetworkCodec.instance(), Component.text("A non-styled component"));
    }

    @Test
    public void testStyled() {
        Component styled = Component.text("A styled component", NamedTextColor.YELLOW, TextDecoration.BOLD);
        NetworkCodecTestUtil.test(JsonComponentNetworkCodec.instance(), styled);
    }

    @Test
    public void testMultiLined() {
        Component multiLined = Component.text("A first line", NamedTextColor.RED)
                .appendNewline()
                .append(Component.text("A second line", NamedTextColor.GOLD, TextDecoration.UNDERLINED))
                .appendNewline()
                .append(Component.text("A third line").style(Style.empty()))
                .appendNewline()
                .appendSpace();
        NetworkCodecTestUtil.test(JsonComponentNetworkCodec.instance(), multiLined);
    }
}