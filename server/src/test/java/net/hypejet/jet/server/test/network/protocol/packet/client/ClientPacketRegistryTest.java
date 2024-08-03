package net.hypejet.jet.server.test.network.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of getting
 * {@linkplain net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec client packet codecs}
 * from a {@link ClientPacketRegistry client packet registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec
 * @see ClientPacketRegistry
 */
public final class ClientPacketRegistryTest {
    @Test
    public void testValidPacket() {
        Assertions.assertNotNull(ClientPacketRegistry.codec(0, ProtocolState.HANDSHAKE));
    }

    @Test
    public void testInvalidPacket() {
        Assertions.assertNull(ClientPacketRegistry.codec(1, ProtocolState.HANDSHAKE));
    }
}