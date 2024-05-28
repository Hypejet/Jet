package net.hypejet.jet.server.test.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.server.util.NetworkUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A test for {@link NetworkUtil network util} methods.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkUtilTest {
    @Test
    public void varIntTest() {
        int expected = 356;

        ByteBuf buf = Unpooled.buffer();
        NetworkUtil.writeVarInt(buf, expected);

        Assertions.assertEquals(NetworkUtil.readVarInt(buf), expected);
    }

    @Test
    public void varLongTest() {
        long expected = 1564;

        ByteBuf buf = Unpooled.buffer();
        NetworkUtil.writeVarLong(buf, expected);

        Assertions.assertEquals(NetworkUtil.readVarLong(buf), expected);
    }

    @Test
    public void stringTest() {
        String expected = "Jet-Is-A-Powerful-Minecraft-Server-Software";

        ByteBuf buf = Unpooled.buffer();
        NetworkUtil.writeString(buf, expected);

        Assertions.assertEquals(NetworkUtil.readString(buf), expected);
    }
}