package net.hypejet.jet.server.test.network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.profile.GameProfile;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.profile.GameProfileCodec;
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
        GameProfile profile = new GameProfile(UUID.randomUUID(), "some-username", "some-signature");

        GameProfileCodec codec = GameProfileCodec.instance();
        ByteBuf buf = Unpooled.buffer();

        codec.write(buf, profile);
        Assertions.assertEquals(profile, codec.read(buf));
    }
}