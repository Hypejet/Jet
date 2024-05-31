package net.hypejet.jet.server.test.network.protocol;

import io.netty.buffer.Unpooled;
import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.login.compression.ServerEnableCompressionPacket;
import net.hypejet.jet.protocol.packet.server.login.cookie.ServerCookieRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.disconnect.ServerDisconnectPacket;
import net.hypejet.jet.protocol.packet.server.login.encryption.ServerEncryptionRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.plugin.ServerPluginMessageRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.success.ServerLoginSuccessPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.buffer.codec.codecs.GameProfilePropertiesCodec;
import net.hypejet.jet.server.network.protocol.ServerPacketRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Represents a test for {@link ServerPacketRegistry server packet registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacketRegistry
 */
public final class ServerPacketRegistryTest {
    @Test
    public void testDisconnect() {
        Component reason = Component.text(
                "Jet is the best server software ever. Isn't it?",
                NamedTextColor.RED,
                TextDecoration.BOLD
        );

        ServerDisconnectPacket packet = ServerDisconnectPacket.builder()
                .reason(reason)
                .build();

        test(packet, buffer -> Assertions.assertEquals(reason, buffer.readJsonTextComponent()));
    }

    @Test
    public void testCompression() {
        int compressionThreshold = ThreadLocalRandom.current().nextInt();

        ServerEnableCompressionPacket packet = ServerEnableCompressionPacket.builder()
                .threshold(compressionThreshold)
                .build();

        test(packet, buffer -> Assertions.assertEquals(compressionThreshold, buffer.readVarInt()));
    }

    @Test
    public void testCookieRequest() {
        Key identifier = Key.key("hypejet", "jet");

        ServerCookieRequestPacket packet = ServerCookieRequestPacket.builder()
                .identifier(identifier)
                .build();

        test(packet, buffer -> Assertions.assertEquals(identifier, buffer.readIdentifier()));
    }

    @Test
    public void testEncryptionRequest() {
        String serverId = "a-server-identifier";

        byte[] publicKey = new byte[1024];
        byte[] verifyToken = new byte[4];

        ThreadLocalRandom random = ThreadLocalRandom.current();

        random.nextBytes(publicKey);
        random.nextBytes(verifyToken);

        boolean shouldAuthenticate  = random.nextBoolean();

        ServerEncryptionRequestPacket packet = ServerEncryptionRequestPacket.builder()
                .serverId(serverId)
                .publicKey(publicKey)
                .verifyToken(verifyToken)
                .shouldAuthenticate(shouldAuthenticate)
                .build();

        test(packet, buffer -> {
            Assertions.assertEquals(serverId, buffer.readString());
            Assertions.assertArrayEquals(publicKey, buffer.readByteArray());
            Assertions.assertArrayEquals(verifyToken, buffer.readByteArray());
            Assertions.assertEquals(shouldAuthenticate, buffer.readBoolean());
        });
    }

    @Test
    public void testLoginSuccess() {
        UUID uuid = UUID.randomUUID();
        String username = "Codestech";

        GameProfileProperties properties = GameProfileProperties.builder()
                .uniqueId(UUID.randomUUID())
                .username("notch")
                .signature("a-very-long-signature-but-not-random")
                .build();

        boolean strictErrorHandling = ThreadLocalRandom.current().nextBoolean();

        ServerLoginSuccessPacket packet = ServerLoginSuccessPacket.builder()
                .uniqueId(uuid)
                .username(username)
                .properties(properties)
                .strictErrorHandling(strictErrorHandling)
                .build();

        test(packet, buffer -> {
            Assertions.assertEquals(uuid, buffer.readUniqueId());
            Assertions.assertEquals(username, buffer.readString());
            Assertions.assertEquals(List.of(properties), buffer.readCollection(GameProfilePropertiesCodec.instance()));
            Assertions.assertEquals(strictErrorHandling, buffer.readBoolean());
        });
    }

    @Test
    public void testPluginMessageRequestPacket() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int identifier = random.nextInt();
        Key channel = Key.key("hypejet", "jet");
        byte[] data = new byte[45];

        random.nextBytes(data);

        ServerPluginMessageRequestPacket packet = ServerPluginMessageRequestPacket.builder()
                .messageId(identifier)
                .channel(channel)
                .data(data)
                .build();

        test(packet, buffer -> {
            Assertions.assertEquals(identifier, buffer.readVarInt());
            Assertions.assertEquals(channel, buffer.readIdentifier());
            Assertions.assertArrayEquals(data, buffer.readByteArray(false));
        });
    }

    private static <P extends ServerPacket> void test(@NonNull P packet, @NonNull Consumer<NetworkBuffer> assertions) {
        NetworkBuffer buffer = new NetworkBuffer(Unpooled.buffer());
        ServerPacketRegistry.write(buffer, packet);

        buffer.readVarInt(); // Read packet identifier
        assertions.accept(buffer);
    }
}