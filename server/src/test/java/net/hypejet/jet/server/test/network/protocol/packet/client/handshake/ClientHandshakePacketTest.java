package net.hypejet.jet.server.test.network.protocol.packet.client.handshake;

import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.test.network.protocol.packet.client.ClientPacketTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing a {@linkplain ClientHandshakePacket client handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientHandshakePacket
 */
public final class ClientHandshakePacketTest {
    @Test
    public void test() {
        ClientPacketTestUtil.testPacket(new ClientHandshakePacket(
                3, "localhost", 25565, ClientHandshakePacket.HandshakeIntent.STATUS
        ));
    }
}