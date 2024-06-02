package net.hypejet.jet.server.test.network.codec;

import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.buffer.codec.codecs.GameProfilePropertiesCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Represents a test, which tests available {@linkplain net.hypejet.jet.server.network.buffer.codec.NetworkCodec
 * network codecs}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.server.network.buffer.codec.NetworkCodec
 */
public final class CodecTest {
    @Test
    public void testGameProfileCodec() {
        GameProfileProperties properties = GameProfileProperties.builder()
                .signature("some-signature")
                .uniqueId(UUID.randomUUID())
                .username("some-username")
                .build();

        GameProfilePropertiesCodec codec = GameProfilePropertiesCodec.instance();
        NetworkBuffer buffer = NetworkBuffer.create();

        codec.write(buffer, properties);
        Assertions.assertEquals(properties, codec.read(buffer));
    }
}