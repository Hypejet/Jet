package net.hypejet.jet.server.test.network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.properties.GameProfileProperties;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.profile.GameProfilePropertiesCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Represents a test, which tests available {@linkplain NetworkCodec
 * network codecs}.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkCodec
 */
public final class CodecTest {
    @Test
    public void testGameProfileCodec() {
        GameProfileProperties properties = new GameProfileProperties(
                UUID.randomUUID(), "some-username", "some-signature"
        );

        GameProfilePropertiesCodec codec = GameProfilePropertiesCodec.instance();
        ByteBuf buf = Unpooled.buffer();

        codec.write(buf, properties);
        Assertions.assertEquals(properties, codec.read(buf));
    }
}