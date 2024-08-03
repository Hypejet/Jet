package net.hypejet.jet.server.test.network.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestLoginPacket;
import net.hypejet.jet.server.test.network.protocol.packet.server.ServerPacketTestUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

/**
 * Represents a test of reading and writing {@linkplain net.hypejet.jet.protocol.packet.server.ServerLoginPacket server
 * login packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerLoginPacket
 */
public final class ServerLoginPacketsTest {
    @Test
    public void testDisconnect() {
        ServerPacketTestUtil.testPacket(
                new ServerDisconnectLoginPacket(Component.text("You got disconnected", NamedTextColor.RED))
        );
    }

    @Test
    public void testEncryptionRequest() {
        ServerPacketTestUtil.testPacket(new ServerEncryptionRequestLoginPacket(
                "a-server-identifier", new byte[] { 1, 3, 5 }, new byte[] { 24, 51, 51, 73 }, true
        ));
    }

    @Test
    public void testLoginSuccess() {
        ServerPacketTestUtil.testPacket(new ServerLoginSuccessLoginPacket(
                UUID.randomUUID(),
                "A-Player",
                List.of(
                        new ServerLoginSuccessLoginPacket.Property("a-key", "a-value", null),
                        new ServerLoginSuccessLoginPacket.Property("some-key", "the-value", "a-signature")
                ),
                true
        ));
    }

    @Test
    public void testEnableCompression() {
        ServerPacketTestUtil.testPacket(new ServerEnableCompressionLoginPacket(325));
    }

    @Test
    public void testPluginMessageRequest() {
        ServerPacketTestUtil.testPacket(new ServerPluginMessageRequestLoginPacket(
                323, Key.key(".a-plugin.", "message-"), new byte[] { 16, 42, 72, 95, 82 }
        ));
    }

    @Test
    public void testCookieRequest() {
        ServerPacketTestUtil.testPacket(new ServerCookieRequestLoginPacket(Key.key("a-simple", "cookie-request")));
    }
}