package net.hypejet.jet.server.test.network.protocol.packet.client.status;

import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.test.network.protocol.packet.client.ClientPacketTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing {@linkplain net.hypejet.jet.protocol.packet.client.ClientStatusPacket
 * client status packets}.
 *
 * @since 1.0
 * @author Codsetech
 * @see net.hypejet.jet.protocol.packet.client.ClientStatusPacket
 */
public final class ClientStatusPacketsTest {
    @Test
    public void serverListRequestTest() {
        ClientPacketTestUtil.testPacket(new ClientServerListRequestStatusPacket());
    }

    @Test
    public void pingRequestTest() {
        ClientPacketTestUtil.testPacket(new ClientPingRequestStatusPacket(34671432));
    }
}