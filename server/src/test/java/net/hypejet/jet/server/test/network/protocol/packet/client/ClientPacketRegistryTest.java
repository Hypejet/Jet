package net.hypejet.jet.server.test.network.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test for reading and writing packets from a {@link ClientPacketRegistry client packet registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacketRegistry
 */
public final class ClientPacketRegistryTest {
    @Test
    public void testValidPacket() {
        Assertions.assertTrue(ClientPacketRegistry.hasCodec(0, ProtocolState.HANDSHAKE));
    }

    @Test
    public void testInvalidPacket() {
        Assertions.assertFalse(ClientPacketRegistry.hasCodec(1, ProtocolState.HANDSHAKE));
    }
}