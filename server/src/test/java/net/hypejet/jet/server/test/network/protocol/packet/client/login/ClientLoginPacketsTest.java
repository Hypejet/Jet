package net.hypejet.jet.server.test.network.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.server.test.network.protocol.packet.client.ClientPacketTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Represents a test of reading and writing {@linkplain net.hypejet.jet.protocol.packet.client.ClientLoginPacket client
 * login packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.client.ClientLoginPacket
 */
public final class ClientLoginPacketsTest {
    @Test
    public void testLoginRequest() {
        ClientPacketTestUtil.testPacket(new ClientLoginRequestLoginPacket("Dummy", UUID.randomUUID()));
    }

    @Test
    public void testEncryptionResponse() {
        ClientPacketTestUtil.testPacket(new ClientEncryptionResponseLoginPacket(
                new byte[] { 1, 3, 5, 5, 2, 6 }, new byte[] { 5, 6, 2, 5, 7, 8, 3, 5, 7, 2, 6, 1 }
        ));
    }

    @Test
    public void testPluginMessageResponse() {
        ClientPacketTestUtil.testPacket(new ClientPluginMessageResponseLoginPacket(2, true, new byte[] { 1, 2, 3 }));
    }

    @Test
    public void testCookieResponse() {
        ClientPacketTestUtil.testPacket(new ClientCookieResponseLoginPacket(
                Key.key("a", "cookie"), new byte[] { 1, 6, 3 }
        ));
    }

    @Test
    public void testLoginAcknowledge() {
        ClientPacketTestUtil.testPacket(new ClientLoginAcknowledgeLoginPacket());
    }
}